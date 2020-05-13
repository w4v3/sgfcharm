package onion.w4v3xrmknycexlsd.lib.sgfcharmer.handle

import onion.w4v3xrmknycexlsd.lib.sgfcharmer.parse.SgfType.*
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.view.SgfView

/**
 * Base class for all instructions understandable by [SgfView].
 *
 * This is to cast the semantics of the `sgf` format into the semantics of what actually
 * needs to be drawn in the end, which might be different. For example, the [SgfView] always
 * needs a complete representation of the current board configuration, but each `sgf` node
 * contains only incremental changes to the board.
 */
sealed class SgfData

/**
 * A gaming piece.
 *
 * @property[color] the color of this piece
 * @property[stone] the [Stone] of this piece
 */
data class Piece(val color: Color.Value, val stone: Stone) : SgfData()

/**
 * Information about the last move played.
 *
 * @property[moveNumber] the number of the move
 * @property[lastPlaced] the [Piece] played in the move
 * @property[prisoners] a pair of Black and White prisoner counts
 */
data class MoveInfo(var moveNumber: Int, var lastPlaced: Piece, var prisoners: Pair<Int, Int>) :
    SgfData()

/**
 * Markup of nodes on the board.
 *
 * @property[type] the [MarkupType] of the markup
 * @property[from] the point on the board where to place the markup
 * @property[to] if the markup type has two points, like arrows and lines, this is the second point
 * @property[label] the text associated with the markup type, if any (like node labels)
 */
data class Markup(
    val type: MarkupType,
    val from: Point,
    val to: Point? = null,
    val label: String? = null
) : SgfData()

/**
 * The possible values for board Markup.
 */
enum class MarkupType {
    /** Labels the currently possible variations. */
    VARIATION,

    /** Draws an arrow. */
    ARROW,

    /** Draws a circle. */
    CIRCLE,

    /** Dims given points. */
    DIM,

    /** Labels points. */
    LABEL,

    /** Draws a line. */
    LINE,

    /** Marks points with an `x`. */
    X,

    /** Highlights points. */
    SELECT,

    /** Draws a square. */
    SQUARE,

    /** Draws a triangle. */
    TRIANGLE,

    /** Declares points as visible, rest becomes invisible. */
    VISIBLE,

    /** Marks black territory (Go). */
    BLACK_TERRITORY,

    /** Marks white territory (Go). */
    WHITE_TERRITORY,
}

/**
 * A piece of information relating to the current node to be displayed in the text window.
 *
 * @property[key] the property type dependent part of the info
 * @property[message] the variable part of the info
 */
data class NodeInfo(val key: String? = null, val message: String? = null) : SgfData()
