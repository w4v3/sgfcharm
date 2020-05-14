[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle](../index.md) / [NodeInfo](./index.md)

# NodeInfo

`data class NodeInfo : `[`SgfData`](../-sgf-data.md)

A piece of information relating to the current node to be displayed in the text window.

Anything can be used for the [key](key.md) as long as the [SgfView](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-sgf-view/index.md) knows how to deal with it,
but by default, the [SgfNodeHandler](../-sgf-node-handler/index.md) uses the values from [SgfInfoKeys](../../onion.w4v3xrmknycexlsd.lib.sgfcharm/-sgf-info-keys/index.md) to encode which
`SGF` property this info came from.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | A piece of information relating to the current node to be displayed in the text window.`NodeInfo(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null)` |

### Properties

| Name | Summary |
|---|---|
| [key](key.md) | the property type dependent part of the info`val key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [message](message.md) | the variable part of the info`val message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
