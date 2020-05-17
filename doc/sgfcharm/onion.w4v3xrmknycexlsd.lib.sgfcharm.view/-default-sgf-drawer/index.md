[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.view](../index.md) / [DefaultSgfDrawer](./index.md)

# DefaultSgfDrawer

`class DefaultSgfDrawer : `[`SgfDrawer`](../-sgf-drawer/index.md) [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/view/DefaultSgfDrawer.kt#L33)

Default implementation of the [SgfDrawer](../-sgf-drawer/index.md) interface, used by [GoSgfView](../-go-sgf-view/index.md).

It only provides the handling of [NodeInfo](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-node-info/index.md)s via [makeInfoText](make-info-text.md), but does not
influence the [GoSgfView](../-go-sgf-view/index.md) otherwise.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Default implementation of the [SgfDrawer](../-sgf-drawer/index.md) interface, used by [GoSgfView](../-go-sgf-view/index.md).`DefaultSgfDrawer()` |

### Functions

| Name | Summary |
|---|---|
| [drawMarkup](draw-markup.md) | Returns `false`.`fun `[`Canvas`](https://developer.android.com/reference/android/graphics/Canvas.html)`.drawMarkup(markup: `[`Markup`](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-markup/index.md)`, x: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, y: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, xTo: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`?, yTo: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`?, size: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, paint: `[`Paint`](https://developer.android.com/reference/android/graphics/Paint.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [drawPiece](draw-piece.md) | Returns `false`.`fun `[`Canvas`](https://developer.android.com/reference/android/graphics/Canvas.html)`.drawPiece(piece: `[`Piece`](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-piece/index.md)`, x: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, y: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, size: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, paint: `[`Paint`](https://developer.android.com/reference/android/graphics/Paint.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [makeInfoText](make-info-text.md) | Simple default implementation to turn [nodeInfos](make-info-text.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.view.DefaultSgfDrawer$makeInfoText(kotlin.collections.List((onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.NodeInfo)), onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.MoveInfo)/nodeInfos) into a `SpannedString`; it prints some move information at the top and then each [NodeInfo](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-node-info/index.md) in the given order with the [NodeInfo.key](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-node-info/key.md) in bold.`fun makeInfoText(nodeInfos: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`NodeInfo`](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-node-info/index.md)`>, lastMoveInfo: `[`MoveInfo`](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-move-info/index.md)`?): `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html) |
