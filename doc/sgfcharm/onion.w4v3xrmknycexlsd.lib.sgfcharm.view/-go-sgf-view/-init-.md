[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.view](../index.md) / [GoSgfView](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`GoSgfView(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, attrs: `[`AttributeSet`](https://developer.android.com/reference/android/util/AttributeSet.html)`?)`

Draws the Board together with undo/redo buttons and additional informational text. It's a
`TextView`, so the text it displays is styled according to the `TextView` attributes.

The layout will be as follows: The grid is drawn with an additional padding to account for
pieces at the borders of the board. Below the grid, there are undo and redo buttons, square
shaped, in the size of half the space between two lines on the grid. Directly underneath, the
text is drawn using the `android:text*` attributes.

For best results, you should not set the height of the view to an absolute value, use
`MATCH_PARENT` or `WRAP_CONTENT` instead. Otherwise, the view might be clipped.

If you need more control, consider setting the [sgfDrawer](sgf-drawer.md) property to a custom implementation of
[SgfDrawer](../-sgf-drawer/index.md), or making a custom view that implements [SgfView](../-sgf-view/index.md).

**See Also**

[SgfDrawer](../-sgf-drawer/index.md)

