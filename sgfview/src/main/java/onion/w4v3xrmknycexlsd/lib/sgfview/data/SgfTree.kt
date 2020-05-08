package onion.w4v3xrmknycexlsd.lib.sgfview.data

/** An [SgfTree] consists of a `Sequence`, which is a list of [SgfNode]s and zero or more [SgfTree]s. */
data class SgfTree(val nodes: List<SgfNode>, val children: List<SgfTree>)

typealias SgfNode = List<SgfProperty<*>>

/** A move to (x,y), where x is the row and y the column, starting at 1. Note that this is the other
 * way around as in the SGF notation. */
typealias Move = Pair<Int, Int>
/** For Go, [Move] and [Point] are the same. */
typealias Point = Pair<Int, Int>
/** For Go, [Move] and [Stone] are the same. */
typealias Stone = Pair<Int, Int>

/** Emphasis type */
enum class Double { GOOD, VERY_GOOD }

/**
 * A color to play.
 *
 * @property[PLAYER_BLACK] the black player.
 * @property[PLAYER_WHITE] the white player.
 */
enum class Player { PLAYER_BLACK, PLAYER_WHITE }

/** Base class for all properties. */
sealed class SgfProperty<out T>(val value: T) {
    /** Black move to [value] */
    class B(value: Move) : SgfProperty<Move>(value)
    /** White move to [value] */
    class W(value: Move) : SgfProperty<Move>(value)
    /** Execute move even if illegal */
    class KO(value: Unit) : SgfProperty<Unit>(value)
    /** Set move number to [value] */
    class MN(value: Int) : SgfProperty<Int>(value)
    /** Places the list of black [Stone]s on the board without checking. */
    class AB(value: List<Stone>) : SgfProperty<List<Stone>>(value)
    /** Places the list of white [Stone]s on the board without checking. */
    class AW(value: List<Stone>) : SgfProperty<List<Stone>>(value)
    /** Clears the list of [Point]s on the board without checking. */
    class AE(value: List<Point>) : SgfProperty<List<Point>>(value)
    /** Sets the current player to [value]. */
    class PL(value: Player) : SgfProperty<Player>(value)
    /** Provides a comment text for the given node. */
    class C(value: String) : SgfProperty<String>(value)
    /** The position is even. */
    class DM(value: Double) : SgfProperty<Double>(value)
    /** Something good for black. */
    class GB(value: Double) : SgfProperty<Double>(value)
    /** Something good for white. */
    class GW(value: Double) : SgfProperty<Double>(value)
    /** Node is a 'hotspot', i.e. something interesting. */
    class HO(value: Double) : SgfProperty<Double>(value)
    /** Provides a name for the node. */
    class N(value: String) : SgfProperty<String>(value)
    /** The position is unclear. */
    class UC(value: Double) : SgfProperty<Double>(value)
    /** Define a value for the node. */
    class V(value: Float) : SgfProperty<Float>(value)
    /** The played move is bad*/
    class BM(value: Double) : SgfProperty<Double>(value)
    /** The played move is doubtful*/
    class DO(value: Unit) : SgfProperty<Unit>(value)
    /** The played move is interesting*/
    class IT(value: Unit) : SgfProperty<Unit>(value)
    /** The played move is a tesuji (good move). */
    class TE(value: Double) : SgfProperty<Double>(value)
}

