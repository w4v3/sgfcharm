[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.view](../index.md) / [GoSgfView](./index.md)

# GoSgfView

`class GoSgfView : AppCompatTextView, `[`SgfView`](../-sgf-view/index.md) [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/view/GoSgfView.kt#L67)

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

### Constructors

| [&lt;init&gt;](-init-.md) | Draws the Board together with undo/redo buttons and additional informational text. It's a `TextView`, so the text it displays is styled according to the `TextView` attributes.`GoSgfView(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, attrs: `[`AttributeSet`](https://developer.android.com/reference/android/util/AttributeSet.html)`?)` |

### Properties

| [blackColor](black-color.md) | the color of black's pieces.`var blackColor: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [gridColor](grid-color.md) | the color of the grid.`var gridColor: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [gridPaint](grid-paint.md) | The paint used to draw the grid.`val gridPaint: `[`Paint`](https://developer.android.com/reference/android/graphics/Paint.html) |
| [markupColor](markup-color.md) | the color of the markup.`var markupColor: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [piecePaint](piece-paint.md) | The paint used to draw the pieces.`val piecePaint: `[`Paint`](https://developer.android.com/reference/android/graphics/Paint.html) |
| [sgfDrawer](sgf-drawer.md) | The [SgfDrawer](../-sgf-drawer/index.md) used to draw pieces and markup.`var sgfDrawer: `[`SgfDrawer`](../-sgf-drawer/index.md) |
| [showText](show-text.md) | whether or not to display information text.`var showText: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [whiteColor](white-color.md) | the color of white's pieces.`var whiteColor: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Functions

| [onDraw](on-draw.md) | Draws the view.`fun onDraw(canvas: `[`Canvas`](https://developer.android.com/reference/android/graphics/Canvas.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onMeasure](on-measure.md) | Measures the view by using the requested with and then requesting a height, including the dimensions of the text currently displayed.`fun onMeasure(widthMeasureSpec: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, heightMeasureSpec: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onReceiveSgfData](on-receive-sgf-data.md) | Called by the [SgfController](../../onion.w4v3xrmknycexlsd.lib.sgfcharm/-sgf-controller/index.md) to transmit new [data](../-sgf-view/on-receive-sgf-data.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.view.SgfView$onReceiveSgfData(kotlin.collections.List((onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.SgfData)))/data) for the view to display.`fun onReceiveSgfData(data: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SgfData`](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-sgf-data.md)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onTouchEvent](on-touch-event.md) | Categorizes touch events (`ACTION_UP` only) into moves carried out, undo button and redo button, and triggers the corresponding event of the registered [SgfInputListener](../-sgf-input-listener/index.md).`fun onTouchEvent(event: `[`MotionEvent`](https://developer.android.com/reference/android/view/MotionEvent.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [registerInputListener](register-input-listener.md) | Registers the [listener](../-sgf-view/register-input-listener.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.view.SgfView$registerInputListener(onion.w4v3xrmknycexlsd.lib.sgfcharm.view.SgfInputListener)/listener) as [SgfInputListener](../-sgf-input-listener/index.md) to trigger in response to touch events.`fun registerInputListener(listener: `[`SgfInputListener`](../-sgf-input-listener/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
