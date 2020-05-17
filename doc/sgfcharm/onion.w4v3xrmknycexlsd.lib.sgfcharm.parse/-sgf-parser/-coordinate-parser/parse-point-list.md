[sgfcharm](../../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse](../../index.md) / [SgfParser](../index.md) / [CoordinateParser](index.md) / [parsePointList](./parse-point-list.md)

# parsePointList

`open fun parsePointList(from: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>` [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/parse/SgfParser.kt#L123)

Parses this string into a list of [Point](../../-sgf-type/-point/index.md)s.

The string represents a single property value, i.e. [...](#). In case this contains a ':',
a list containing all the [Point](../../-sgf-type/-point/index.md)s in the compressed list must be returned,
otherwise a list with a single element. For invalid input, an empty list should be returned.

The existing implementation of this should be enough for most purposes; it requires only
the implementation of [rangeTo](range-to.md).

