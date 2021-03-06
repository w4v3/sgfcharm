/*
 *    Copyright 2020 w4v3
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
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType.Number
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType.List
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType.Double
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType.Color
import kotlin.collections.List as KList

// NOTE: Number, List, Double, Color in this file are data types defined here
// Kotlin's List is imported as KList
// everything in this file is public; the modifier is left out for readability

/** Representation of the `SGF Collection`, which is a list of `GameTree`s. */
typealias SgfCollection = KList<SgfTree>

/**
 * Representation of the `SGF GameTree`, which consists of an `Sequence`, and zero or more children `GameTree`s.
 *
 * @property[nodes] the nodes making up the `SGF Sequence` of the tree
 * @property[children] the child `GameTree`s
 * @property[parent] a reference to its parent tree for backtracking.
 */
@Status.Api
data class SgfTree(
    var parent: SgfTree? = null,
    val nodes: SgfSequence = mutableListOf(),
    val children: MutableList<SgfTree> = mutableListOf()
)

/** Representation of the `SGF Sequence`, which is a list of `Node`s. */
typealias SgfSequence = MutableList<SgfNode>
/** Representation of the `SGF Node`, which is a list of `Property`s. */
typealias SgfNode = MutableList<SgfProperty<SgfType>>

/**
 * Base class for all `SGF Property`s, with a subclass for each particular property.
 *
 * The documentation for each class includes just the first sentence of the `SGF` documentation.
 * For more information, refer to the official [SGF documentation](https://www.red-bean.com/SGF/go.html).
 *
 * @property[value] the content of the property value, typed
 * */
sealed class SgfProperty<out T : SgfType> {
    abstract val value: T

    /** Execute a black move. */
    data class B(override val value: Move) : SgfProperty<Move>()

    /** Execute a given move, even if it's illegal. */
    object KO : SgfProperty<None>() {
        override val value: None = None
    }

    /** Sets move number to the given value. */
    data class MN(override val value: Number) : SgfProperty<Number>()

    /** Execute a white move. */
    data class W(override val value: Move) : SgfProperty<Move>()

    /** Add black stones to the board. */
    data class AB(override val value: List<Stone> = List(mutableListOf())) :
        SgfProperty<List<Stone>>()

    /** Clear the given points on the board. */
    data class AE(override val value: List<Point> = List(mutableListOf())) :
        SgfProperty<List<Point>>()

    /** Add white stones to the board. */
    data class AW(override val value: List<Stone> = List(mutableListOf())) :
        SgfProperty<List<Stone>>()

    /** Tells whose turn it is to play. */
    data class PL(override val value: Color) : SgfProperty<Color>()

    /** Provides a comment text for the given node. */
    data class C(override val value: Text) : SgfProperty<Text>()

    /** The position is even. */
    data class DM(override val value: Double) : SgfProperty<Double>()

    /** Something good for black. */
    data class GB(override val value: Double) : SgfProperty<Double>()

    /** Something good for white. */
    data class GW(override val value: Double) : SgfProperty<Double>()

    /** Node is a 'hotspot', i.e. something interesting. */
    data class HO(override val value: Double) : SgfProperty<Double>()

    /** Provides a name for the node. */
    data class N(override val value: SimpleText) : SgfProperty<SimpleText>()

    /** The position is unclear. */
    data class UC(override val value: Double) : SgfProperty<Double>()

    /** Define a value for the node. */
    data class V(override val value: Real) : SgfProperty<Real>()

    /** The played move is bad */
    data class BM(override val value: Double) : SgfProperty<Double>()

    /** The played move is doubtful */
    object DO : SgfProperty<None>() {
        override val value: None = None
    }

    /** The played move is interesting */
    object IT : SgfProperty<None>() {
        override val value: None = None
    }

    /** The played move is a tesuji (good move). */
    data class TE(override val value: Double) : SgfProperty<Double>()

    /** Draw an arrow pointing FROM the first point TO the second point. */
    data class AR(override val value: List<Compose<Point, Point>> = List(mutableListOf())) :
        SgfProperty<List<Compose<Point, Point>>>()

    /** Marks the given points with a circle. */
    data class CR(override val value: List<Point> = List(mutableListOf())) :
        SgfProperty<List<Point>>()

    /** Dim (grey out) the given points. */
    data class DD(override val value: List<Point> = List(mutableListOf())) :
        SgfProperty<List<Point>>()

    /** Writes the given text on the board at the point. */
    data class LB(override val value: List<Compose<Point, SimpleText>> = List(mutableListOf())) :
        SgfProperty<List<Compose<Point, SimpleText>>>()

