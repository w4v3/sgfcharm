[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.view](../index.md) / [SgfView](./index.md)

# SgfView

`interface SgfView` [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/view/SgfView.kt#L36)

If you want to use the `sgf` parsing and handling architecture provided by this library but make
your own view with fundamental changes, you can make it implement this interface so that it can be
controlled by the [SgfController](../../onion.w4v3xrmknycexlsd.lib.sgfcharm/-sgf-controller/index.md).

If you only need to make adjustments to the way some components are drawn, consider implementing
[SgfDrawer](../-sgf-drawer/index.md) instead.

**See Also**

[SgfInputListener](../-sgf-input-listener/index.md)

### Functions

| Name | Summary |
|---|---|
| [onReceiveSgfData](on-receive-sgf-data.md) | Called by the [SgfController](../../onion.w4v3xrmknycexlsd.lib.sgfcharm/-sgf-controller/index.md) to transmit new [data](on-receive-sgf-data.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.view.SgfView$onReceiveSgfData(kotlin.collections.List((onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.SgfData)))/data) for the view to display.`abstract fun onReceiveSgfData(data: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SgfData`](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-sgf-data.md)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [registerInputListener](register-input-listener.md) | Registers the [listener](register-input-listener.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.view.SgfView$registerInputListener(onion.w4v3xrmknycexlsd.lib.sgfcharm.view.SgfInputListener)/listener) as [SgfInputListener](../-sgf-input-listener/index.md) to trigger in response to touch events.`abstract fun registerInputListener(listener: `[`SgfInputListener`](../-sgf-input-listener/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [GoSgfView](../-go-sgf-view/index.md) | Draws the Board together with undo/redo buttons and additional informational text. It's a `TextView`, so the text it displays is styled according to the `TextView` attributes.`class GoSgfView : AppCompatTextView, `[`SgfView`](./index.md) |
