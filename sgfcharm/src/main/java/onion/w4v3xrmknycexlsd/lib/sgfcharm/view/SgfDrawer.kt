/*
 *    Copyright [2020] [w4v3]
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

@file:JvmName("SgfDrawerUtils")

package onion.w4v3xrmknycexlsd.lib.sgfcharm.view

import android.graphics.*
import onion.w4v3xrmknycexlsd.lib.sgfcharm.Status
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.Markup
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.MarkupType
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.MoveInfo
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.NodeInfo
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.Piece
import kotlin.math.*

/**
 * If there are some specific components for which you would like to change the way they are drawn,
 * most prominently the board markup, the pieces and the info text, you can override the corresponding
 * functions in this interface.
 *
 * You might want to reuse [Canvas.drawArrow] and [Canvas.drawTriangle] if you just want to change
 * the paint with which they are drawn, and [String.width]/[String.height] for anything involving text.
 * You can also use the [DefaultSgfDrawer] implementation for methods you do not want to override.
 */
@Status.Beta
public interface SgfDrawer {

    /**
     * Draws the [piece] at screen coordinates [x] and [y], with the given [size] and [paint],
     * returning `true` if the piece was drawn.
     *
     * The [size] parameter is the distance between two lines of the board.
     */
    @Status.Beta
    public fun Canvas.drawPiece(
        piece: Piece,
        x: Float,
        y: Float,
        size: Float,
        paint: Paint
    ): Boolean

    /**
     * Draws the [markup] at screen coordinates [x] and [y], with [xTo] and [yTo] if the
     * markup type uses two coordinates, using the [paint]. Returns `true` if the markup was drawn;
     * this can be used to specify custom drawing for some markup types only.
     *
     * The [size] parameter is the distance between two lines on the board. The passed [paint] is
     * the text paint used by the [GoSgfView].
     *
     * Note that you can *not* use this to override the drawing behavior for markup of type
     * [MarkupType.VISIBLE]. This will always draw a square in the background color over the invisible
     * parts of the board, no matter what you specify here.
     */
    @Status.Beta
    public fun Canvas.drawMarkup(
        markup: Markup,
        x: Float,
        y: Float,
        xTo: Float?,
        yTo: Float?,
        size: Float,
        paint: Paint
    ): Boolean

    /**
     * Formats the [nodeInfos] and [lastMoveInfo] into a string.
     *
     * The default implementation is very simple, it prints some move information at the top and then
     * each [NodeInfo] in the given order with the [NodeInfo.key] in bold.
     */
    @Status.Beta
    public fun makeInfoText(nodeInfos: List<NodeInfo>, lastMoveInfo: MoveInfo?): CharSequence
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
    paint.getTextBounds(this, 0, (this ?: "").length, txtBounds)
    return txtBounds.width()
}

/** Returns the height of [this] string when drawn with the given [paint]. */
@Status.Util
public fun String?.height(paint: Paint): Int {
    paint.getTextBounds(this, 0, (this ?: "").length, txtBounds)
    return txtBounds.height()
}

// for reuse
private var txtBounds = Rect()
