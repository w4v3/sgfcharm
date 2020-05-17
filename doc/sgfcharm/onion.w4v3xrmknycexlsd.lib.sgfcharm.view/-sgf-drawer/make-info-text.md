[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.view](../index.md) / [SgfDrawer](index.md) / [makeInfoText](./make-info-text.md)

# makeInfoText

`abstract fun makeInfoText(nodeInfos: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`NodeInfo`](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-node-info/index.md)`>, lastMoveInfo: `[`MoveInfo`](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-move-info/index.md)`?): `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html) [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/view/SgfDrawer.kt#L88)

Formats the [nodeInfos](make-info-text.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.view.SgfDrawer$makeInfoText(kotlin.collections.List((onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.NodeInfo)), onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.MoveInfo)/nodeInfos) and [lastMoveInfo](make-info-text.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.view.SgfDrawer$makeInfoText(kotlin.collections.List((onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.NodeInfo)), onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.MoveInfo)/lastMoveInfo) into a string.

The default implementation is very simple, it prints some move information at the top and then
each [NodeInfo](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-node-info/index.md) in the given order with the [NodeInfo.key](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-node-info/key.md) in bold.

