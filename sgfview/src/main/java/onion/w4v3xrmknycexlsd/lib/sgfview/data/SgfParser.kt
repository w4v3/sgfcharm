package onion.w4v3xrmknycexlsd.lib.sgfview.data

import kotlin.reflect.KClass

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
    fun parseSgfTree(sgfString: String): SgfTree {
        var currentTree = SgfTree()

        var propIdentString = ""
        var propValueString = ""
        var currentProp: SgfProperty<SgfType>? = null

        for (c in sgfString) {
            when (state) {
                ParseState.EXPECT_ANYTHING -> when (c) {
                    '(' -> {
                        val newTree = SgfTree()
                        newTree.parent = currentTree
                        currentTree.children.add(newTree)
                        currentTree = newTree
                    }
                    ')' -> currentTree.parent?.let { currentTree = it }
                    ';' -> currentTree.nodes.add(mutableListOf())
                    in 'A'..'Z' -> propIdentString = "$c".also { state = ParseState.PARSE_PROPIDENT }
                    '[' -> propValueString = "".also { state = ParseState.PARSE_PROPVALUE }
                }
                ParseState.PARSE_PROPIDENT -> when (c) {
                    in 'A'..'Z' -> propIdentString.plus(c)
                    '[' -> {
                        currentProp = getProperty(propIdentString)
                        state = ParseState.PARSE_PROPVALUE
                    }
                }
                ParseState.PARSE_PROPVALUE -> when (c) {
                    '\\' -> state = ParseState.ESCAPE
                    ']' -> {
                        parsePropValue(currentProp, propValueString)?.let {
                            currentTree.nodes.lastOrNull()?.add(it)
                        }
                        state = ParseState.EXPECT_ANYTHING
                    }
                    else -> propValueString.plus(c)
                }
                ParseState.ESCAPE -> {
                    if (c != '\n') propValueString.plus(c)
                    state = ParseState.PARSE_PROPVALUE
                }
            }
        }

        return currentTree.children[0]
    }

    // returns the [SgfProperty] from the identifier, or UNKNOWN if not found
    private fun getProperty(propIdentString: String): SgfProperty<SgfType> =
        when (propIdentString) {
            "B" -> SgfProperty.B()
            "W" -> SgfProperty.W()
            "KO" -> SgfProperty.KO()
            "MN" -> SgfProperty.MN()
            "AB" -> SgfProperty.AB()
            "AW" -> SgfProperty.AW()
            "AE" -> SgfProperty.AE()
            "PL" -> SgfProperty.PL()
            "C" -> SgfProperty.C()
            "DM" -> SgfProperty.DM()
            "GB" -> SgfProperty.GB()
            "GW" -> SgfProperty.GW()
            "HO" -> SgfProperty.HO()
            "N" -> SgfProperty.N()
            "UC" -> SgfProperty.UC()
            "V" -> SgfProperty.V()
            "BM" -> SgfProperty.BM()
            "DO" -> SgfProperty.DO()
            "IT" -> SgfProperty.IT()
            "TE" -> SgfProperty.TE()
            else -> SgfProperty.UNKNOWN()
        }

    private fun parsePropValue(property: SgfProperty<SgfType>?, propValue: String): SgfProperty<SgfType>? {
        return property?.apply {
            vlu = when (property.vlu) {
                is SgfType.Double -> propValue.parseDouble()
                is SgfType.Player -> propValue.parseColor()
                is SgfType.Number -> propValue.parseNumber()
                is SgfType.Real -> propValue.parseReal()
                is SgfType.SimpleText -> propValue.parseSimpleText()
                is SgfType.Text -> propValue.parseText()
                is SgfType.None -> vlu
            }
        }
    }

    private fun String.parseDouble(): SgfType.Double? =
        when (this) {
            "1" -> SgfType.Double(DoubleValue.GOOD)
            "2" -> SgfType.Double(DoubleValue.VERY_GOOD)
            else -> null
        }

    private fun String.parseColor(): SgfType.Player? =
        when (this) {
            "B" -> SgfType.Player(PlayerValue.PLAYER_BLACK)
            "W" -> SgfType.Player(PlayerValue.PLAYER_WHITE)
            else -> null
        }

    private fun String.parseNumber(): SgfType.Number? = toIntOrNull()?.let { SgfType.Number(it) }
    private fun String.parseReal(): SgfType.Real? = toFloatOrNull()?.let { SgfType.Real(it) }

    private fun String.parseText(): SgfType.Text = SgfType.Text(replace(Regex("\\s&&[^\n]"), " "))
    private fun String.parseSimpleText(): SgfType.SimpleText = SgfType.SimpleText(replace(Regex("\\s"), " "))

}