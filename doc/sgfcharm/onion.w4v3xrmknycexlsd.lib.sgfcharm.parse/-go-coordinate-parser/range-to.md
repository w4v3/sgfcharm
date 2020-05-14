[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse](../index.md) / [GoCoordinateParser](index.md) / [rangeTo](./range-to.md)

# rangeTo

`fun XYPoint.rangeTo(other: XYPoint): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<XYPoint>`

Returns a list of [Point](../-sgf-type/-point/index.md)s contained in a rectangle between [this](../-sgf-parser/-coordinate-parser/range-to/-this-.md) upper left
and the [other](../-sgf-parser/-coordinate-parser/range-to.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfParser.CoordinateParser$rangeTo(onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfParser.CoordinateParser.T, onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfParser.CoordinateParser.T)/other) lower right [Point](../-sgf-type/-point/index.md). Returns an empty list if that rectangle is
empty, including the case where the other point lies to the left or above this point.

