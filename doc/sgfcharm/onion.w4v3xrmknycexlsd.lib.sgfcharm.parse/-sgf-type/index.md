[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse](../index.md) / [SgfType](./index.md)

# SgfType

`sealed class SgfType`

This is the base class for all `sgf` data types, for strong typing.

Unfortunately, neither `inline sealed class` nor union data types are supported by Kotlin, yet,
so these wrapper classes might lead to performance and memory issues for large `GameTree`s.

### Types

| Name | Summary |
|---|---|
| [Color](-color/index.md) | Color type in `sgf`.`data class Color : `[`SgfType`](./index.md) |
| [Compose](-compose/index.md) | `Compose` data type in `sgf`, a pair of [SgfType](./index.md).`data class Compose<S : `[`SgfType`](./index.md)`, T : `[`SgfType`](./index.md)`> : `[`SgfType`](./index.md) |
| [Double](-double/index.md) | Emphasis type in `sgf`.`data class Double : `[`SgfType`](./index.md) |
| [List](-list/index.md) | `List` and `Elist` data types in `sgf`, lists of [SgfType](./index.md).`data class List<T : `[`SgfType`](./index.md)`> : `[`SgfType`](./index.md)`, `[`MutableIterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterable/index.html)`<T>` |
| [Move](-move/index.md) | Supertype for `sgf Move` types. Game-specific, but might contain a [Point](-point/index.md).`abstract class Move : `[`SgfType`](./index.md) |
| [None](-none.md) | `None` data type in `sgf`.`object None : `[`SgfType`](./index.md) |
| [Number](-number/index.md) | `Number` type in `sgf` holding integer values.`data class Number : `[`SgfType`](./index.md) |
| [Point](-point/index.md) | Supertype for `sgf Point` types. Abstract, as these are game-specific.`abstract class Point : `[`SgfType`](./index.md) |
| [Real](-real/index.md) | `Real` type in `sgf` holding floating point values.`data class Real : `[`SgfType`](./index.md) |
| [SimpleText](-simple-text/index.md) | `SimpleText` type in `sgf` holding text without newlines.`data class SimpleText : `[`SgfType`](./index.md) |
| [Stone](-stone/index.md) | Supertype for `sgf Stone` types. Game-specific, but always contains a [Point](-point/index.md).`abstract class Stone : `[`SgfType`](./index.md) |
| [Text](-text/index.md) | `Text` type in `sgf` holding arbitrary text.`data class Text : `[`SgfType`](./index.md) |
| [XYMove](-x-y-move/index.md) | This is analogous to [XYPoint](-x-y-point/index.md).`data class XYMove : Move` |
| [XYPoint](-x-y-point/index.md) | A point at ([x](-x-y-point/x.md), [y](-x-y-point/y.md)), useful for Go and Chess, for example.`data class XYPoint : Point` |
| [XYStone](-x-y-stone/index.md) | This is analogous to [XYPoint](-x-y-point/index.md).`data class XYStone : Stone` |
