[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.view](../index.md) / [DefaultSgfDrawer](index.md) / [makeInfoText](./make-info-text.md)

# makeInfoText

`fun makeInfoText(nodeInfos: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`NodeInfo`](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-node-info/index.md)`>, lastMoveInfo: `[`MoveInfo`](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-move-info/index.md)`?): `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html) [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/view/DefaultSgfDrawer.kt#L59)

Simple default implementation to turn [nodeInfos](make-info-text.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.view.DefaultSgfDrawer$makeInfoText(kotlin.collections.List((onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.NodeInfo)), onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.MoveInfo)/nodeInfos) into a `SpannedString`; it prints some move
information at the top and then each [NodeInfo](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-node-info/index.md) in the given order with the [NodeInfo.key](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-node-info/key.md) in bold.

