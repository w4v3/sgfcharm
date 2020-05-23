/*
 *    Copyright 2020 w4v3
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */
@file:JvmName("SgfViewComponents")

package onion.w4v3xrmknycexlsd.lib.sgfcharm.view

import android.graphics.*
import android.text.SpannableStringBuilder
import androidx.core.text.bold
import onion.w4v3xrmknycexlsd.lib.sgfcharm.Status
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.MarkupType
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.NodeInfo
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.XYPoint
import onion.w4v3xrmknycexlsd.lib.sgfcharm.SgfInfoKeys
import kotlin.math.*

// for piece outlines and markup
// only need to allocate one paint object this way
private var muColor: Int = Color.BLUE
private var gridSep: Float = 0f
private val varPaint = Paint(Paint.ANTI_ALIAS_FLAG)
private val solidMarkupPaint: Paint
    get() = varPaint.apply {
        color = muColor
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = gridSep * 0.1f
    }
private val strokeMarkupPaint: Paint
    get() = varPaint.apply {
        color = muColor
        style = Paint.Style.STROKE
        strokeWidth = gridSep * 0.1f
    }

/**
 * The default [SgfPieceDrawer] used by the [SgfView] to draw pieces.
 *
 * It simply draws a circle of the given `blackColor` or `whiteColor`, slightly smaller than the
 * given `size`, and draws a border around the piece if the `whiteColor` is [Color.WHITE].
 *
 * @see SgfView.usePieceDrawer
 */
public val GoPieceDrawer: SgfPieceDrawer =
    lambda@{ canvas, piece, x, y, size, paint, blackColor, whiteColor ->
        with(canvas) {
            paint.apply {
                color = when (piece.color) {
                    SgfType.Color.Value.BLACK -> blackColor
                    SgfType.Color.Value.WHITE -> whiteColor
                }
            }

            drawCircle(x, y, 0.45f * size, paint)

            // draw border
            if (whiteColor == Color.WHITE) {
                varPaint.apply {
                    color = Color.BLACK
                    style = Paint.Style.STROKE
                    strokeWidth = 0.45f * size * 0.15f
                }

                drawCircle(x, y, 0.45f * size, varPaint)
            }
        }
    }

/**
 * The default [SgfMarkupDrawer] used by the [SgfView] to draw the board markup.
 *
 * You might want to reuse [Canvas.drawArrow] and [Canvas.drawTriangle] if you just want to change
 * the paint with which they are drawn, and [String.width]/[String.height] for anything involving text.
 * You can also delegate to the [DefaultMarkupDrawer] for markup types you do not want to change.
 *
 * @see SgfView.useMarkupDrawer
 */
public val DefaultMarkupDrawer: SgfMarkupDrawer =
    { canvas, markup, x, y, x2, y2, size, paint, blackColor, whiteColor, markupColor ->
        with(canvas) {
            gridSep = size
            muColor = markupColor

            when (markup.type) {
                MarkupType.ARROW -> drawArrow(
                    x,
                    y,
                    x2,
                    y2,
                    size / 2f,
                    35f,
                    solidMarkupPaint
                )
                MarkupType.CIRCLE -> drawCircle(x, y, size * 0.45f * 6 / 10, strokeMarkupPaint)
                MarkupType.DIM -> drawRect(
                    x - size / 2,
                    y - size / 2,
                    x + size / 2,
                    y + size / 2,
                    varPaint.apply {
                        color = Color.argb(200, 255, 255, 255)
                        style = Paint.Style.FILL_AND_STROKE
                        strokeWidth = 0.05f * size
                    })
                MarkupType.LABEL -> {
                    val wd = markup.label.width(paint)
                    val ht = markup.label.height(paint)
                    drawRect( // draw the text on a rectangle with an appropriate color for visibility
                        x - wd / 2,
                        y - ht / 2,
                        x + wd / 2,
                        y + ht / 2,
                        varPaint.apply { // invert the text color and "solidify"
                            color = paint.color.inv() or 0xFF000000.toInt()
                            style = Paint.Style.FILL_AND_STROKE
                            strokeWidth = 0.05f * size
                        })
                    drawText(
                        markup.label ?: "",
                        x - wd / 2,
                        y + ht / 2,
                        paint
                    )
                }
                MarkupType.LINE -> drawLine(x, y, x2, y2, varPaint.apply {
                    strokeWidth = size * 0.1f
                    style = Paint.Style.FILL
                    color = markupColor
                })
                MarkupType.X -> {
                    drawLine(
                        x - size * 0.25f,
                        y - size * 0.25f,
                        x + size * 0.25f,
                        y + size * 0.25f,
                        strokeMarkupPaint
                    )
                    drawLine(
                        x - size * 0.25f,
                        y + size * 0.25f,
                        x + size * 0.25f,
                        y - size * 0.25f,
                        strokeMarkupPaint
                    )
                }
                MarkupType.SELECT -> drawCircle(x, y, size * 0.45f, strokeMarkupPaint)
                MarkupType.SQUARE -> drawRect(
                    x - size * 0.25f,
                    y - size * 0.25f,
                    x + size * 0.25f,
                    y + size * 0.25f,
                    strokeMarkupPaint
                )
                MarkupType.TRIANGLE -> drawTriangle(
                    x.toInt(),
                    y.toInt(),
                    size.toInt() / 2,
                    strokeMarkupPaint
                )
                MarkupType.VISIBLE -> {
                }
                MarkupType.BLACK_TERRITORY -> drawRect(
                    x - size * 0.25f,
                    y - size * 0.25f,
                    x + size * 0.25f,
                    y + size * 0.25f,
                    solidMarkupPaint.apply { color = blackColor })
                MarkupType.WHITE_TERRITORY -> drawRect(
                    x - size * 0.25f,
                    y - size * 0.25f,
                    x + size * 0.25f,
                    y + size * 0.25f,
                    solidMarkupPaint.apply { color = whiteColor })
            }
        }
    }

