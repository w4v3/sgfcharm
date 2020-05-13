package onion.w4v3xrmknycexlsd.lib.sgfcharmer.view

import android.graphics.Canvas
import android.graphics.Paint
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.handle.Markup
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.handle.MoveInfo
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.handle.NodeInfo
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.handle.Piece

/**
 * If there are some specific components for which you would like to change the way they are drawn,
 * most prominently the board markup, the pieces and the info text, you can override the corresponding
 * functions in this interface
 */
interface SgfDrawer {

    /**
     * Draws the [piece] at screen coordinates [x] and [y], with the given [size] and [paint],
     * returning `true` if the piece was drawn.
     *
     * By default, this returns `false`.
     */
    fun Canvas.drawPiece(piece: Piece, x: Float, y: Float, size: Float, paint: Paint): Boolean =
        false

    /**
     * Draws the [markup] at screen coordinates [xFrom] and [yFrom], with [xTo] and [yTo] if the
     * markup type uses two coordinates, using the [paint]. Returns `true` if the markup was drawn;
     * this can be used to specify custom drawing for some markup types only.
     *
     * By default, this returns `false`.
     */
    fun Canvas.drawMarkup(
        markup: Markup,
        xFrom: Float,
        yFrom: Float,
        xTo: Float?,
        yTo: Float?,
        paint: Paint
    ): Boolean = false

    /**
     * Formats the [nodeInfos] and [lastMoveInfo] into a string.
     *
     * Defaults to [SgfView.defaultMakeInfoText].
     */

    fun makeInfoText(nodeInfos: List<NodeInfo>, lastMoveInfo: MoveInfo?): CharSequence =
        SgfView.defaultMakeInfoText(nodeInfos, lastMoveInfo)

}