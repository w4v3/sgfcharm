[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle](../index.md) / [Markup](./index.md)

# Markup

`data class Markup : `[`SgfData`](../-sgf-data.md) [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/handle/SgfData.kt#L61)

Markup of nodes on the board.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Markup of nodes on the board.`Markup(type: `[`MarkupType`](../-markup-type/index.md)`, point: Point, to: Point? = null, label: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null)` |

### Properties

| Name | Summary |
|---|---|
| [label](label.md) | the text associated with the markup type, if any (like node labels)`val label: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [point](point.md) | the point on the board where to place the markup`val point: Point` |
| [to](to.md) | if the markup type has two points, like arrows and lines, this is the second point`val to: Point?` |
| [type](type.md) | the [MarkupType](../-markup-type/index.md) of the markup`val type: `[`MarkupType`](../-markup-type/index.md) |
