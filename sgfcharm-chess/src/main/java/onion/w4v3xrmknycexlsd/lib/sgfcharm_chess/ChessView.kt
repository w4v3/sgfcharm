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

package onion.w4v3xrmknycexlsd.lib.sgfcharm_chess

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import androidx.core.content.ContextCompat
import androidx.core.graphics.*
import onion.w4v3xrmknycexlsd.lib.sgfcharm.Status
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.Piece
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.VariationData
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.XYPoint
import onion.w4v3xrmknycexlsd.lib.sgfcharm.view.*
import kotlin.math.round

/** Draws a chess piece on the canvas. */
@Status.Beta
public val ChessPieceDrawer: ((Context) -> SgfPieceDrawer) =
    { context ->
        lambda@{ canvas, piece, x, y, size, _, blackColor, whiteColor ->
            if (piece.stone !is ChessStone) return@lambda

            val rect = Rect(
                x.toInt() - size.toInt() / 2,
                y.toInt() - size.toInt() / 2,
                x.toInt() + size.toInt() / 2,
                y.toInt() + size.toInt() / 2
            )

            val image = if (piece.color == SgfType.Color.Value.BLACK) {
                when ((piece.stone as ChessStone).type) {
                    ChessStoneType.PAWN -> ContextCompat.getDrawable(context, R.drawable.b_pawn)
                    ChessStoneType.ROOK -> ContextCompat.getDrawable(context, R.drawable.b_rook)
                    ChessStoneType.KNIGHT -> ContextCompat.getDrawable(context, R.drawable.b_knight)
                    ChessStoneType.BISHOP -> ContextCompat.getDrawable(context, R.drawable.b_bishop)
                    ChessStoneType.QUEEN -> ContextCompat.getDrawable(context, R.drawable.b_queen)
                    ChessStoneType.KING -> ContextCompat.getDrawable(context, R.drawable.b_king)
                }
            } else {
                when ((piece.stone as ChessStone).type) {
                    ChessStoneType.PAWN -> ContextCompat.getDrawable(context, R.drawable.w_pawn)
                    ChessStoneType.ROOK -> ContextCompat.getDrawable(context, R.drawable.w_rook)
                    ChessStoneType.KNIGHT -> ContextCompat.getDrawable(context, R.drawable.w_knight)
                    ChessStoneType.BISHOP -> ContextCompat.getDrawable(context, R.drawable.w_bishop)
                    ChessStoneType.QUEEN -> ContextCompat.getDrawable(context, R.drawable.w_queen)
                    ChessStoneType.KING -> ContextCompat.getDrawable(context, R.drawable.w_king)
                }
            }

            image?.bounds = rect

            val color = if (piece.color == SgfType.Color.Value.BLACK) blackColor else whiteColor
            val (r, g, b, a) = listOf(
                color.red.toFloat(),
                color.green.toFloat(),
                color.blue.toFloat(),
                color.alpha.toFloat()
            )

            val colorMatrix = if (piece.color == SgfType.Color.Value.BLACK) {
                floatArrayOf(
                    (255 - r) / 255, 0f, 0f, 0f, r,
                    0f, (255 - g) / 255, 0f, 0f, g,
                    0f, 0f, (255 - b) / 255, 0f, b,
                    0f, 0f, 0f, a / 255, 0f
                )
            } else {
                floatArrayOf(
                    r / 255, 0f, 0f, 0f, 0f,
                    0f, g / 255, 0f, 0f, 0f,
                    0f, 0f, b / 255, 0f, 0f,
                    0f, 0f, 0f, a / 255, 0f
                )
            }

            image?.colorFilter = ColorMatrixColorFilter(colorMatrix)

            image?.draw(canvas)
        }
    }

/** Draws a checkerboard pattern for chess boards and converts touch input to [ChessMove]s. */
@Status.Beta
object ChessBoard : SgfBoard {
    private var boardPadding = 0
    private var gridSep = 0
    private var onDisplay = Rect(0, 0, 0, 0)

    private var displayedVariations: List<XYPoint> = listOf()

    override var tileSize: Int = 0

    // need 2 gridsep board padding because stones are not put on tile intersections
    override fun setBoard(toDisplay: Rect, width: Int): Int {
        // bottom and top will be inverted because chess coordinates start at bottom left
        onDisplay = toDisplay
        gridSep = width / (toDisplay.right - toDisplay.left + 2)
        boardPadding = gridSep
        tileSize = gridSep
        return gridSep * (toDisplay.bottom - toDisplay.top) + 2 * boardPadding
    }

    override fun getAllPoints(numColumns: Int, numRows: Int): Set<SgfType.Point> =
        GoBoard.getAllPoints(numColumns, numRows)

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
                (onDisplay.bottom - row) * gridSep
    }
    private val getXCoordinateFromScreen = { x: Float ->
        (x - boardPadding.toFloat()) / gridSep +
                onDisplay.left
    }
    private val getYCoordinateFromScreen = { y: Float ->
        onDisplay.bottom -
                (y - boardPadding.toFloat()) / gridSep
    }

    override fun Canvas.drawBoard(trueColumns: Int, trueRows: Int, onDisplay: Rect, paint: Paint) {
        var blackTile = onDisplay.left % 2 == 1 && onDisplay.top % 2 == 0
        for (row in onDisplay.bottom downTo onDisplay.top) {
            for (col in onDisplay.left..onDisplay.right) {
                if (blackTile) {
                    paint.style = Paint.Style.FILL_AND_STROKE
                } else {
                    paint.style = Paint.Style.STROKE
                }

                drawRect(
                    getXCoordinateFromBoard(col) - gridSep / 2,
                    getYCoordinateFromBoard(row) - gridSep / 2,
                    getXCoordinateFromBoard(col) + gridSep / 2,
                    getYCoordinateFromBoard(row) + gridSep / 2,
                    paint
                )

                blackTile = !blackTile
            }
            blackTile = !blackTile
        }
    }

    private val varPaint = Paint(ANTI_ALIAS_FLAG)
    private val solidMarkupPaint: Paint
        get() = varPaint.apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = gridSep * 0.1f
        }

    @OptIn(Status.Util::class)
    override fun Canvas.drawVariations(variationData: List<VariationData>, paint: Paint): Unit {
        variationData.map {
            when (val move = it.move) {
                is StandardChessMove -> move.let { (stone, point) ->
                    drawArrow(
                        getXCoordinateFromBoard(stone.point.x),
                        getYCoordinateFromBoard(stone.point.y),
                        getXCoordinateFromBoard(point.x),
                        getYCoordinateFromBoard(point.y),
                        gridSep / 3f,
                        30f,
                        solidMarkupPaint.apply { color = paint.color }
                    )
                }
            }
        }
    }

    private var touchedStone: ChessStone? = null

    @OptIn(Status.Util::class)
    override fun Pair<Float, Float>.onTouch(currentPieces: List<Piece>): Pair<SgfType.Move?, Int?> =
        touchedStone?.let { touchedStone ->
            toBoardCoordinate().let { (xb, yb) ->
                StandardChessMove(
                    touchedStone,
                    XYPoint(xb, yb)
                ).takeIf { (xb.toFloat() to yb.toFloat()) in onDisplay } to
                        displayedVariations.indexOf(XYPoint(xb, yb)).takeIf { it >= 0 }
            }.also { this@ChessBoard.touchedStone = null }
        } ?: (null to null).also {
            touchedStone =
                currentPieces.find { it.stone?.point == toBoardCoordinate() }?.stone as? ChessStone
        }
}