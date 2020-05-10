package onion.w4v3xrmknycexlsd.lib.sgfview.data

/**
 * The parser object for reading strings into [SgfTree]s.
 *
 * It is very lenient, in that it will not throw errors for incorrect input, but it will try to
 * parse it as good as it can. Of course, that means that for incorrect input, the results might be
 * completely wrong (it will not try to correct anything).
 *
 * Note that only one SGF `GameTree` per input is considered, following ones will be ignored.
 */
object SgfParser {
    private enum class ParseState {
        EXPECT_ANYTHING,
        PARSE_PROPIDENT,
        PARSE_PROPVALUE,
        ESCAPE,
    }

    private var state = ParseState.EXPECT_ANYTHING

    /** Parses the [sgfString] representing an SGF `GameTree` into an [SgfTree] object. */
    fun parseSgfTree(sgfString: String): SgfTree? {
        var currentTree = SgfTree()

        // we parse character by character, state dependent
        // maintaining property identifier and value strings parsed so far
        // as well as the last property used (for those properties accepting multiple values)
        var propIdentString = ""
        var propValueString = ""
        var currentProp: SgfProperty<SgfType<*>>? = null

        for (c in sgfString) {
            when (state) {
                // whenever we are neither reading a property identifier or value, anything could come:
                // * a new `GameTree`, beginning with '('
                // * the end of a `GameTree`, ')'
                // * a new node, beginning with ';'
                // * a new property, beginning with a capital letter
                // * a new property value, beginning with '['
                ParseState.EXPECT_ANYTHING -> when (c) {
                    '(' -> { // make new [SgfTree] and descent into it
                        val newTree = SgfTree()
                        newTree.parent = currentTree
                        currentTree.children.add(newTree)
                        currentTree = newTree
                    }
                    ')' -> currentTree.parent?.let { currentTree = it } // go up
                    ';' -> currentTree.nodes.add(mutableListOf())
                    in 'A'..'Z' -> {
                        propIdentString = "$c"
                        currentProp = null
                        state = ParseState.PARSE_PROPIDENT
                    }
                    '[' -> propValueString = "".also { state = ParseState.PARSE_PROPVALUE }
                }
                // when a capital letter was encountered outside of a property value, we read
                // a property identifier consisting of capitals, character by character
                ParseState.PARSE_PROPIDENT -> when (c) {
                    in 'A'..'Z' -> propIdentString += c
                    // new property value begins:
                    '[' -> propValueString = "".also { state = ParseState.PARSE_PROPVALUE }
                }
                // here we are between [ ] of a property value, accumulating character by character
                ParseState.PARSE_PROPVALUE -> when (c) {
                    '\\' -> state = ParseState.ESCAPE // '\' is escape character
                    ']' -> { // property value is finished, make [SgfProperty] and append to last node
                        parsePropValue(propIdentString, propValueString, currentProp)?.let {
                            // only add if new property, otherwise it was already updated by [parsePropValue]
                            if (currentProp == null) currentTree.nodes.lastOrNull()?.add(it)
                            currentProp = it
                        }
                        state = ParseState.EXPECT_ANYTHING
                    }
                    else -> propValueString += c
                }
                // escaping: this character is inserted verbatim into the property value
                // this is only relevant when parsing `Text` or `SimpleText`s
                // the conversion of whitespace characters is handled when actually reading those values
                ParseState.ESCAPE -> {
                    if (c != '\n') propValueString += c // newline chars after '\' should disappear
                    state = ParseState.PARSE_PROPVALUE
                }
            }
        }

        // we ignore all other GameTrees in this collection except the first
        // and make it root tree
        return currentTree.children.getOrNull(0)?.apply { parent = null }
    }

