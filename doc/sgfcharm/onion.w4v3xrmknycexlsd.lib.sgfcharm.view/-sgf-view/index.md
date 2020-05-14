[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.view](../index.md) / [SgfView](./index.md)

# SgfView

`interface SgfView`

If you want to use the `sgf` parsing and handling architecture provided by this library but make
your own view with fundamental changes, you can make it implement this interface so that it can be
controlled by the [SgfController](../../onion.w4v3xrmknycexlsd.lib.sgfcharm/-sgf-controller/index.md). You do not need to actually implement anything, just override
the properties here and they will be populated by the [SgfController](../../onion.w4v3xrmknycexlsd.lib.sgfcharm/-sgf-controller/index.md).

You will however need to call the appropriate [inputListener](input-listener.md) members when handling touch events
if you want anything to happen in response to them.

If you only need to make adjustments to the way some components are drawn, consider implementing
[SgfDrawer](../-sgf-drawer/index.md) instead.

**See Also**

[SgfInputListener](../-sgf-input-listener/index.md)

### Properties

| Name | Summary |
|---|---|
| [gridColumns](grid-columns.md) | The number of columns of the board..`abstract var gridColumns: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [gridRows](grid-rows.md) | The number of rows of the board.`abstract var gridRows: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [inputListener](input-listener.md) | The [SgfInputListener](../-sgf-input-listener/index.md) to trigger in response to touch events.`abstract var inputListener: `[`SgfInputListener`](../-sgf-input-listener/index.md)`?` |
| [lastMoveInfo](last-move-info.md) | The [MoveInfo](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-move-info/index.md) object from the last move.`abstract var lastMoveInfo: `[`MoveInfo`](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-move-info/index.md)`?` |
| [markups](markups.md) | The current board [Markup](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-markup/index.md)s.`abstract var markups: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Markup`](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-markup/index.md)`>` |
| [nodeInfos](node-infos.md) | The [NodeInfo](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-node-info/index.md)s from the current node.`abstract var nodeInfos: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`NodeInfo`](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-node-info/index.md)`>` |
| [pieces](pieces.md) | The current [Piece](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-piece/index.md)s on the board.`abstract var pieces: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Piece`](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-piece/index.md)`>` |

### Functions

| Name | Summary |
|---|---|
| [invalidate](invalidate.md) | Make the view redraw itself. Triggered after all the information from the node was loaded into the View. You probably do not need to override this as this is implemented by the `View` class itself.`abstract fun invalidate(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [GoSgfView](../-go-sgf-view/index.md) | Draws the Board together with undo/redo buttons and additional informational text. It's a `TextView`, so the text it displays is styled according to the `TextView` attributes.`class GoSgfView : AppCompatTextView, `[`SgfView`](./index.md) |
