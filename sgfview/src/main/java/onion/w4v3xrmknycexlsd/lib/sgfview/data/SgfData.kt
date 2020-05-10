package onion.w4v3xrmknycexlsd.lib.sgfview.data

/**
 * Base class for all instructions understandable by [SgfView].
 *
 * This is to cast the semantics of the `sgf` format into the semantics of what actually
 * needs to be drawn in the end, which might be different. For example, the [SgfView] always
 * has a complete representation of the current board configuration, but each `sgf` node
 * contains only incremental changes to the board.
 */
sealed class SgfData

/**
 * A gaming piece.
 *
 * @property[color] color of this piece.
 * @property[x] x coordinate where the piece is placed.
 * @property[y] y coordinate where the piece is placed.
 */
data class Piece(val color: ColorValue, val x: Int, val y: Int) : SgfData()

