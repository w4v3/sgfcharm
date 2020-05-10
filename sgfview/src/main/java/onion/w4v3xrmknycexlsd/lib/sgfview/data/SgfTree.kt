package onion.w4v3xrmknycexlsd.lib.sgfview.data

/** An [SgfTree] consists of a `Sequence`, which is a list of [SgfNode]s and zero or more [SgfTree]s.
 * It also holds a reference [parent] to its parent tree for backtracking. */
data class SgfTree(
    var parent: SgfTree? = null,
    val nodes: SgfSequence = mutableListOf(),
    val children: MutableList<SgfTree> = mutableListOf()
)

typealias SgfSequence = MutableList<SgfNode>
typealias SgfNode = MutableList<SgfProperty<SgfType<*>>>

/** Possible values for the SGF `Double` data type. */
enum class DoubleValue { GOOD, VERY_GOOD }
/** Possible values for the SGF `Color` data type. */
enum class ColorValue { BLACK, WHITE }
operator fun ColorValue.not() = if (this == ColorValue.BLACK) ColorValue.WHITE else ColorValue.BLACK

/** For strong typing, this class is the base class for all SGF data types.
 * Unfortunately, neither `inline sealed class` nor union data types are supported by Kotlin, yet.
 * This is a performance bottleneck ... */
sealed class SgfType<out T>(open val content: T) {
    /** Base class for [Move], [Point] and [Stone], including explicit downcasting. */
    open class Coordinate(override val content: Pair<Int, Int>) : SgfType<Pair<Int, Int>>(content) {
        inline fun <reified T : Coordinate> get() : T =
            when (T::class) {
                Move::class -> Move(content) as T
                Point::class -> Point(content) as T
                Stone::class -> Stone(content) as T
                else -> Coordinate(content) as T // should never happen
            }
    }
    /** A move to (x,y), where x is the row and y the column, starting at 1.
     * Note that this is the other way around as in the SGF notation. */
    data class Move(override val content: Pair<Int, Int>) : Coordinate(content)
    /** For Go, [Move] and [Point] are the same. */
    data class Point(override val content: Pair<Int, Int>) : Coordinate(content)
    /** For Go, [Move] and [Stone] are the same. */
    data class Stone(override val content: Pair<Int, Int>) : Coordinate(content)
    /** Emphasis type */
    data class Double(override val content: DoubleValue) : SgfType<DoubleValue>(content)
    /** A color to play. */
    data class Color(override val content: ColorValue) : SgfType<ColorValue>(content)
    /** SGF `Number` type holding integer override values. */
    data class Number(override val content: Int) : SgfType<Int>(content)
    /** SGF `Real` type holding floating point override values. */
    data class Real(override val content: Float) : SgfType<Float>(content)
    /** SGF `Text` type holding arbitrary text. */
    data class Text(override val content: String) : SgfType<String>(content)
    /** SGF `SimpleText` type holding text without newlines. */
    data class SimpleText(override val content: String) : SgfType<String>(content)
    /** SGF `None` data type (Singleton). */
    object None : SgfType<Unit>(Unit)
    /** SGF `List` and `Elist` data types, lists of [SgfType]. */
    data class List<T : SgfType<*>>(override val content: MutableList<T>) : SgfType<MutableList<T>>(content)
    /** SGF `Compose` data type, a pair of [SgfType]. */
    data class Compose<S : SgfType<*>, T : SgfType<*>>(override val content: Pair<S, T>) : SgfType<Pair<S, T>>(content)
}

