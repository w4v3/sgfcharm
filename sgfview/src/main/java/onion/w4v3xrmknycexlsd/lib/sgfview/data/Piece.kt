package onion.w4v3xrmknycexlsd.lib.sgfview.data

/**
 * A gaming piece.
 *
 * @property[player] who played this piece.
 * @property[x] x coordinate where the piece is placed.
 * @property[y] y coordinate where the piece is placed.
 */
data class Piece(val player: Player, val x: Int, val y: Int)

