[sgfcharm](../../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse](../../index.md) / [SgfParser](../index.md) / [CoordinateParser](./index.md)

# CoordinateParser

`interface CoordinateParser<T : Point>`

The interface to implement for parsing of the property values for the game-specific
[Point](../../-sgf-type/-point/index.md), [Move](../../-sgf-type/-move/index.md) and [Stone](../../-sgf-type/-stone/index.md) types.

For a specific game, you need to implement [parsePoint](parse-point.md), [parseMove](parse-move.md) and [parseStone](parse-stone.md), as
well as [rangeTo](range-to.md) for the `Point` type to allow parsing compressed point lists. In addition,
if the `Stone` and the `Point` type for the game are the same, you need to override [castToStone](cast-to-stone.md)
to allow compressed lists for the `Stone` type as well.

Alternatively, you can implement [parsePointList](parse-point-list.md) and [parseStoneList](parse-stone-list.md) yourself if you need
more control.

### Parameters

`T` - the type of [Point](../../-sgf-type/-point/index.md)

### Functions

| Name | Summary |
|---|---|
| [castToStone](cast-to-stone.md) | Casts the given [Point](../../-sgf-type/-point/index.md) to the [Stone](../../-sgf-type/-stone/index.md) type, or `null` if that's impossible.`open fun castToStone(value: T): Stone?` |
| [parseMove](parse-move.md) | Parses the [from](parse-move.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfParser.CoordinateParser$parseMove(kotlin.String)/from) string representing an `sgf Move` into an [Move](../../-sgf-type/-move/index.md) object.`abstract fun parseMove(from: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): Move?` |
| [parsePoint](parse-point.md) | Parses the [from](parse-point.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfParser.CoordinateParser$parsePoint(kotlin.String)/from) string representing an `sgf Point` into an [Point](../../-sgf-type/-point/index.md) object.`abstract fun parsePoint(from: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): T?` |
| [parsePointList](parse-point-list.md) | Parses this string into a list of [Point](../../-sgf-type/-point/index.md)s.`open fun parsePointList(from: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>` |
| [parseStone](parse-stone.md) | Parses the [from](parse-stone.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfParser.CoordinateParser$parseStone(kotlin.String)/from) string representing an `sgf Stone` into an [Stone](../../-sgf-type/-stone/index.md) object.`abstract fun parseStone(from: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): Stone?` |
| [parseStoneList](parse-stone-list.md) | Parses this string into a list of [Stone](../../-sgf-type/-stone/index.md)s.`open fun parseStoneList(from: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<Stone>` |
| [rangeTo](range-to.md) | Returns a list of [Point](../../-sgf-type/-point/index.md)s contained in a rectangle between [this](range-to/-this-.md) upper left and the [other](range-to.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfParser.CoordinateParser$rangeTo(onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfParser.CoordinateParser.T, onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfParser.CoordinateParser.T)/other) lower right [Point](../../-sgf-type/-point/index.md). Returns an empty list if that rectangle is empty, including the case where the other point lies to the left or above this point.`abstract operator fun T.rangeTo(other: T): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>` |