/**
 * Draws an arrow from screen coordinates ([xFrom], [yFrom]) to ([xTo], [yTo]) on [this] canvas.
 *
 * The appearance of the arrow heads can be controlled using [headRadius] and [headAngle] (in degrees).
 *
 * This is taken from https://stackoverflow.com/a/41734848/7739788.
 * @author Steven Roelants 2017
 */
@Status.Util
public fun Canvas.drawArrow(
    xFrom: Float,
    yFrom: Float,
    xTo: Float,
    yTo: Float,
    headRadius: Float = 50f,
    headAngle: Float = 15f,
    paint: Paint
) {
    val anglerad: Float = (PI.toFloat() * headAngle / 180.0f)
    val lineangle: Float = atan2(yTo - yFrom, xTo - xFrom)

    drawLine(xFrom, yFrom, xTo, yTo, paint)

    val path = Path()
    path.fillType = Path.FillType.EVEN_ODD
    path.moveTo(xTo, yTo)
    path.lineTo(
        (xTo - headRadius * cos(lineangle - anglerad / 2.0)).toFloat(),
        (yTo - headRadius * sin(lineangle - anglerad / 2.0)).toFloat()
    )
    path.lineTo(
        (xTo - headRadius * cos(lineangle + anglerad / 2.0)).toFloat(),
        (yTo - headRadius * sin(lineangle + anglerad / 2.0)).toFloat()
    )
    path.close()
    drawPath(path, paint)
}

/** Draws a triangle centered at screen coordinates ([x], [y]) with the given [sidelength] on [this] canvas. */
@Status.Util
public fun Canvas.drawTriangle(x: Int, y: Int, sidelength: Int, paint: Paint) {
    val height = (sidelength * sqrt(3f) / 2).toInt()
    val a = Point(x, y - 2 * height / 3)
    val b = Point(x - sidelength / 2, y + height / 3)
    val c = Point(x + sidelength / 2, y + height / 3)

    val path = Path()
    path.moveTo(a.x.toFloat(), a.y.toFloat())
    path.lineTo(b.x.toFloat(), b.y.toFloat())
    path.lineTo(c.x.toFloat(), c.y.toFloat())
    path.lineTo(a.x.toFloat(), a.y.toFloat())
    path.close()

    drawPath(path, paint)
}

/** Returns the width of [this] string when drawn with the given [paint]. */
@Status.Util
public fun String?.width(paint: Paint): Int {
    paint.getTextBounds(
        this, 0, (this ?: "").length,
        txtBounds
    )
    return txtBounds.width()
}

/** Returns the height of [this] string when drawn with the given [paint]. */
@Status.Util
public fun String?.height(paint: Paint): Int {
    paint.getTextBounds(
        this, 0, (this ?: "").length,
        txtBounds
    )
    return txtBounds.height()
}

// for reuse
private var txtBounds = Rect()

/**
 * Simple default implementation of [SgfInfoTextMaker] to turn [NodeInfo]s into a `SpannedString`.
 *
 * It prints some information about the last move at the top and then each [NodeInfo] in the given order
 * with the [NodeInfo.key] in bold, putting those that do not have a key (like comments by default) to the bottom.
 *
 * @see SgfView.useInfoTextMaker
 * @see SgfInfoKeys
 */
public val DefaultInfoTextMaker: SgfInfoTextMaker =
    { nodeInfos, lastMoveInfo ->
        SpannableStringBuilder().apply {
            lastMoveInfo?.let {
                it.lastPlayed.point // pass move
                    ?: bold { append("#${it.lastColor} #${it.moveNumber} pass\n") }
                (it.lastPlayed.point as? XYPoint)?.let { point ->
                    bold { append("${it.lastColor} #${it.moveNumber} @ ${point.x}-${point.y}\n") }
                }
                append("# prisoners black ${it.prisoners.first} white ${it.prisoners.second}\n\n")
            }

            // first those with key
            nodeInfos.filter { it.key !in listOf(null, "") }.map {
                bold { append(it.key + " ") }.append((it.message ?: "") + "\n")
            }

            // then those without key
            nodeInfos.filter { it.key in listOf(null, "") }.map { append(it.message + "\n") }
        }
    }

