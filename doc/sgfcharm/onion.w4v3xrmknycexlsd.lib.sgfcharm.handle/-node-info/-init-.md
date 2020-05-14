[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle](../index.md) / [NodeInfo](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`NodeInfo(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null)`

A piece of information relating to the current node to be displayed in the text window.

Anything can be used for the [key](key.md) as long as the [SgfView](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-sgf-view/index.md) knows how to deal with it,
but by default, the [SgfNodeHandler](../-sgf-node-handler/index.md) uses the values from [SgfInfoKeys](../../onion.w4v3xrmknycexlsd.lib.sgfcharm/-sgf-info-keys/index.md) to encode which
`SGF` property this info came from.

