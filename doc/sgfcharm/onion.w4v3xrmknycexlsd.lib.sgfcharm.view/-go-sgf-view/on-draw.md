[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.view](../index.md) / [GoSgfView](index.md) / [onDraw](./on-draw.md)

# onDraw

`protected fun onDraw(canvas: `[`Canvas`](https://developer.android.com/reference/android/graphics/Canvas.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Draws the view.

First, it draws the grid representing the board, then the [pieces](pieces.md) and [markups](markups.md) using the
[SgfDrawer](../-sgf-drawer/index.md), then undo and redo buttons, and finally, the text obtained from the [nodeInfos](node-infos.md)
using [SgfDrawer.makeInfoText](../-sgf-drawer/make-info-text.md).

