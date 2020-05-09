package onion.w4v3xrmknycexlsd.lib.sgfview.data

/** An [SgfTree] consists of a `Sequence`, which is a list of [SgfNode]s and zero or more [SgfTree]s. */
data class SgfTree(
    var parent: SgfTree? = null,
    val nodes: MutableList<SgfNode> = mutableListOf(),
    val children: MutableList<SgfTree> = mutableListOf()
)

typealias SgfNode = MutableList<SgfProperty<SgfType>>


enum class DoubleValue { GOOD, VERY_GOOD }
enum class PlayerValue { PLAYER_BLACK, PLAYER_WHITE }

sealed class SgfType {
    /** A move to (x,y), where x is the row and y the column, starting at 1. Note that this is the other
     * way around as in the SGF notation. */
    data class Move(val x: Int, val y: Int) : SgfType()
    /** For Go, [Move] and [Point] are the same. */
    data class Point(val x: Int, val y: Int) : SgfType()
    /** For Go, [Move] and [Stone] are the same. */
    data class Stone(val x: Int, val y: Int) : SgfType()
    /** Emphasis type */
    data class Double(val value: DoubleValue) : SgfType()
    /** A color to play. */
    data class Player(val value: PlayerValue) : SgfType()
    data class Number(val value: Int) : SgfType()
    data class Real(val value: Float) : SgfType()
    data class Text(val value: String) : SgfType()
    data class SimpleText(val value: String) : SgfType()
    data class None(val value: Unit = Unit) : SgfType()
    data class List<T : SgfType>(val elem: List<T>) : SgfType()
}

/** Base class for all properties. */
sealed class SgfProperty<out T : SgfType>(var vlu: SgfType?) {
    /** Black move to [value] */
    data class B(var value: SgfType.Move? = null) : SgfProperty<SgfType.Move>(value)
    /** White move to [value] */
    data class W(var value: SgfType.Move? = null) : SgfProperty<SgfType.Move>(value)
    /** Execute move even if illegal */
    data class KO(var value: SgfType.None? = null) : SgfProperty<SgfType.None>(value)
    /** Set move number to [value] */
    data class MN(var value: SgfType.Number? = null) : SgfProperty<SgfType.Number>(value)
    /** Places the list of black [Stone]s on the board without checking. */
    data class AB(var value: SgfType.List<SgfType.Stone>? = null) : SgfProperty<SgfType.List<SgfType.Stone>>(value)
    /** Places the list of white [Stone]s on the board without checking. */
    data class AW(var value: SgfType.List<SgfType.Stone>? = null) : SgfProperty<SgfType.List<SgfType.Stone>>(value)
    /** Clears the list of [Point]s on the board without checking. */
    data class AE(var value: SgfType.List<SgfType.Point>? = null) : SgfProperty<SgfType.List<SgfType.Point>>(value)
    /** Sets the current player to [value]. */
    data class PL(var value: SgfType.Player? = null) : SgfProperty<SgfType.Player>(value)
    /** Provides a comment text for the given node. */
    data class C(var value: SgfType.Text? = null) : SgfProperty<SgfType.Text>(value)
    /** The position is even. */
    data class DM(var value: SgfType.Double? = null) : SgfProperty<SgfType.Double>(value)
    /** Something good for black. */
    data class GB(var value: SgfType.Double? = null) : SgfProperty<SgfType.Double>(value)
    /** Something good for white. */
    data class GW(var value: SgfType.Double? = null) : SgfProperty<SgfType.Double>(value)
    /** Node is a 'hotspot', i.e. something interesting. */
    data class HO(var value: SgfType.Double? = null) : SgfProperty<SgfType.Double>(value)
    /** Provides a name for the node. */
    data class N(var value: SgfType.SimpleText? = null) : SgfProperty<SgfType.SimpleText>(value)
    /** The position is unclear. */
    data class UC(var value: SgfType.Double? = null) : SgfProperty<SgfType.Double>(value)
    /** Define a value for the node. */
    data class V(var value: SgfType.Real? = null) : SgfProperty<SgfType.Real>(value)
    /** The played move is bad*/
    data class BM(var value: SgfType.Double? = null) : SgfProperty<SgfType.Double>(value)
    /** The played move is doubtful*/
    data class DO(var value: SgfType.None? = null) : SgfProperty<SgfType.None>(value)
    /** The played move is interesting*/
    data class IT(var value: SgfType.None? = null) : SgfProperty<SgfType.None>(value)
    /** The played move is a tesuji (good move). */
    data class TE(var value: SgfType.Double? = null) : SgfProperty<SgfType.Double>(value)
    /** For unknown properties */
    data class UNKNOWN(var value: SgfType.None? = null) : SgfProperty<SgfType.None>(value)
}

