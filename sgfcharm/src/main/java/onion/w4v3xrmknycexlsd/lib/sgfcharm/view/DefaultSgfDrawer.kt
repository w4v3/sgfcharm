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

package onion.w4v3xrmknycexlsd.lib.sgfcharm.view

import android.graphics.Canvas
import android.graphics.Paint
import android.text.SpannableStringBuilder
import androidx.core.text.bold
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.*
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType

/**
 * Default implementation of the [SgfDrawer] interface, used by [GoSgfView].
 *
 * It only provides the handling of [NodeInfo]s via [makeInfoText], but does not
 * influence the [GoSgfView] otherwise.
 */
class DefaultSgfDrawer : SgfDrawer {
    /**
     * Returns `false`, leading to the default behavior: A circle of the appropriate color, with size
     * slightly less than the distance between two grid lines. A border is drawn iff the white color
     * is `Color.WHITE`.
     */
    public override fun Canvas.drawPiece(
        piece: Piece,
        x: Float,
        y: Float,
        size: Float,
        paint: Paint
    ): Boolean =
        false

    /** Returns `false`, leading to the default behavior. */
    public override fun Canvas.drawMarkup(
        markup: Markup,
        x: Float,
        y: Float,
        xTo: Float?,
        yTo: Float?,
        size: Float,
        paint: Paint
    ): Boolean = false

    /**
     * Simple default implementation to turn [nodeInfos] into a `SpannedString`; it prints some move
     * information at the top and then each [NodeInfo] in the given order with the [NodeInfo.key] in bold.
     */
    public override fun makeInfoText(
        nodeInfos: List<NodeInfo>,
        lastMoveInfo: MoveInfo?
    ): CharSequence =
        SpannableStringBuilder().apply {
            lastMoveInfo?.let {
                it.lastPlaced.stone // pass move
                    ?: bold { append("#${it.lastPlaced.color} #${it.moveNumber} pass\n") }
                (it.lastPlaced.stone as? SgfType.XYStone)?.let { stone ->
                    bold { append("${it.lastPlaced.color} #${it.moveNumber} @ ${stone.point.x}-${stone.point.y}\n") }
                }
                append("# prisoners black ${it.prisoners.first} white ${it.prisoners.second}\n\n")
            }

            nodeInfos.map {
                when (it.key) {
                    in listOf(null, "") -> append(it.message + "\n")
                    else -> bold { append(it.key + " ") }.append((it.message ?: "") + "\n")
                }
            }
        }
}