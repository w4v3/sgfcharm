/*
 *    Copyright [2020] [w4v3]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package onion.w4v3xrmknycexlsd.lib.sgfcharm.parse

import onion.w4v3xrmknycexlsd.lib.sgfcharm.Status
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType.*
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfProperty.*
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType.Number
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType.Double
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType.Color
import kotlin.collections.List as KList

// NOTE: Number, Double, Color in this file are data types defined here
// Kotlin's List is imported as KList
// everything in this file is public; the modifier is left out for readability

/**
 * Parser for reading `sgf` strings into [SgfCollection]s.
 *
 * It is very lenient, in that it will not throw errors for incorrect input, but it will try to
 * parse it as good as it can. Of course, that means that for incorrect input, the results might be
 * completely wrong (it will not try to correct anything).
 *
 * Note that the parser returns all `GameTree`s represented in the string, i.e., it returns a
 * collection. This is only to make the parser more reusable; the other components ignore all but the
 * first tree in the collection.
 *
 * Only the current `sgf` version `FF[4]` is supported, and only coordinate parsing for Go
 * is implemented. Unknown properties will be included in the output as [CUSTOM] types.
 * Thus, if you need to handle old file formats or game-specific properties for games other than Go,
 * you can run the parser as it is and handle the [CUSTOM] properties manually later on.
 *
 * Most of the parser is private as it simply implements the FF[4] specification. However, since
 * the parsing of [Point], [Move] and [Stone] is game-dependent, there is
 * a possibility of implementing the [SgfParser.CoordinateParser] interface and setting the
 * [coordinateParser] property to the parser to be used.
 *
 * @property[coordinateParser] the [SgfParser.CoordinateParser] to be used for game-specific parsing;
 * default is [GoCoordinateParser]
 *
 * @constructor initializes the parser with the given [SgfParser.CoordinateParser]
 */
@Status.Api
class SgfParser(public var coordinateParser: CoordinateParser<*> = GoCoordinateParser) {
    private enum class ParseState {
        EXPECT_ANYTHING,
        PARSE_PROPIDENT,
        PARSE_PROPVALUE,
        ESCAPE,
    }

    private var state: ParseState = ParseState.EXPECT_ANYTHING

    /**
     * The class to extend for parsing of the property values for the game-specific
     * [Point], [Move] and [Stone] types.
     *
     * For a specific game, you need to implement [parsePoint], [parseMove] and [parseStone], as
     * well as [rangeTo] for the `Point` type to allow parsing compressed point lists. In addition,
     * if the `Stone` and the `Point` type for the game are the same, you need to override the
     * [pointToStone] property to specify how a point should be converted to a stone for allowing
     * compressed lists for the `Stone` type as well.
     *
     * @param[T] the type of [Point]
     * @property[pointToStone] Casts the given [Point] to the [Stone] type, or `null` if that's impossible.
     * This should only be overriden by games in which [Point] and [Stone] are the same.
     * In this case, it should cast any castable [Point] to the equivalent [Stone] and everything else
     * to `null`. Typically, this is just the constructor of the [Stone] type you are using.
     * The only purpose of this function is to enable compressed point lists for stones.
     */
    @Status.Beta
    public abstract class CoordinateParser<T : Point>(open val pointToStone: (T) -> Stone? = { _ -> null }) {
        /** Parses the [from] string representing an `sgf Point` into an [Point] object. */
        @Status.Beta
        public abstract fun parsePoint(from: String): T?

        /** Parses the [from] string representing an `sgf Stone` into an [Stone] object. */
        @Status.Beta
        public abstract fun parseStone(from: String): Stone?

        /**
         * Parses the [from] string representing an `sgf Move` into an [Move] object.
         *
         * Should return `null` if the move is a pass or input is invalid (which will be treated as pass).
         */
        @Status.Beta
        public abstract fun parseMove(from: String): Move

        /**
         * Returns a list of [Point]s contained in a rectangle between [this] upper left
         * and the [other] lower right [Point]. Returns an empty list if that rectangle is
         * empty, including the case where the other point lies to the left or above this point.
         */
        @Status.Beta
        public abstract operator fun T.rangeTo(other: T): KList<T>

        /**
         * Parses this string into a list of [Point]s.
         *
         * The string represents a single property value, i.e. [...]. In case this contains a ':',
         * a list containing all the [Point]s in the compressed list must be returned,
         * otherwise a list with a single element. For invalid input, an empty list should be returned.
         *
         * The existing implementation of this should be enough for most purposes; it requires only
         * the implementation of [rangeTo].
         */
        @Status.Util
        public open fun parsePointList(from: String): KList<T> =
            if (from.contains(':')) { // compressed list
                val (fst, snd) = from.split(':').map { parsePoint(it) }
                fst?.let { snd?.let { (fst..snd) } } ?: emptyList()
            } else {
                (parsePoint(from))?.let { listOf(it) } ?: emptyList()
            }