    /** Applications should draw a simple line form one point to the other. */
    data class LN(override val value: List<Compose<Point, Point>> = List(mutableListOf())) :
        SgfProperty<List<Compose<Point, Point>>>()

    /** Marks the given points with an 'X'. */
    data class MA(override val value: List<Point> = List(mutableListOf())) :
        SgfProperty<List<Point>>()

    /** Selected points. Type of markup unknown */
    data class SL(override val value: List<Point> = List(mutableListOf())) :
        SgfProperty<List<Point>>()

    /** Marks the given points with a square. */
    data class SQ(override val value: List<Point> = List(mutableListOf())) :
        SgfProperty<List<Point>>()

    /** Marks the given points with a triangle. */
    data class TR(override val value: List<Point> = List(mutableListOf())) :
        SgfProperty<List<Point>>()

    /** Provides the name and version number of the application used to create this gametree. */
    data class AP(override val value: Compose<SimpleText, SimpleText>) :
        SgfProperty<Compose<SimpleText, SimpleText>>()

    /** Provides the used charset for SimpleText and Text type. */
    data class CA(override val value: SimpleText) : SgfProperty<SimpleText>()

    /** Defines the used file format. */
    data class FF(override val value: Number) : SgfProperty<Number>()

    /** Defines the type of game. */
    data class GM(override val value: Number) : SgfProperty<Number>()

    /** Defines how variations should be shown. */
    data class ST(override val value: Number) : SgfProperty<Number>()

    /** Defines the size of the board. */
    data class SZ(override val value: Compose<Number, Number>) :
        SgfProperty<Compose<Number, Number>>()

    /** Provides the name of the person who made the annotations. */
    data class AN(override val value: SimpleText) : SgfProperty<SimpleText>()

    /** Provides the rank of the black player. */
    data class BR(override val value: SimpleText) : SgfProperty<SimpleText>()

    /** Provides the name of the black team. */
    data class BT(override val value: SimpleText) : SgfProperty<SimpleText>()

    /** Any copyright information (e.g. for the annotations) should be included here. */
    data class CP(override val value: SimpleText) : SgfProperty<SimpleText>()

    /** Provides the date when the game was played. */
    data class DT(override val value: SimpleText) : SgfProperty<SimpleText>()

    /** Provides the name of the event (e.g. tournament). */
    data class EV(override val value: SimpleText) : SgfProperty<SimpleText>()

    /** Provides a name for the game. The name is used to */
    data class GN(override val value: SimpleText) : SgfProperty<SimpleText>()

    /** Provides some extra information about the following game. */
    data class GC(override val value: Text) : SgfProperty<Text>()

    /** Provides some information about the opening played. */
    data class ON(override val value: SimpleText) : SgfProperty<SimpleText>()

    /** Describes the method used for overtime (byo-yomi). */
    data class OT(override val value: SimpleText) : SgfProperty<SimpleText>()

    /** Provides the name of the black player. */
    data class PB(override val value: SimpleText) : SgfProperty<SimpleText>()

    /** Provides the place where the game was played. */
    data class PC(override val value: SimpleText) : SgfProperty<SimpleText>()

    /** Provides the name of the white player. */
    data class PW(override val value: SimpleText) : SgfProperty<SimpleText>()

    /** Provides the result of the game. */
    data class RE(override val value: SimpleText) : SgfProperty<SimpleText>()

    /** Provides round-number and type of round. */
    data class RO(override val value: SimpleText) : SgfProperty<SimpleText>()

    /** Provides the used rules for this game. */
    data class RU(override val value: SimpleText) : SgfProperty<SimpleText>()

    /** Provides the name of the source (e.g. book, journal, ...). */
    data class SO(override val value: SimpleText) : SgfProperty<SimpleText>()

    /** Provides the time limits of the game. */
    data class TM(override val value: Real) : SgfProperty<Real>()

    /** Provides the name of the user (or program) who entered the game. */
    data class US(override val value: SimpleText) : SgfProperty<SimpleText>()

    /** Provides the rank of the white player. */
    data class WR(override val value: SimpleText) : SgfProperty<SimpleText>()

    /** Provide the name of the white team. */
    data class WT(override val value: SimpleText) : SgfProperty<SimpleText>()

    /** Time left for black, after the move was made. */
    data class BL(override val value: Real) : SgfProperty<Real>()

    /** Number of black moves left to play in this byo-yomi period. */
    data class OB(override val value: Number) : SgfProperty<Number>()

