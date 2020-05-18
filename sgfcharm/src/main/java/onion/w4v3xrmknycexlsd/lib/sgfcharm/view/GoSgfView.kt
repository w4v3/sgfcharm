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

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.withTranslation
import onion.w4v3xrmknycexlsd.lib.sgfcharm.R
import onion.w4v3xrmknycexlsd.lib.sgfcharm.Status
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.*
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

/**
 * Draws the Board together with undo/redo buttons and additional informational text. It's a
 * `TextView`, so the text it displays is styled according to the `TextView` attributes.
 *
 * The layout will be as follows: The grid is drawn with an additional padding to account for
 * pieces at the borders of the board. Below the grid, there are undo and redo buttons, square
 * shaped, in the size of half the space between two lines on the grid. Directly underneath, the
 * text is drawn using the `android:text*` attributes.
 *
 * For best results, you should not set the height of the view to an absolute value, use
 * `MATCH_PARENT` or `WRAP_CONTENT` instead. Otherwise, the view might be clipped.
 *
 * If you need more control, consider setting the [sgfDrawer] property to a custom implementation of
 * [SgfDrawer], or making a custom view that implements [SgfView].
 *
 * @see SgfDrawer
 *
 * XML attributes:
 * @property[blackColor] the color of black's pieces.
 * @property[whiteColor] the color of white's pieces.
 * @property[gridColor] the color of the grid.
 * @property[markupColor] the color of the markup.
 * @property[showText] whether or not to display information text.
 * @property[showButtons] whether or not to display undo/redo buttons
 */
