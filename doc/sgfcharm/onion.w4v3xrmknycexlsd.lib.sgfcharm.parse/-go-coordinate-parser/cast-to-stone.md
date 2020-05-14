[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse](../index.md) / [GoCoordinateParser](index.md) / [castToStone](./cast-to-stone.md)

# castToStone

`fun castToStone(value: XYPoint): XYStone`

Casts the given [Point](../-sgf-type/-point/index.md) to the [Stone](../-sgf-type/-stone/index.md) type, or `null` if that's impossible.

This should only be overriden by games in which [Point](../-sgf-type/-point/index.md) and [Stone](../-sgf-type/-stone/index.md) are
the same. In this case, it should cast any [Stone](../-sgf-type/-stone/index.md) to the equivalent [Point](../-sgf-type/-point/index.md)
and everything else to `null`. The only purpose of this function is to enable compressed
point lists for stones as well.

