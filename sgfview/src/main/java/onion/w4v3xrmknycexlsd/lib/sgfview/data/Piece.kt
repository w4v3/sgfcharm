package onion.w4v3xrmknycexlsd.lib.sgfview.data

/**
 * A gaming piece.
 *
 * @property[color] color of this piece.
 * @property[x] x coordinate where the piece is placed.
 * @property[y] y coordinate where the piece is placed.
 */
data class Piece(val color: SgfType.Color, val x: Int, val y: Int)

