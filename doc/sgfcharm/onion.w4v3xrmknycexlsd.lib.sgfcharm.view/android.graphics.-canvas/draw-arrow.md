[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.view](../index.md) / [android.graphics.Canvas](index.md) / [drawArrow](./draw-arrow.md)

# drawArrow

`fun `[`Canvas`](https://developer.android.com/reference/android/graphics/Canvas.html)`.drawArrow(xFrom: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, yFrom: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, xTo: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, yTo: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, headRadius: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 50f, headAngle: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 15f, paint: `[`Paint`](https://developer.android.com/reference/android/graphics/Paint.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Draws an arrow from screen coordinates ([xFrom](draw-arrow.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.view$drawArrow(android.graphics.Canvas, kotlin.Float, kotlin.Float, kotlin.Float, kotlin.Float, kotlin.Float, kotlin.Float, android.graphics.Paint)/xFrom), [yFrom](draw-arrow.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.view$drawArrow(android.graphics.Canvas, kotlin.Float, kotlin.Float, kotlin.Float, kotlin.Float, kotlin.Float, kotlin.Float, android.graphics.Paint)/yFrom)) to ([xTo](draw-arrow.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.view$drawArrow(android.graphics.Canvas, kotlin.Float, kotlin.Float, kotlin.Float, kotlin.Float, kotlin.Float, kotlin.Float, android.graphics.Paint)/xTo), [yTo](draw-arrow.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.view$drawArrow(android.graphics.Canvas, kotlin.Float, kotlin.Float, kotlin.Float, kotlin.Float, kotlin.Float, kotlin.Float, android.graphics.Paint)/yTo)) on [this](draw-arrow/-this-.md) canvas.

The appearance of the arrow heads can be controlled using [headRadius](draw-arrow.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.view$drawArrow(android.graphics.Canvas, kotlin.Float, kotlin.Float, kotlin.Float, kotlin.Float, kotlin.Float, kotlin.Float, android.graphics.Paint)/headRadius) and [headAngle](draw-arrow.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.view$drawArrow(android.graphics.Canvas, kotlin.Float, kotlin.Float, kotlin.Float, kotlin.Float, kotlin.Float, kotlin.Float, android.graphics.Paint)/headAngle) (in degrees).

This is taken from https://stackoverflow.com/a/41734848/7739788.

**Author**
Steven Roelants 2017

