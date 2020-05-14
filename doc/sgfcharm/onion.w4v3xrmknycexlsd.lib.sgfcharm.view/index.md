[sgfcharm](../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.view](./index.md)

## Package onion.w4v3xrmknycexlsd.lib.sgfcharm.view

This package contains the logic for rendering [SgfData](../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-sgf-data.md) received from the [SgfController](../onion.w4v3xrmknycexlsd.lib.sgfcharm/-sgf-controller/index.md):

* the [GoSgfView](-go-sgf-view/index.md) can render the data into an interactive view, and forwards touch events to the [SgfController](../onion.w4v3xrmknycexlsd.lib.sgfcharm/-sgf-controller/index.md)
* the [SgfView](-sgf-view/index.md) interface can be implemented by any view to be controllable by the [SgfController](../onion.w4v3xrmknycexlsd.lib.sgfcharm/-sgf-controller/index.md)
* the [SgfDrawer](-sgf-drawer/index.md) interface can be implemented if only relatively minor changes to the [GoSgfView](-go-sgf-view/index.md) drawing are required

### Types

| Name | Summary |
|---|---|
| [DefaultSgfDrawer](-default-sgf-drawer/index.md) | Default implementation of the [SgfDrawer](-sgf-drawer/index.md) interface, used by [GoSgfView](-go-sgf-view/index.md).`class DefaultSgfDrawer : `[`SgfDrawer`](-sgf-drawer/index.md) |
| [GoSgfView](-go-sgf-view/index.md) | Draws the Board together with undo/redo buttons and additional informational text. It's a `TextView`, so the text it displays is styled according to the `TextView` attributes.`class GoSgfView : AppCompatTextView, `[`SgfView`](-sgf-view/index.md) |
| [SgfDrawer](-sgf-drawer/index.md) | If there are some specific components for which you would like to change the way they are drawn, most prominently the board markup, the pieces and the info text, you can override the corresponding functions in this interface.`interface SgfDrawer` |
| [SgfInputListener](-sgf-input-listener/index.md) | Interface for communicating touch events from the view to the controller.`interface SgfInputListener` |
| [SgfView](-sgf-view/index.md) | If you want to use the `sgf` parsing and handling architecture provided by this library but make your own view with fundamental changes, you can make it implement this interface so that it can be controlled by the [SgfController](../onion.w4v3xrmknycexlsd.lib.sgfcharm/-sgf-controller/index.md). You do not need to actually implement anything, just override the properties here and they will be populated by the [SgfController](../onion.w4v3xrmknycexlsd.lib.sgfcharm/-sgf-controller/index.md).`interface SgfView` |

### Extensions for External Classes

| Name | Summary |
|---|---|
| [android.graphics.Canvas](android.graphics.-canvas/index.md) |  |
| [kotlin.String](kotlin.-string/index.md) |  |