    /** Number of white moves left to play in this byo-yomi period. */
    data class OW(override val value: Number) : SgfProperty<Number>()

    /** Time left for white after the move was made. */
    data class WL(override val value: Real) : SgfProperty<Real>()

    /** The figure property is used to divide a game into different figures for printing. */
    data class FG(override val value: Compose<Number, SimpleText>) :
        SgfProperty<Compose<Number, SimpleText>>()

    /** This property is used for printing. */
    data class PM(override val value: Number) : SgfProperty<Number>()

    /** View only given points of the board. */
    data class VW(override val value: List<Point> = List(mutableListOf())) :
        SgfProperty<List<Point>>()

    /** Defines the Number of handicap stones (>=2). */
    data class HA(override val value: Number) : SgfProperty<Number>()

    /** Defines the komi. */
    data class KM(override val value: Real) : SgfProperty<Real>()

    /** Specifies the black territory or area. */
    data class TB(override val value: List<Point> = List(mutableListOf())) :
        SgfProperty<List<Point>>()

    /** Specifies the white territory or area. */
    data class TW(override val value: List<Point> = List(mutableListOf())) :
        SgfProperty<List<Point>>()

    /**
     * This class is used for custom properties. It is composed of the identifier and the
     * content of the value, both as strings inside [SimpleText] and [Text] objects, respectively.
     */
    data class CUSTOM(override val value: Compose<SimpleText, Text>) :
        SgfProperty<Compose<SimpleText, Text>>()
}

/**
 * This is the base class for all `SGF` data types, for strong typing.
 *
 * Unfortunately, neither `inline sealed class` nor union data types are supported by Kotlin, yet,
 * so these wrapper classes might lead to performance and memory issues for large `GameTree`s.
 */
sealed class SgfType {

    /** Supertype for `SGF Point` types. Abstract, as these are game-specific. */
    abstract class Point : SgfType()

    /**
     *  Supertype for `SGF Move` types. Game-specific, but might contain a target [Point].
     *
     * @property[point] the target [Point] of the move, if any
     */
    abstract class Move(open val point: Point?) : SgfType()

    /**
     * Supertype for `SGF Stone` types. Game-specific, but always contains a [Point].
     *
     * @property[point] the [Point] where to place the stone
     */
    abstract class Stone(open val point: Point) : SgfType()

    /**
     * Emphasis type in `SGF`.
     *
     * @property[value] the [Double.Value] value contained
     */
    data class Double(val value: Double.Value) : SgfType() {
        /** Possible values of the [Double] emphasis type. */
        enum class Value {
            /** emphasis */
            MUCH,

            /** strong emphasis */
            VERY_MUCH
        }
    }

    /**
     * Color type in `SGF`.
     *
     * @property[value] the [Color.Value] value contained
     */
    data class Color(val value: Color.Value) : SgfType() {
        /**Possible values of the [Color] type. */
        enum class Value {
            /** black color */
            BLACK,

            /** white color */
            WHITE;
        }
    }

    /**
     * `Number` type in `SGF` holding integer values.
     *
     * @property[number] the integer value contained
     */
    data class Number(val number: Int) : SgfType()

    /**
     * `Real` type in `SGF` holding floating point values.
     *
     * @property[number] the floating point value contained
     */
    data class Real(val number: Float) : SgfType()

    /**
     * `Text` type in `SGF` holding arbitrary text.
     *
     * @property[text] the text contained
     */
    data class Text(val text: String) : SgfType()

    /**
     * `SimpleText` type in `SGF` holding text without newlines.
     *
     * @property[text] the text contained
     */
    data class SimpleText(val text: String) : SgfType()

    /** `None` data type in `SGF`. */
    object None : SgfType()

    /**
     * `List` and `Elist` data types in `SGF`, lists of [SgfType].
     *
     * @property[elements] the list contained
     * */
    data class List<T : SgfType>(val elements: MutableList<T>) : SgfType(),
        MutableIterable<T> by elements

    /**
     * `Compose` data type in `SGF`, a pair of [SgfType].
     *
     * @property[first] the first component
     * @property[second] the second component
     */
    data class Compose<S : SgfType, T : SgfType>(val first: S, val second: T) : SgfType()
}

/** Switches this [Color.Value] from black to white and vice versa. */
@Status.Util
public operator fun Color.Value.not(): Color.Value = when (this) {
    Color.Value.WHITE -> Color.Value.BLACK
    Color.Value.BLACK -> Color.Value.WHITE
}
