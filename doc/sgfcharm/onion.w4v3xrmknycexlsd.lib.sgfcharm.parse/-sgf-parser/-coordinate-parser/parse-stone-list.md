[sgfcharm](../../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse](../../index.md) / [SgfParser](../index.md) / [CoordinateParser](index.md) / [parseStoneList](./parse-stone-list.md)

# parseStoneList

`open fun parseStoneList(from: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<Stone>`

Parses this string into a list of [Stone](../../-sgf-type/-stone/index.md)s.

The string represents a single property value, i.e. [...](#). In case this contains a ':',
a list containing all the [Stone](../../-sgf-type/-stone/index.md)s in the compressed list must be returned,
otherwise a list with a single element. Note that a compressed list might only be given
if the [Stone](../../-sgf-type/-stone/index.md) type is the same as [Point](../../-sgf-type/-point/index.md) for that game.
For invalid input, an empty list should be returned.

The existing implementation of this should be enough for most purposes; it requires only
the implementation of [parsePointList](parse-point-list.md) and [castToStone](cast-to-stone.md).

