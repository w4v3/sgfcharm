package onion.w4v3xrmknycexlsd.lib.sgfcharmer.parse

import onion.w4v3xrmknycexlsd.lib.sgfcharmer.Util
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.parse.SgfType.XYPoint
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.parse.SgfType.XYMove
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.parse.SgfType.XYStone

// this file contains the implementations of the [SgfParser.CoordinateParser] interface

/**
 * A coordinate parser for the Go game.
 *
 * The `Point`, `Move` and `Stone` data types are all the same in Go. A `Point` consists of two
 * letters, 'a'..'z' + 'A'..'Z', where the first letter represents the column and the second one
 * the row (see [SgfType.Point] for details).
 */
object GoCoordinateParser : SgfParser.CoordinateParser<XYPoint> {
    public override fun parsePoint(from: String): XYPoint? =
        from.parseGoCoordinate()?.let { XYPoint(it.first, it.second) }

    public override fun parseMove(from: String): XYMove? =
        from.parseGoCoordinate()?.let { XYMove(it.first, it.second) }

    public override fun parseStone(from: String): XYStone? =
        from.parseGoCoordinate()?.let { XYStone(XYPoint(it.first, it.second)) }

    public override fun XYPoint.rangeTo(other: XYPoint): List<XYPoint> =
        (x..other.x).flatMap { x -> (y..other.y).map { y -> XYPoint(x, y) } }

    public override fun castToStone(value: XYPoint): XYStone = XYStone(value)

    /** Parses this string as a Go coordinate. */
    @Util
    fun String.parseGoCoordinate(): Pair<Int, Int>? =
        getOrNull(0)?.getInt()?.let { x ->
            getOrNull(1)?.getInt()?.let { y ->
                (x to y)
            }
        }

    // order is 'a'..'z','A'..'Z'; resulting coordinate should start at 1
    private fun Char.getInt(): Int? = when (this) {
        in 'a'..'z' -> ('a'..'z').indexOf(this) + 1
        in 'A'..'Z' -> ('A'..'Z').indexOf(this) + ('a'..'z').count()
        else -> null
    }
}