        /**
         * Parses this string into a list of [Stone]s.
         *
         * The string represents a single property value, i.e. [...]. In case this contains a ':',
         * a list containing all the [Stone]s in the compressed list must be returned,
         * otherwise a list with a single element. Note that a compressed list might only be given
         * if the [Stone] type is the same as [Point] for that game.
         * For invalid input, an empty list should be returned.
         *
         * The existing implementation of this should be enough for most purposes; it requires only
         * the implementation of [rangeTo] (or [parsePointList]) and [pointToStone].
         */
        @Status.Util
        public open fun parseStoneList(from: String): KList<Stone> =
            if (from.contains(':')) { // compressed list
                val (fst, snd) = from.split(':').map { parsePoint(it) }
                fst?.let { snd?.let { (fst..snd).mapNotNull { pointToStone(it) } } } ?: emptyList()
            } else {
                (parseStone(from))?.let { listOf(it) } ?: emptyList()
            }
    }

    /** Parses the [sgfString] representing an `sgf GameTree` into an [SgfCollection] object. */
    @Status.Api
    public fun parseSgfCollection(sgfString: String): SgfCollection {
        var currentTree = SgfTree()

        // we parse character by character, state dependent
        // maintaining property identifier and value strings parsed so far
        // as well as the last property used (for those properties accepting multiple values)
        var propIdentString = ""
        var propValueString = ""
        var currentProp: SgfProperty<SgfType>? = null

        for (c in sgfString.replace(
            Regex("\r\n?|\n?\r"),
            "\n"
        )) { // all newlines are treated the same
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

        // we return all children despite only using the first,
        // and turn them into root trees
        return currentTree.children.map { it.apply { parent = null } }
    }

    // makes a new [SgfProperty] from the [propIdent] identifier and the [propValue] value strings
    // if [property] exists already, in case of a basic data type it is replaced (i.e., old values
    // are ignored); if it is a list type, the parsed result is appended to the current one
    // reflection would lose type safety here, as well as using generics due to the lack of support
    // of union like data types, so we go for the manual approach
    private fun parsePropValue(
        propIdent: String,
        propValue: String,
        property: SgfProperty<SgfType>?
    ): SgfProperty<SgfType>? {
        return when (propIdent) {
            "B" -> B((coordinateParser.parseMove(propValue)))
            "KO" -> KO
            "MN" -> propValue.parseNumber()?.let { MN(it) }
            "W" -> W((coordinateParser.parseMove(propValue)))
            "AB" -> (property as? AB ?: AB())
                .apply { value.elements.addAll(coordinateParser.parseStoneList(propValue)) }
            "AE" -> (property as? AE ?: AE())
                .apply { value.elements.addAll(coordinateParser.parsePointList(propValue)) }
            "AW" -> (property as? AW ?: AW())
                .apply { value.elements.addAll(coordinateParser.parseStoneList(propValue)) }
            "PL" -> propValue.parseColor()?.let { PL(it) }
            "C" -> C(propValue.parseText())
            "DM" -> propValue.parseDouble()?.let { DM(it) }
            "GB" -> propValue.parseDouble()?.let { GB(it) }
            "GW" -> propValue.parseDouble()?.let { GW(it) }
            "HO" -> propValue.parseDouble()?.let { HO(it) }
            "N" -> N(propValue.parseSimpleText())
            "UC" -> propValue.parseDouble()?.let { UC(it) }
            "V" -> propValue.parseReal()?.let { V(it) }
            "BM" -> propValue.parseDouble()?.let { BM(it) }
            "DO" -> DO
            "IT" -> IT
            "TE" -> propValue.parseDouble()?.let { TE(it) }
            "AR" -> (property as? AR ?: AR())
                .apply {
                    propValue.parseCompose(
                        coordinateParser::parsePoint,
                        coordinateParser::parsePoint
                    )?.let { value.elements.add(it) }
                }
            "CR" -> (property as? CR ?: CR())
                .apply { value.elements.addAll(coordinateParser.parsePointList(propValue)) }
            "DD" -> (property as? DD ?: DD())
                .apply { value.elements.addAll(coordinateParser.parsePointList(propValue)) }
            "LB" -> (property as? LB ?: LB())
                .apply {
                    propValue.parseCompose(coordinateParser::parsePoint, String::parseSimpleText)
                        ?.let { value.elements.add(it) }
                }
            "LN" -> (property as? LN ?: LN())
                .apply {
                    propValue.parseCompose(
                        coordinateParser::parsePoint,
                        coordinateParser::parsePoint
                    )?.let { value.elements.add(it) }
                }
            "MA" -> (property as? MA ?: MA())
                .apply { value.elements.addAll(coordinateParser.parsePointList(propValue)) }
            "SL" -> (property as? SL ?: SL())
                .apply { value.elements.addAll(coordinateParser.parsePointList(propValue)) }
            "SQ" -> (property as? SQ ?: SQ())
                .apply { value.elements.addAll(coordinateParser.parsePointList(propValue)) }
            "TR" -> (property as? TR ?: TR())
                .apply { value.elements.addAll(coordinateParser.parsePointList(propValue)) }
            "AP" -> propValue.parseCompose(String::parseSimpleText, String::parseSimpleText)
                ?.let { AP(it) }
            "CA" -> CA(propValue.parseSimpleText())
            "FF" -> propValue.parseNumber()?.let { FF(it) }
            "GM" -> propValue.parseNumber()?.let { GM(it) }
            "ST" -> propValue.parseNumber()?.let { ST(it) }
            "SZ" -> propValue.parseNumberOrComposeNumber()?.let { SZ(it) }
            "AN" -> AN(propValue.parseSimpleText())
            "BR" -> BR(propValue.parseSimpleText())
            "BT" -> BT(propValue.parseSimpleText())
            "CP" -> CP(propValue.parseSimpleText())
            "DT" -> DT(propValue.parseSimpleText())
            "EV" -> EV(propValue.parseSimpleText())
            "GN" -> GN(propValue.parseSimpleText())
            "GC" -> GC(propValue.parseText())
            "ON" -> ON(propValue.parseSimpleText())
            "OT" -> OT(propValue.parseSimpleText())
            "PB" -> PB(propValue.parseSimpleText())
            "PC" -> PC(propValue.parseSimpleText())
            "PW" -> PW(propValue.parseSimpleText())
            "RE" -> RE(propValue.parseSimpleText())
            "RO" -> RO(propValue.parseSimpleText())
            "RU" -> RU(propValue.parseSimpleText())
            "SO" -> SO(propValue.parseSimpleText())
            "TM" -> propValue.parseReal()?.let { TM(it) }
            "US" -> US(propValue.parseSimpleText())
            "WR" -> WR(propValue.parseSimpleText())
            "WT" -> WT(propValue.parseSimpleText())
            "BL" -> propValue.parseReal()?.let { BL(it) }
            "OB" -> propValue.parseNumber()?.let { OB(it) }
            "OW" -> propValue.parseNumber()?.let { OW(it) }
            "WL" -> propValue.parseReal()?.let { WL(it) }
            "FG" -> propValue.parseCompose(String::parseNumber, String::parseSimpleText)
                ?.let { FG(it) }
            "PM" -> propValue.parseNumber()?.let { PM(it) }
            "VW" -> (property as? VW ?: VW())
                .apply { value.elements.addAll(coordinateParser.parsePointList(propValue)) }
            "HA" -> propValue.parseNumber()?.let { HA(it) }
            "KM" -> propValue.parseReal()?.let { KM(it) }
            "TB" -> (property as? TB ?: TB())
                .apply { value.elements.addAll(coordinateParser.parsePointList(propValue)) }
            "TW" -> (property as? TW ?: TW())
                .apply { value.elements.addAll(coordinateParser.parsePointList(propValue)) }
            else -> CUSTOM( // unknown property, just return a CUSTOM one with identifier and value as text
                Compose(
                    SimpleText(propIdent),
                    Text(propValue)
                )
            )
        }
    }
}

/** Parses this string representing an [Double] property value. */
@Status.Util
public fun String.parseDouble(): Double? =
    when (this) {
        "1" -> Double(Double.Value.MUCH)
        "2" -> Double(Double.Value.VERY_MUCH)
        else -> null
    }

/** Parses this string representing an [Color] property value. */
@Status.Util
public fun String.parseColor(): Color? =
    when (this) {
        "B" -> Color(Color.Value.BLACK)
        "W" -> Color(Color.Value.WHITE)
        else -> null
    }

/** Parses this string representing an [Number] property value. */
@Status.Util
public fun String.parseNumber(): Number? = toIntOrNull()?.let { Number(it) }

/** Parses this string representing an [Real] property value. */
@Status.Util
public fun String.parseReal(): Real? = toFloatOrNull()?.let { Real(it) }

/** Parses this string representing an [Text] property value. */
@Status.Util
public fun String.parseText(): Text = // whitespace except '\n' gets converted to space
    Text(replace(Regex("\\s&&[^\n]"), " "))

/** Parses this string representing an [SimpleText] property value. */
@Status.Util
public fun String.parseSimpleText(): SimpleText = // all whitespace gets converted to space
    SimpleText(replace(Regex("\\s"), " "))

/**
 * Parses this string representing an [Compose] property value.
 *
 * The first component is parsed using the [parseFst] function, the second one using [parseSnd].
 */
@Status.Util
public fun <S : SgfType, T : SgfType> String.parseCompose(
    parseFst: String.() -> S?,
    parseSnd: String.() -> T?
): Compose<S, T>? =
    if (contains(':')) {
        val (fst, snd) = split(':')
        fst.parseFst()?.let { s -> snd.parseSnd()?.let { t -> Compose(s, t) } }
    } else {
        null
    }

/**
 * Parses this string given either as a single `sgf Number` or a composed `Number : Number`.
 *
 * In the former case, the returned object is [Compose] with both components identical to the
 * `Number`. This is for parsing the `SZ` property which determines the board size. As it might be
 * subject to change, I do not recommend using it directly.
 */
@Status.Impl
public fun String.parseNumberOrComposeNumber(): Compose<Number, Number>? =
    if (contains(':'))
        parseCompose(String::parseNumber, String::parseNumber)
    else
        parseNumber()?.let { Compose(it, it) }