    // makes a new [SgfProperty] from the [propIdent] identifier and the [propValue] value strings
    // if [property] exists already, in case of a basic data type it is replaced (i.e., old values
    // are ignored); if it is a list type, the parsed result is appended to the current one
    // reflection would lose type safety here, as well as using generics due to the lack of support
    // of union like data types, so we go for the manual approach
    private fun parsePropValue(
        propIdent: String,
        propValue: String,
        property: SgfProperty<SgfType<*>>?
    ): SgfProperty<SgfType<*>>? {
        return when (propIdent) {
            "B" -> (propValue.parseCoordinate() as? SgfType.Move)?.let { SgfProperty.B(it) }
            "W" -> (propValue.parseCoordinate() as? SgfType.Move)?.let { SgfProperty.W(it) }
            "KO" -> SgfProperty.KO
            "MN" -> propValue.parseNumber()?.let { SgfProperty.MN(it) }
            "AB" -> (property as? SgfProperty.AB ?: SgfProperty.AB())
                .apply { value.content.addAll(propValue.parseList()) }
            "AW" -> (property as? SgfProperty.AW ?: SgfProperty.AW())
                .apply { value.content.addAll(propValue.parseList()) }
            "AE" -> (property as? SgfProperty.AE ?: SgfProperty.AE())
                .apply { value.content.addAll(propValue.parseList()) }
            "PL" -> propValue.parseColor()?.let { SgfProperty.PL(it) }
            "C" -> propValue.parseText().let { SgfProperty.C(it) }
            "DM" -> propValue.parseDouble()?.let { SgfProperty.DM(it) }
            "GB" -> propValue.parseDouble()?.let { SgfProperty.GB(it) }
            "GW" -> propValue.parseDouble()?.let { SgfProperty.GW(it) }
            "HO" -> propValue.parseDouble()?.let { SgfProperty.HO(it) }
            "N" -> propValue.parseSimpleText().let { SgfProperty.N(it) }
            "UC" -> propValue.parseDouble()?.let { SgfProperty.UC(it) }
            "V" -> propValue.parseReal()?.let { SgfProperty.V(it) }
            "BM" -> propValue.parseDouble()?.let { SgfProperty.BM(it) }
            "DO" -> SgfProperty.DO
            "IT" -> SgfProperty.IT
            "TE" -> propValue.parseDouble()?.let { SgfProperty.TE(it) }
            "AR" -> (property as? SgfProperty.AR ?: SgfProperty.AR())
                .apply { propValue.parseCompose(String::parsePoint, String::parsePoint)
                    ?.let {value.content.add(it) } }
            "CR" -> (property as? SgfProperty.CR ?: SgfProperty.CR())
                .apply { value.content.addAll(propValue.parseList()) }
            "DD" -> (property as? SgfProperty.DD ?: SgfProperty.DD())
                .apply { value.content.addAll(propValue.parseList()) }
            "LB" -> (property as? SgfProperty.LB ?: SgfProperty.LB())
                .apply { propValue.parseCompose(String::parsePoint, String::parseSimpleText)
                    ?.let {value.content.add(it) } }
            "LN" -> (property as? SgfProperty.LN ?: SgfProperty.LN())
                .apply { propValue.parseCompose(String::parsePoint, String::parsePoint)
                    ?.let {value.content.add(it) } }
            "MA" -> (property as? SgfProperty.MA ?: SgfProperty.MA())
                .apply { value.content.addAll(propValue.parseList()) }
            "SL" -> (property as? SgfProperty.SL ?: SgfProperty.SL())
                .apply { value.content.addAll(propValue.parseList()) }
            "SQ" -> (property as? SgfProperty.SQ ?: SgfProperty.SQ())
                .apply { value.content.addAll(propValue.parseList()) }
            "TR" -> (property as? SgfProperty.TR ?: SgfProperty.TR())
                .apply { value.content.addAll(propValue.parseList()) }
            "AP" -> propValue.parseCompose(String::parseSimpleText, String::parseSimpleText)?.let { SgfProperty.AP(it) }
            "CA" -> propValue.parseSimpleText().let { SgfProperty.CA(it) }
            "FF" -> propValue.parseNumber()?.let { SgfProperty.FF(it) }
            "GM" -> propValue.parseNumber()?.let { SgfProperty.GM(it) }
            "ST" -> propValue.parseNumber()?.let { SgfProperty.ST(it) }
            "SZ" -> propValue.parseCompose(String::parseNumber, String::parseNumber)?.let { SgfProperty.SZ(it) }
            "AN" -> propValue.parseSimpleText().let { SgfProperty.AN(it) }
            "BR" -> propValue.parseSimpleText().let { SgfProperty.BR(it) }
            "BT" -> propValue.parseSimpleText().let { SgfProperty.BT(it) }
            "CP" -> propValue.parseSimpleText().let { SgfProperty.CP(it) }
            "DT" -> propValue.parseSimpleText().let { SgfProperty.DT(it) }
            "EV" -> propValue.parseSimpleText().let { SgfProperty.EV(it) }
            "GN" -> propValue.parseSimpleText().let { SgfProperty.GN(it) }
            "GC" -> propValue.parseText().let { SgfProperty.GC(it) }
            "ON" -> propValue.parseSimpleText().let { SgfProperty.ON(it) }
            "OT" -> propValue.parseSimpleText().let { SgfProperty.OT(it) }
            "PB" -> propValue.parseSimpleText().let { SgfProperty.PB(it) }
            "PC" -> propValue.parseSimpleText().let { SgfProperty.PC(it) }
            "PW" -> propValue.parseSimpleText().let { SgfProperty.PW(it) }
            "RE" -> propValue.parseSimpleText().let { SgfProperty.RE(it) }
            "RO" -> propValue.parseSimpleText().let { SgfProperty.RO(it) }
            "RU" -> propValue.parseSimpleText().let { SgfProperty.RU(it) }
            "SO" -> propValue.parseSimpleText().let { SgfProperty.SO(it) }
            "TM" -> propValue.parseReal()?.let { SgfProperty.TM(it) }
            "US" -> propValue.parseSimpleText().let { SgfProperty.US(it) }
            "WR" -> propValue.parseSimpleText().let { SgfProperty.WR(it) }
            "WT" -> propValue.parseSimpleText().let { SgfProperty.WT(it) }
            "BL" -> propValue.parseReal()?.let { SgfProperty.BL(it) }
            "OB" -> propValue.parseNumber()?.let { SgfProperty.OB(it) }
            "OW" -> propValue.parseNumber()?.let { SgfProperty.OW(it) }
            "WL" -> propValue.parseReal()?.let { SgfProperty.WL(it) }
            "FG" -> propValue.parseCompose(String::parseNumber, String::parseSimpleText)?.let { SgfProperty.FG(it) }
            "PM" -> propValue.parseNumber()?.let { SgfProperty.PM(it) }
            "VW" -> (property as? SgfProperty.VW ?: SgfProperty.VW())
                .apply { value.content.addAll(propValue.parseList()) }
            "HA" -> propValue.parseNumber()?.let { SgfProperty.HA(it) }
            "KM" -> propValue.parseReal()?.let { SgfProperty.KM(it) }
            "TB" -> (property as? SgfProperty.TB ?: SgfProperty.TB())
                .apply { value.content.addAll(propValue.parseList()) }
            "TW" -> (property as? SgfProperty.TW ?: SgfProperty.TW())
                .apply { value.content.addAll(propValue.parseList()) }
            else -> null
        }
    }
}

