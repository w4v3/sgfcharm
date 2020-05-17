[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.view](../index.md) / [GoSgfView](index.md) / [onTouchEvent](./on-touch-event.md)

# onTouchEvent

`fun onTouchEvent(event: `[`MotionEvent`](https://developer.android.com/reference/android/view/MotionEvent.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/view/GoSgfView.kt#L541)

Categorizes touch events (`ACTION_UP` only) into moves carried out, undo button and redo button,
and triggers the corresponding event of the registered [SgfInputListener](../-sgf-input-listener/index.md).