public class GoSgfView(context: Context, attrs: AttributeSet?) : AppCompatTextView(context, attrs),
    SgfView {
    // data properties
    /** The current [Piece]s on the board. */
    private var pieces: List<Piece> = listOf()

    /** The [NodeInfo]s from the current node. */
    private var nodeInfos: List<NodeInfo> = listOf()

    /** The current board [Markup]s. */
    private var markups: List<Markup> = listOf()

    /** The [MoveInfo] object from the last move. */
    private var lastMoveInfo: MoveInfo? = null

    /** The number of columns of the currently visible board.. */
    private var gridColumns: Int = 19

    /** The number of rows of the currently visible board. */
    private var gridRows: Int = 19

    // the true values, irrespective of which part is shown
    private var trueColumns = 19
    private var trueRows = 19

    // for partial displays, we keep track of which part is actually displayed
    // in terms of coordinates
    private val onDisplay = Rect(1, 1, gridColumns, gridRows)

    // XML properties
    @Status.Api
    public var blackColor: Int =
        DEFAULT_BLACK_COLOR

    @Status.Api
    public var whiteColor: Int =
        DEFAULT_WHITE_COLOR

    @Status.Api
    public var gridColor: Int =
        DEFAULT_GRID_COLOR

    @Status.Api
    public var markupColor: Int =
        DEFAULT_MARKUP_COLOR

    @Status.Api
    public var showText: Boolean =
        DEFAULT_SHOW_TEXT

    @Status.Api
    public var showButtons: Boolean =
        DEFAULT_SHOW_BUTTONS

    // drawing properties
    /**
     * The paint used to draw the pieces.
     *
     * Note that the color will be set to [blackColor] or [whiteColor] before actually drawing a piece.
     * */
    @Status.Api
    public val piecePaint: Paint =
        Paint(Paint.ANTI_ALIAS_FLAG) // for pieces and the grid, to keep paint from TextView preserved

    /** The paint used to draw the grid. */
    @Status.Api
    public val gridPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // for piece outlines and markup
    // only need to allocate one paint object this way
    private val varPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val solidMarkupPaint: Paint
        get() = varPaint.apply {
            color = markupColor
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = gridSep * 0.1f
        }
    private val strokeMarkupPaint: Paint
        get() = varPaint.apply {
            color = markupColor
            style = Paint.Style.STROKE
            strokeWidth = gridSep * 0.1f
        }

    private var boardWidth = 0 // width of the board
    private var boardHeight = 0 // height of the board
    private var gridSep = 0 // distance between two lines on the grid
    private var boardPadding = 0 // additional padding
    private var pieceSize = 0f // radius of circle
    private var buttonSize = 0 // side length of button
    private var aboveButtonSep = 0 // space between grid and buttons
    private var belowButtonSep = 0 // space between buttons and text
    private lateinit var undoRect: Rect // rectangle representing the undo button
    private lateinit var redoRect: Rect // rectangle representing the redo button
    private var staticText: StaticLayout? = null // for multiline text

    /**
     * The [SgfDrawer] used to draw pieces and markup.
     *
     * Defaults to [DefaultSgfDrawer].
     */
    @Status.Beta
    public var sgfDrawer: SgfDrawer = DefaultSgfDrawer()

    /** The [SgfInputListener] to trigger in response to touch events. */
    private var inputListener: SgfInputListener? = null

    override fun registerInputListener(listener: SgfInputListener) {
        inputListener = listener
    }

    init {
        setupAttributes(attrs)
        gridPaint.color = gridColor
        varPaint.color = markupColor
    }

    override fun onReceiveSgfData(data: List<SgfData>) {
        val tmpPieces = mutableListOf<Piece>()
        val tmpNodeInfos = mutableListOf<NodeInfo>()
        val tmpMarkups = mutableListOf<Markup>()
        lastMoveInfo = null
        val oldCols = gridColumns // saving for recalculating layout afterwards

        for (d in data) {
            when (d) {
                is Piece -> tmpPieces.add(d)
                is NodeInfo -> tmpNodeInfos.add(d)
                is Markup -> tmpMarkups.add(d)
                is MoveInfo -> lastMoveInfo = d
                is BoardConfig -> d.let { trueColumns = it.columns; trueRows = it.rows }
            }
        }

        pieces = tmpPieces
        nodeInfos = tmpNodeInfos
        markups = tmpMarkups

        // determining which part of the board is visible
        // by looking which parts are just marked up as visible
        // and taking one more step into that direction for "fade out" grid effect
        // but still within the actual board size
        markups.filter { it.type == MarkupType.VISIBLE }
            .mapNotNull { it.point as? SgfType.XYPoint }
            .let {
                onDisplay.apply {
                    left = it.minBy { it.x }?.x ?: 1
                    top = it.minBy { it.y }?.y ?: 1
                    right = it.maxBy { it.x }?.x ?: trueColumns
                    bottom = it.maxBy { it.y }?.y ?: trueRows
                }
            }

        gridColumns = onDisplay.right - onDisplay.left + 1
        gridRows = onDisplay.bottom - onDisplay.top + 1

        // adapt sizes if board spec has changed to prevent glitching
        boardWidth = (boardWidth * oldCols * (gridColumns - 1)) / ((oldCols - 1) * gridColumns)
        gridSep = boardWidth / (gridColumns - 1)
        boardHeight = gridSep * (gridRows - 1)
        boardPadding = gridSep / 2
        pieceSize = gridSep * 0.45f

        invalidate()
    }

    /**
     * Draws the view.
     *
     * First, it draws the grid representing the board, then the [Piece] and [Markup] data using the
     * [SgfDrawer], then undo and redo buttons, and finally, the text obtained from the [NodeInfo] data
     * using [SgfDrawer.makeInfoText].
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.run {
            drawGrid()
            drawPieces()
            drawMarkup()
            if (showButtons) drawButtons()
            if (showText) drawText()
        }
    }

    private fun Canvas.drawGrid() {
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
                gridPaint
            )
        }

        for (col in onDisplay.left..onDisplay.right) {
            drawLine(
                getXCoordinateFromBoard(col),
                getYCoordinateFromBoard(fromy),
                getXCoordinateFromBoard(col),
                getYCoordinateFromBoard(toy),
                gridPaint
            )
        }
    }

    private fun Canvas.drawPieces() = pieces.map { drawPiece(it) }

    private fun Canvas.drawPiece(piece: Piece) {
        val point = piece.stone?.point
        if (point !is SgfType.XYPoint) return

        piecePaint.color = when (piece.color) {
            SgfType.Color.Value.BLACK -> blackColor
            SgfType.Color.Value.WHITE -> whiteColor
        }

        with(sgfDrawer) { // check if custom drawer absorbs it
            if (drawPiece(
                    piece,
                    getXCoordinateFromBoard(point.x),
                    getYCoordinateFromBoard(point.y),
                    pieceSize * 2,
                    piecePaint
                )
            )
                return
        }

        drawCircle(
            getXCoordinateFromBoard(point.x),
            getYCoordinateFromBoard(point.y),
            pieceSize,
            piecePaint
        )

        // draw border
        if (whiteColor == Color.WHITE) {
            varPaint.apply {
                color = Color.BLACK
                style = Paint.Style.STROKE
                strokeWidth = pieceSize * 0.15f
            }
            drawCircle(
                getXCoordinateFromBoard(point.x),
                getYCoordinateFromBoard(point.y),
                pieceSize,
                varPaint
            )
        }
    }

    private fun Canvas.drawMarkup() {
        for (markup in markups) {
            val from = markup.point as? SgfType.XYPoint ?: continue
            val to = markup.to as? SgfType.XYPoint
            val x = getXCoordinateFromBoard(from.x)
            val y = getYCoordinateFromBoard(from.y)
            val x2 = getXCoordinateFromBoard(to?.x ?: 0)
            val y2 = getYCoordinateFromBoard(to?.y ?: 0)

            val absorbed = with(sgfDrawer) {
                drawMarkup(markup, x, y, x2, y2, pieceSize * 2, paint)
            }
            if (absorbed) continue

            when (markup.type) {
                MarkupType.VARIATION -> drawText(
                    markup.label ?: "",
                    x - markup.label.width(paint) / 2,
                    y + markup.label.height(paint) / 2,
                    varPaint.apply {
                        color = markupColor
                        style = paint.style
                        textSize = paint.textSize
                    }
                )
                MarkupType.ARROW -> drawArrow(x, y, x2, y2, gridSep / 2f, 35f, solidMarkupPaint)
                MarkupType.CIRCLE -> drawCircle(x, y, pieceSize * 6 / 10, strokeMarkupPaint)
                MarkupType.DIM -> drawRect(
                    x - gridSep / 2,
                    y - gridSep / 2,
                    x + gridSep / 2,
                    y + gridSep / 2,
                    varPaint.apply {
                        color = Color.argb(200, 255, 255, 255)
                        style = Paint.Style.FILL_AND_STROKE
                        strokeWidth = 0.05f * gridSep
                    })
                MarkupType.LABEL -> {
                    val wd = markup.label.width(paint)
                    val ht = markup.label.height(paint)
                    drawRect(
                        x - wd / 2,
                        y - ht / 2,
                        x + wd / 2,
                        y + ht / 2,
                        varPaint.apply {
                            color = this@GoSgfView.getBackgroundColor() ?: Color.WHITE
                            style = Paint.Style.FILL_AND_STROKE
                            strokeWidth = 0.05f * gridSep
                        })
                    drawText(
                        markup.label ?: "",
                        x - wd / 2,
                        y + ht / 2,
                        paint
                    )
                }
                MarkupType.LINE -> drawLine(x, y, x2, y2, varPaint.apply {
                    strokeWidth = gridSep * 0.1f
                    style = Paint.Style.FILL
                    color = markupColor
                })
                MarkupType.X -> {
                    drawLine(
                        x - gridSep * 0.25f,
                        y - gridSep * 0.25f,
                        x + gridSep * 0.25f,
                        y + gridSep * 0.25f,
                        strokeMarkupPaint
                    )
                    drawLine(
                        x - gridSep * 0.25f,
                        y + gridSep * 0.25f,
                        x + gridSep * 0.25f,
                        y - gridSep * 0.25f,
                        strokeMarkupPaint
                    )
                }
                MarkupType.SELECT -> drawCircle(x, y, pieceSize, strokeMarkupPaint)
                MarkupType.SQUARE -> drawRect(
                    x - gridSep * 0.25f,
                    y - gridSep * 0.25f,
                    x + gridSep * 0.25f,
                    y + gridSep * 0.25f,
                    strokeMarkupPaint
                )
                MarkupType.TRIANGLE -> drawTriangle(
                    x.toInt(),
                    y.toInt(),
                    gridSep * 5 / 10,
                    strokeMarkupPaint
                )
                MarkupType.VISIBLE -> {
                }
                MarkupType.BLACK_TERRITORY -> drawRect(
                    x - gridSep * 0.25f,
                    y - gridSep * 0.25f,
                    x + gridSep * 0.25f,
                    y + gridSep * 0.25f,
                    piecePaint.apply { color = blackColor })
                MarkupType.WHITE_TERRITORY -> drawRect(
                    x - gridSep * 0.25f,
                    y - gridSep * 0.25f,
                    x + gridSep * 0.25f,
                    y + gridSep * 0.25f,
                    piecePaint.apply { color = whiteColor })
            }
        }

        val visibles = markups.filter { it.type == MarkupType.VISIBLE }
            .mapNotNull { it.point as? SgfType.XYPoint }
            .map { (x, y) -> x to y }
        if (visibles.isNotEmpty()) {
            val board =
                (1..trueColumns).flatMap { x -> (1..trueRows).map { y -> x to y } }
            (board.toSet().minus(visibles)).map { (xb, yb) ->
                getXCoordinateFromBoard(xb).let { x ->
                    getYCoordinateFromBoard(yb).let { y ->
                        drawRect(
                            x - gridSep / 2,
                            y - gridSep / 2,
                            x + gridSep / 2,
                            y + gridSep / 2,
                            varPaint.apply {
                                color = this@GoSgfView.getBackgroundColor() ?: Color.WHITE
                                style = Paint.Style.FILL
                            })
                    }
                }
            }
        }
    }

    // get the background actually in use by traversing up the view hierarchy
    // this is why I love kotlin
    private fun View.getBackgroundColor(): Int? =
        (background as? ColorDrawable)?.color
            ?: (parent as? View)?.getBackgroundColor()

    private fun Canvas.drawButtons() {
        val undo = ContextCompat.getDrawable(
            context,
            R.drawable.ic_undo_white_24dp
        )
        val redo = ContextCompat.getDrawable(
            context,
            R.drawable.ic_redo_black_24dp
        )

        drawRect(undoRect, piecePaint.apply { color = blackColor })
        drawRect(redoRect, piecePaint.apply { color = whiteColor })

        // draw borders
        if (whiteColor == Color.WHITE) {
            varPaint.apply {
                color = Color.BLACK
                style = Paint.Style.STROKE
                strokeWidth = undoRect.width() * 0.07f
            }

            drawRect(undoRect, varPaint)
            drawRect(redoRect, varPaint)

        }

        undo?.bounds = undoRect
        redo?.bounds = redoRect

        undo?.draw(this)
        redo?.draw(this)
    }

    private fun Canvas.drawText() {
        val text = sgfDrawer.makeInfoText(nodeInfos, lastMoveInfo)
        staticText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder.obtain(text, 0, text.length, paint, boardWidth)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(0f, 1.1f)
                .setIncludePad(false)
                .build()
        } else {
            @Suppress("DEPRECATION")
            StaticLayout(
                text,
                paint,
                boardWidth,
                Layout.Alignment.ALIGN_NORMAL,
                1.1f,
                0f,
                false
            )
        }

        withTranslation(
            paddingLeft + boardPadding.toFloat(),
            paddingTop + boardPadding.toFloat() + boardHeight + (if (showButtons) aboveButtonSep + buttonSize else 0) + belowButtonSep
        ) {
            staticText?.draw(this)
        }

        requestLayout() // dimensions might have changed
    }

    /**
     * Measures the view by using the requested with and then requesting a height,
     * including the dimensions of the text currently displayed.
     */
    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // we try to fit the view into the smaller one of the requested dimensions
        val w = MeasureSpec.getSize(widthMeasureSpec)
        resetSizes(w)

        // we request the optimal height arising from the given width
        val h = View.resolveSizeAndState(calculateHeight(), heightMeasureSpec, 0)

        setMeasuredDimension(w, h)
    }

    // recalculate layout quantities from new width
    private fun resetSizes(w: Int) {
        // board size leaves room for half a column on each side => 1 additional column in total
        boardWidth = (w - paddingLeft - paddingRight) / gridColumns * (gridColumns - 1)
        gridSep = boardWidth / (gridColumns - 1)
        boardHeight = gridSep * (gridRows - 1)
        boardPadding = gridSep / 2
        pieceSize = gridSep * 0.45f

        buttonSize = w / 8 // should be invariant wrt col and row count
        // ideally this is also invariant but stone size depends on col/row count, so we need to choose
        // at least gridSep / 2 to avoid overlaps with stones
        aboveButtonSep = max(gridSep / 2 * 11 / 10, buttonSize / 2)
        val buttonSep = buttonSize / 2 // space between the buttons
        belowButtonSep = buttonSep
        undoRect = Rect().apply {
            left += paddingLeft + boardPadding + (boardWidth - buttonSep) / 2 - buttonSize
            top += paddingTop + boardPadding + boardHeight + aboveButtonSep
            right = left + buttonSize
            bottom = top + buttonSize
        }
        redoRect = Rect().apply {
            left = undoRect.right + buttonSep
            top = undoRect.top
            right = left + buttonSize
            bottom = top + buttonSize
        }
    }

    private fun calculateHeight() =
        paddingTop + boardPadding + boardHeight + (if (showButtons) aboveButtonSep +
                buttonSize else 0) + belowButtonSep + (if (showText) staticText?.height
            ?: 0 else 0) + paddingBottom

    /**
     * Categorizes touch events (`ACTION_UP` only) into moves carried out, undo button and redo button,
     * and triggers the corresponding event of the registered [SgfInputListener].
     */
    @SuppressLint("ClickableViewAccessibility")
    public override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            val x = getXCoordinateFromScreen(event.x)
            val y = getYCoordinateFromScreen(event.y)

            if (
                round(x) >= onDisplay.left && round(x) <= onDisplay.right &&
                round(y) >= onDisplay.top && round(y) <= onDisplay.bottom
            ) {
                inputListener?.onMove(SgfType.XYMove(round(x).toInt(), round(y).toInt()))
            } else if (showButtons && redoRect.isIn(event.x, event.y)) {
                inputListener?.onRedo()
            } else if (showButtons && undoRect.isIn(event.x, event.y)) {
                inputListener?.onUndo()
            }

            return true
        } else if (event?.action == MotionEvent.ACTION_DOWN) return true

        return super.onTouchEvent(event)
    }

    private fun Rect.isIn(x: Float, y: Float) = x >= left && x <= right && y >= top && y <= bottom


    // convert between coordinates on board and on screen
    private val getXCoordinateFromBoard = { col: Int ->
        paddingLeft + boardPadding.toFloat() +
                (col - onDisplay.left) * gridSep
    }
    private val getYCoordinateFromBoard = { row: Int ->
        paddingTop + boardPadding.toFloat() +
                (row - onDisplay.top) * gridSep
    }
    private val getXCoordinateFromScreen = { x: Float ->
        (x - paddingLeft - boardPadding.toFloat()) /
                gridSep + onDisplay.left
    }
    private val getYCoordinateFromScreen = { y: Float ->
        (y - paddingTop - boardPadding.toFloat()) /
                gridSep + onDisplay.top
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SgfView, 0, 0
        ).apply {
            try {
                blackColor = getColor(
                    R.styleable.SgfView_blackColor,
                    DEFAULT_BLACK_COLOR
                )
                whiteColor = getColor(
                    R.styleable.SgfView_whiteColor,
                    DEFAULT_WHITE_COLOR
                )
                gridColor = getColor(
                    R.styleable.SgfView_gridColor,
                    DEFAULT_GRID_COLOR
                )
                markupColor = getColor(
                    R.styleable.SgfView_markupColor,
                    DEFAULT_MARKUP_COLOR
                )
                showText = getBoolean(
                    R.styleable.SgfView_showText,
                    DEFAULT_SHOW_TEXT
                )
                showButtons = getBoolean(
                    R.styleable.SgfView_showButtons,
                    DEFAULT_SHOW_BUTTONS
                )
            } finally {
                recycle()
            }
        }
    }

    companion object {
        private const val DEFAULT_BLACK_COLOR = Color.BLACK
        private const val DEFAULT_WHITE_COLOR = Color.WHITE
        private const val DEFAULT_GRID_COLOR = Color.LTGRAY
        private const val DEFAULT_MARKUP_COLOR = Color.BLUE
        private const val DEFAULT_SHOW_TEXT = true
        private const val DEFAULT_SHOW_BUTTONS = true
    }
}