// a Coordinate consists of two characters, denoting column and row
private inline fun <reified T : SgfType.Coordinate> String.parseCoordinate(): T? =
    getOrNull(0)?.getInt()?.let { y -> // we reverse the order
        getOrNull(1)?.getInt()?.let { x ->
            SgfType.Coordinate(x to y).get()
        }
    }

private fun String.parsePoint() = parseCoordinate<SgfType.Point>()

// order is 'a'..'z','A'..'Z'
private fun Char.getInt(): Int? = when (this) {
    in 'a'..'z' -> ('a'..'z').indexOf(this) + 1
    in 'A'..'Z' -> ('A'..'Z').indexOf(this) + ('a'..'z').count()
    else -> null
}

private fun String.parseDouble(): SgfType.Double? =
    when (this) {
        "1" -> SgfType.Double(DoubleValue.GOOD)
        "2" -> SgfType.Double(DoubleValue.VERY_GOOD)
        else -> null
    }

private fun String.parseColor(): SgfType.Color? =
    when (this) {
        "B" -> SgfType.Color(ColorValue.BLACK)
        "W" -> SgfType.Color(ColorValue.WHITE)
        else -> null
    }

private fun String.parseNumber(): SgfType.Number? = toIntOrNull()?.let { SgfType.Number(it) }
private fun String.parseReal(): SgfType.Real? = toFloatOrNull()?.let { SgfType.Real(it) }

private fun String.parseText(): SgfType.Text = // whitespace except '\n' gets converted to space
    SgfType.Text(replace(Regex("\\s&&[^\n]"), " "))
private fun String.parseSimpleText(): SgfType.SimpleText = // all whitespace gets converted to space
    SgfType.SimpleText(replace(Regex("\\s"), " "))

private inline fun <reified T : SgfType.Coordinate> String.parseList(): List<T> =
    if (contains(':')) { // `Compose` type
        val (fst, snd) = split(':').map { it.parseCoordinate() as? T }
        fst?.let { snd?.let { (fst..snd) }} ?: emptyList()
    } else {
        (parseCoordinate() as? T)?.let { listOf(it) } ?: emptyList()
    }

private fun <S : SgfType<*>, T : SgfType<*>> String.parseCompose(
    parseFst: String.() -> S?,
    parseSnd: String.() -> T?
): SgfType.Compose<S, T>? =
    if (contains(':')) {
        val (fst, snd) = split(':')
        fst.parseFst()?.let { s -> snd.parseSnd()?.let { t -> SgfType.Compose(s to t) } }
    } else {
        null
    }
