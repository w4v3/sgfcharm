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
import android.graphics.Rect
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.*
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.XYMove
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.XYPoint
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

/** Default implementation of the [SgfBoard] used by the [SgfView] for the game Go, based on [XYPoint]s and [XYMove]s. */
object GoBoard : SgfBoard {
    override var tileSize: Int = 0

    private var displayedVariations: List<SgfType.Point> = listOf()
    private var boardPadding = 0
    private var gridSep = 0
    private var onDisplay = Rect(0, 0, 0, 0)

    override fun setBoard(toDisplay: Rect, width: Int): Int {
        onDisplay = toDisplay
        gridSep = width / (toDisplay.right - toDisplay.left + 1)
        boardPadding = gridSep / 2
        tileSize = gridSep
        return gridSep * (toDisplay.bottom - toDisplay.top) + 2 * boardPadding
    }

    override fun getAllPoints(numColumns: Int, numRows: Int): Set<SgfType.Point> =
        (1..numColumns).flatMap { x -> (1..numRows).map { y -> XYPoint(x, y) } }.toSet()

    public override fun Canvas.drawBoard(
        trueColumns: Int,
        trueRows: Int,
        onDisplay: Rect,
        paint: Paint
    ) {
        val fromx = max(onDisplay.left - 1, 1)
        val tox = min(onDisplay.right + 1, trueColumns)
        val fromy = max(onDisplay.top - 1, 1)
        val toy = min(onDisplay.bottom + 1, trueRows)

        for (row in onDisplay.top..onDisplay.bottom) {
            drawLine(
                getXCoordinateFromBoard(fromx),
                getYCoordinateFromBoard(row),
                getXCoordinateFromBoard(tox),
                getYCoordinateFromBoard(row),
                paint
            )
        }

        for (col in onDisplay.left..onDisplay.right) {
            drawLine(
                getXCoordinateFromBoard(col),
                getYCoordinateFromBoard(fromy),
                getXCoordinateFromBoard(col),
                getYCoordinateFromBoard(toy),
                paint
            )
        }
    }

    public override fun SgfType.Point.toScreenCoordinate(): Pair<Float, Float> =
        (this as? XYPoint)?.let { (x, y) -> getXCoordinateFromBoard(x) to getYCoordinateFromBoard(y) }
            ?: (0f to 0f)

    private fun Pair<Float, Float>.toBoardCoordinate(): XYPoint =
        XYPoint(
            round(getXCoordinateFromScreen(first)).toInt(),
            round(getYCoordinateFromScreen(second)).toInt()
        )

    // convert between coordinates on board and on screen
    private val getXCoordinateFromBoard = { col: Int ->
        boardPadding.toFloat() +
                (col - onDisplay.left) * gridSep
    }

    private val getYCoordinateFromBoard = { row: Int ->
        boardPadding.toFloat() +
                (row - onDisplay.top) * gridSep
    }

    private val getXCoordinateFromScreen = { x: Float ->
        (x - boardPadding.toFloat()) /
                gridSep + onDisplay.left
    }

    private val getYCoordinateFromScreen = { y: Float ->
        (y - boardPadding.toFloat()) /
                gridSep + onDisplay.top
    }

    /**
     * Implements a simple variation markup with letters, where variations without moves are put to
     * the middle of the board, with as equal spacing as possible, respecting other variations
     * that might have been printed there already (i.e., those with moves).
     */
    override fun Canvas.drawVariations(variationData: List<VariationData>, paint: Paint) {
        // the variations will be labeled 'A'..'Z' and in theory continuing further by 16 bit unicode
        // some of the nodes might contain a move, for which we would like to place the label
        // at the position of that move, but others might not, so these will be put onto the middle
        // line, equally spaced apart if possible
        val labeledVariations = variationData
            .map { (('A'..'Z').elementAtOrNull(it.index) ?: '?') to it.move }

        val variationMarkup = // move properties can be added directly
            labeledVariations.filter { it.second?.point != null }
                .map { it.first to it.second?.point!! }.toMutableList()

        // for the rest, we first find out the optimal spacing
        val noMove = labeledVariations.filter { it.second == null }
        val gridRows = onDisplay.bottom - onDisplay.top + 1
        val gridColumns = onDisplay.right - onDisplay.left + 1
        var y = ((gridRows + 1) / 2).coerceAtLeast(1)
        val dx = ((gridColumns - 1) / (noMove.size + 1)).coerceAtLeast(1)

        // now we try and place them there if there is place, otherwise we shift things around
        noMove.forEachIndexed { idx, (first) ->
            var x = (idx + 1) * dx + 1 // column count starts at 1 so +1
            while (variationMarkup.find { it.second == XYPoint(x, y) } != null) {
                // here the place is already taken, so we increase x
                x += 1
                if (x > gridColumns) {
                    // or y if x became too big; worst case is that it falls off the board in y direction
                    x = (idx + 1) * dx
                    y++
                }
            }
            // unoccupied space found, add here
            variationMarkup.add(first to XYPoint(x, y))
        }

        variationMarkup.sortBy { it.first }

        variationMarkup.map {
            it.second.toScreenCoordinate().let { (x, y) ->
                drawText(
                    it.first.toString(),
                    x - it.first.toString().width(paint) / 2,
                    y + it.first.toString().height(paint) / 2,
                    paint
                )
            }
        }

        displayedVariations = variationMarkup.map { it.second }
    }

    override fun Pair<Float, Float>.onTouch(currentPieces: List<Piece>): Pair<SgfType.Move?, Int?> =
        toBoardCoordinate().let { (xb, yb) ->
            XYMove(xb, yb).takeIf { (xb.toFloat() to yb.toFloat()) in onDisplay } to
                    displayedVariations.indexOf(XYPoint(xb, yb)).takeIf { it >= 0 }
        }
}