/** Base class for all properties, typed. */
sealed class SgfProperty<out T : SgfType<*>>(open val value: T) {
    /** Black move */
    data class B(override val value: SgfType.Move) : SgfProperty<SgfType.Move>(value)
    /** White move */
    data class W(override val value: SgfType.Move) : SgfProperty<SgfType.Move>(value)
    /** Execute move even if illegal */
    object KO : SgfProperty<SgfType.None>(SgfType.None)
    /** Set move number */
    data class MN(override val value: SgfType.Number) : SgfProperty<SgfType.Number>(value)
    /** Places the list of black [SgfType.Stone]s on the board without checking. */
    data class AB(override val value: SgfType.List<SgfType.Stone> = SgfType.List(mutableListOf()))
        : SgfProperty<SgfType.List<SgfType.Stone>>(value)
    /** Places the list of white [SgfType.Stone]s on the board without checking. */
    data class AW(override val value: SgfType.List<SgfType.Stone> = SgfType.List(mutableListOf()))
        : SgfProperty<SgfType.List<SgfType.Stone>>(value)
    /** Clears the list of [SgfType.Point]s on the board without checking. */
    data class AE(override val value: SgfType.List<SgfType.Point> = SgfType.List(mutableListOf()))
        : SgfProperty<SgfType.List<SgfType.Point>>(value)
    /** Sets the current player to [value]. */
    data class PL(override val value: SgfType.Color) : SgfProperty<SgfType.Color>(value)
    /** Provides a comment text for the given node. */
    data class C(override val value: SgfType.Text) : SgfProperty<SgfType.Text>(value)
    /** The position is even. */
    data class DM(override val value: SgfType.Double) : SgfProperty<SgfType.Double>(value)
    /** Something good for black. */
    data class GB(override val value: SgfType.Double) : SgfProperty<SgfType.Double>(value)
    /** Something good for white. */
    data class GW(override val value: SgfType.Double) : SgfProperty<SgfType.Double>(value)
    /** Node is a 'hotspot', i.e. something interesting. */
    data class HO(override val value: SgfType.Double) : SgfProperty<SgfType.Double>(value)
    /** Provides a name for the node. */
    data class N(override val value: SgfType.SimpleText) : SgfProperty<SgfType.SimpleText>(value)
    /** The position is unclear. */
    data class UC(override val value: SgfType.Double) : SgfProperty<SgfType.Double>(value)
    /** Define a value for the node. */
    data class V(override val value: SgfType.Real) : SgfProperty<SgfType.Real>(value)
    /** The played move is bad */
    data class BM(override val value: SgfType.Double) : SgfProperty<SgfType.Double>(value)
    /** The played move is doubtful */
    object DO : SgfProperty<SgfType.None>(SgfType.None)
    /** The played move is interesting */
    object IT : SgfProperty<SgfType.None>(SgfType.None)
    /** The played move is a tesuji (good move). */
    data class TE(override val value: SgfType.Double) : SgfProperty<SgfType.Double>(value)
    /** Draw an arrow pointing FROM the first point TO the second point. */
    data class AR(override val value: SgfType.List<SgfType.Compose<SgfType.Point, SgfType.Point>> = SgfType.List(mutableListOf())) : SgfProperty<SgfType.List<SgfType.Compose<SgfType.Point, SgfType.Point>>>(value)
    /** Marks the given points with a circle. */
    data class CR(override val value: SgfType.List<SgfType.Point> = SgfType.List(mutableListOf())) : SgfProperty<SgfType.List<SgfType.Point>>(value)
    /** Dim (grey out) the given points. */
    data class DD(override val value: SgfType.List<SgfType.Point> = SgfType.List(mutableListOf())) : SgfProperty<SgfType.List<SgfType.Point>>(value)
    /** Writes the given text on the board at the point. */
    data class LB(override val value: SgfType.List<SgfType.Compose<SgfType.Point, SgfType.SimpleText>> = SgfType.List(mutableListOf())) : SgfProperty<SgfType.List<SgfType.Compose<SgfType.Point, SgfType.SimpleText>>>(value)
    /** Applications should draw a simple line form one point to the other. */
    data class LN(override val value: SgfType.List<SgfType.Compose<SgfType.Point, SgfType.Point>> = SgfType.List(mutableListOf())) : SgfProperty<SgfType.List<SgfType.Compose<SgfType.Point, SgfType.Point>>>(value)
    /** Marks the given points with an 'X'. */
    data class MA(override val value: SgfType.List<SgfType.Point> = SgfType.List(mutableListOf())) : SgfProperty<SgfType.List<SgfType.Point>>(value)
    /** Selected points. Type of markup unknown */
    data class SL(override val value: SgfType.List<SgfType.Point> = SgfType.List(mutableListOf())) : SgfProperty<SgfType.List<SgfType.Point>>(value)
    /** Marks the given points with a square. */
    data class SQ(override val value: SgfType.List<SgfType.Point> = SgfType.List(mutableListOf())) : SgfProperty<SgfType.List<SgfType.Point>>(value)
    /** Marks the given points with a triangle. */
    data class TR(override val value: SgfType.List<SgfType.Point> = SgfType.List(mutableListOf())) : SgfProperty<SgfType.List<SgfType.Point>>(value)
    /** Provides the name and version number of the application used to create this gametree. */
    data class AP(override val value: SgfType.Compose<SgfType.SimpleText, SgfType.SimpleText>) : SgfProperty<SgfType.Compose<SgfType.SimpleText, SgfType.SimpleText>>(value)
    /** Provides the used charset for SimpleText and Text type. */
    data class CA(override val value: SgfType.SimpleText) : SgfProperty<SgfType.SimpleText>(value)
    /** Defines the used file format. */
    data class FF(override val value: SgfType.Number) : SgfProperty<SgfType.Number>(value)
    /** Defines the type of game. */
    data class GM(override val value: SgfType.Number) : SgfProperty<SgfType.Number>(value)
    /** Defines how variations should be shown. */
    data class ST(override val value: SgfType.Number) : SgfProperty<SgfType.Number>(value)
    /** Defines the size of the board. */
    data class SZ(override val value: SgfType.Compose<SgfType.Number, SgfType.Number>) : SgfProperty<SgfType.Compose<SgfType.Number, SgfType.Number>>(value)
    /** Provides the name of the person who made the annotations. */
    data class AN(override val value: SgfType.SimpleText) : SgfProperty<SgfType.SimpleText>(value)
    /** Provides the rank of the black player. */
    data class BR(override val value: SgfType.SimpleText) : SgfProperty<SgfType.SimpleText>(value)
    /** Provides the name of the black team. */
    data class BT(override val value: SgfType.SimpleText) : SgfProperty<SgfType.SimpleText>(value)
    /** Any copyright information (e.g. for the annotations) should be included here. */
    data class CP(override val value: SgfType.SimpleText) : SgfProperty<SgfType.SimpleText>(value)
    /** Provides the date when the game was played. */
    data class DT(override val value: SgfType.SimpleText) : SgfProperty<SgfType.SimpleText>(value)
    /** Provides the name of the event (e.g. tournament). */
    data class EV(override val value: SgfType.SimpleText) : SgfProperty<SgfType.SimpleText>(value)
    /** Provides a name for the game. The name is used to */
    data class GN(override val value: SgfType.SimpleText) : SgfProperty<SgfType.SimpleText>(value)
    /** Provides some extra information about the following game. */
    data class GC(override val value: SgfType.Text) : SgfProperty<SgfType.Text>(value)
    /** Provides some information about the opening played. */
    data class ON(override val value: SgfType.SimpleText) : SgfProperty<SgfType.SimpleText>(value)
    /** Describes the method used for overtime (byo-yomi). */
    data class OT(override val value: SgfType.SimpleText) : SgfProperty<SgfType.SimpleText>(value)
    /** Provides the name of the black player. */
    data class PB(override val value: SgfType.SimpleText) : SgfProperty<SgfType.SimpleText>(value)
    /** Provides the place where the game was played. */
    data class PC(override val value: SgfType.SimpleText) : SgfProperty<SgfType.SimpleText>(value)
    /** Provides the name of the white player. */
    data class PW(override val value: SgfType.SimpleText) : SgfProperty<SgfType.SimpleText>(value)
    /** Provides the result of the game. */
    data class RE(override val value: SgfType.SimpleText) : SgfProperty<SgfType.SimpleText>(value)
    /** Provides round-number and type of round. */
    data class RO(override val value: SgfType.SimpleText) : SgfProperty<SgfType.SimpleText>(value)
    /** Provides the used rules for this game. */
    data class RU(override val value: SgfType.SimpleText) : SgfProperty<SgfType.SimpleText>(value)
    /** Provides the name of the source (e.g. book, journal, ...). */
    data class SO(override val value: SgfType.SimpleText) : SgfProperty<SgfType.SimpleText>(value)
    /** Provides the time limits of the game. */
    data class TM(override val value: SgfType.Real) : SgfProperty<SgfType.Real>(value)
    /** Provides the name of the user (or program) who entered the game. */
    data class US(override val value: SgfType.SimpleText) : SgfProperty<SgfType.SimpleText>(value)
    /** Provides the rank of the white player. For recommended */
    data class WR(override val value: SgfType.SimpleText) : SgfProperty<SgfType.SimpleText>(value)
    /** Provide the name of the white team, if game was part of a */
    data class WT(override val value: SgfType.SimpleText) : SgfProperty<SgfType.SimpleText>(value)
    /** Time left for black, after the move was made. */
    data class BL(override val value: SgfType.Real) : SgfProperty<SgfType.Real>(value)
    /** Number of black moves left to play in this byo-yomi period. */
    data class OB(override val value: SgfType.Number) : SgfProperty<SgfType.Number>(value)
    /** Number of white moves left to play in this byo-yomi period. */
    data class OW(override val value: SgfType.Number) : SgfProperty<SgfType.Number>(value)
    /** Time left for white after the move was made. */
    data class WL(override val value: SgfType.Real) : SgfProperty<SgfType.Real>(value)
    /** The figure property is used to divide a game into */
    data class FG(override val value: SgfType.Compose<SgfType.Number, SgfType.SimpleText>) : SgfProperty<SgfType.Compose<SgfType.Number, SgfType.SimpleText>>(value)
    /** This property is used for printing. */
    data class PM(override val value: SgfType.Number) : SgfProperty<SgfType.Number>(value)
    /** View only given points of the board. */
    data class VW(override val value: SgfType.List<SgfType.Point> = SgfType.List(mutableListOf())) : SgfProperty<SgfType.List<SgfType.Point>>(value)
    /** Defines the Number of handicap stones (>=2). */
    data class HA(override val value: SgfType.Number) : SgfProperty<SgfType.Number>(value)
    /** Defines the komi. */
    data class KM(override val value: SgfType.Real) : SgfProperty<SgfType.Real>(value)
    /** Specifies the black territory or area. */
    data class TB(override val value: SgfType.List<SgfType.Point> = SgfType.List(mutableListOf())) : SgfProperty<SgfType.List<SgfType.Point>>(value)
    /** Specifies the white territory or area. */
    data class TW(override val value: SgfType.List<SgfType.Point> = SgfType.List(mutableListOf())) : SgfProperty<SgfType.List<SgfType.Point>>(value)
}

/** A [rangeTo] extension for `Pair<Int, Int>`, for allowing ranges over [SgfType.Coordinate]s. */
operator fun Pair<Int, Int>.rangeTo(other: Pair<Int, Int>): List<Pair<Int, Int>> =
    (first..other.first).flatMap { x -> (second..other.second).map { y -> (x to y) } }

/** A [rangeTo] extension for [SgfType.Coordinate], for SGF `Compose` types. */
inline operator fun <reified T : SgfType.Coordinate> T.rangeTo(snd: T): List<T> =
    (this.content..snd.content).map { SgfType.Coordinate(it).get() as T }
