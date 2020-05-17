[sgfcharm](../../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse](../../index.md) / [SgfParser](../index.md) / [CoordinateParser](index.md) / [rangeTo](./range-to.md)

# rangeTo

`abstract operator fun T.rangeTo(other: T): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>` [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/parse/SgfParser.kt#L110)

Returns a list of [Point](../../-sgf-type/-point/index.md)s contained in a rectangle between [this](range-to/-this-.md) upper left
and the [other](range-to.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfParser.CoordinateParser$rangeTo(onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfParser.CoordinateParser.T, onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfParser.CoordinateParser.T)/other) lower right [Point](../../-sgf-type/-point/index.md). Returns an empty list if that rectangle is
empty, including the case where the other point lies to the left or above this point.

