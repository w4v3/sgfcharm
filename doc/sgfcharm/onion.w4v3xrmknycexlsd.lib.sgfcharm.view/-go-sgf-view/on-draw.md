[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.view](../index.md) / [GoSgfView](index.md) / [onDraw](./on-draw.md)

# onDraw

`protected fun onDraw(canvas: `[`Canvas`](https://developer.android.com/reference/android/graphics/Canvas.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/view/GoSgfView.kt#L208)

Draws the view.

First, it draws the grid representing the board, then the [Piece](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-piece/index.md) and [Markup](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-markup/index.md) data using the
[SgfDrawer](../-sgf-drawer/index.md), then undo and redo buttons, and finally, the text obtained from the [NodeInfo](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-node-info/index.md) data
using [SgfDrawer.makeInfoText](../-sgf-drawer/make-info-text.md).

