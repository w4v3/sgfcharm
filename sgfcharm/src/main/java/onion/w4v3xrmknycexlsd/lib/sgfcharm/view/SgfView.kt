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
import onion.w4v3xrmknycexlsd.lib.sgfcharm.GameId
import onion.w4v3xrmknycexlsd.lib.sgfcharm.R
import onion.w4v3xrmknycexlsd.lib.sgfcharm.Status
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.*
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.XYPoint
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType

/** @see SgfView.usePieceDrawer */
typealias SgfPieceDrawer = (canvas: Canvas, piece: Piece, x: Float, y: Float, size: Float, paint: Paint, blackColor: Int, whiteColor: Int) -> Unit

/** @see SgfView.useMarkupDrawer */
typealias SgfMarkupDrawer = (canvas: Canvas, markup: Markup, x: Float, y: Float, xTo: Float, yTo: Float, size: Float, paint: Paint, blackColor: Int, whiteColor: Int, markupColor: Int) -> Unit

/** @see SgfView.useInfoTextMaker */
typealias SgfInfoTextMaker = (nodeInfos: List<NodeInfo>, lastMoveInfo: MoveInfo?) -> CharSequence

/**
 * Draws an interactive Board together with undo/redo buttons and additional informational text. It's a
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
 * If you need more control, you have the following options:
 * - change the way markup is drawn using [useMarkupDrawer]
 * - change the way the informational text is created from the supplied data with [useInfoTextMaker]
 * - change the way pieces are drawn or implement piece drawing for a game other than Go using
 * [usePieceDrawer]
 * - change the way the board is drawn or implement a board for a game other than Go using [useBoard]
 * - for even more freedom, make a custom view that implements [SgfViewAdapter].
 *
 * You can use this view to implement any kind of board game you wish, but there are some limitations
 * for games with unusual boards:
 * - expansion of the board to only the visible parts when the `VW` property is used is only supported
 * if the game uses [XYPoint] as its [SgfType.Point] implementation; otherwise the whole board will
 * be visible all the time
 * - pieces and board tiles are assumed to have equal width and height, with the value returned by
 * [SgfBoard.tileSize]; if you need pieces with other side ratios, you need to handle that in the
 * [SgfPieceDrawer] manually and the `VW` property will only cover a square portion of the tile
 *
 * @see SgfBoard
 *
 * XML attributes:
 * @property[blackColor] the color of black's pieces.
 * @property[whiteColor] the color of white's pieces.
 * @property[gridColor] the color of the grid.
 * @property[markupColor] the color of the markup.
 * @property[showText] whether or not to display information text.
 * @property[showButtons] whether or not to display undo/redo buttons
 */
public class SgfView(context: Context, attrs: AttributeSet?) : AppCompatTextView(context, attrs),
    SgfViewAdapter {
    // data properties
    /** The current [Piece]s on the board. */
    private var pieces: List<Piece> = listOf()

    /** The [MoveInfo] object from the last move. */
    private var lastMoveInfo: MoveInfo? = null

    /** The [VariationData]s for the currently possible variations. */
    private var variationData: List<VariationData> = listOf()

    /** The current board [Markup]s. */
    private var markups: List<Markup> = listOf()

    /** The [NodeInfo]s from the current node. */
    private var nodeInfos: List<NodeInfo> = listOf()

    /** The number of columns of the board. */
    private var totalColumns = 19

    /** The number of rows of the board. */
    private var totalRows = 19

    /** The number of columns of the currently visible board. */
    private var visibleColumns: Int = 19

    /** The number of rows of the currently visible board. */
    private var visibleRows: Int = 19

    // for partial displays, we keep track of which part is actually displayed
    // in terms of coordinates
    private val onDisplay = Rect(1, 1, visibleColumns, visibleRows)

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

    private val varPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var boardWidth = 0 // width of the board
    private var boardHeight = 0 // height of the board
    private var buttonSize = 0 // side length of button
    private var gridSep = 0 // vertical distance between two lines on the grid
    private var belowButtonSep = 0 // space between buttons and text
    private lateinit var undoRect: Rect // rectangle representing the undo button
    private lateinit var redoRect: Rect // rectangle representing the redo button
    private var staticText: StaticLayout? = null // for multiline text

    private var gameId: Int = 1
        set(value) {
            field = value
            pieceDrawer = pieceDrawingStrategies[value] ?: GoPieceDrawer
            board = boardStrategies[value] ?: GoBoard
        }

    private var board: SgfBoard = GoBoard
    private var pieceDrawer: SgfPieceDrawer = GoPieceDrawer

    /** The [SgfInputListener] to trigger in response to touch events. */
    private var inputListener: SgfInputListener? = null

    public override fun registerInputListener(listener: SgfInputListener) {
        inputListener = listener
    }

    init {
        setupAttributes(attrs)
        gridPaint.color = gridColor
    }

    public override fun onReceiveSgfData(data: List<SgfData>) {
        val tmpPieces = mutableListOf<Piece>()
        val tmpVariationInfos = mutableListOf<VariationData>()
        val tmpNodeInfos = mutableListOf<NodeInfo>()
        val tmpMarkups = mutableListOf<Markup>()
        lastMoveInfo = null

        for (d in data) {
            when (d) {
                is Piece -> tmpPieces.add(d)
                is NodeInfo -> tmpNodeInfos.add(d)
                is Markup -> tmpMarkups.add(d)
                is MoveInfo -> lastMoveInfo = d
                is GameConfig -> d.let {
                    gameId = it.gameId; totalColumns = it.columns; totalRows = it.rows
                }
                is VariationData -> tmpVariationInfos.add(d)
            }
        }

        pieces = tmpPieces
        variationData = tmpVariationInfos
        nodeInfos = tmpNodeInfos
        markups = tmpMarkups

        // determining which part of the board is visible -- only for XYPoint based boards
        // by looking which parts are just marked up as visible
        // and taking one more step into that direction for "fade out" grid effect
        // but still within the actual board size
        markups.filter { it.type == MarkupType.VISIBLE }
            .mapNotNull { it.point as? XYPoint }
            .let {
                onDisplay.apply {
                    left = it.minBy { it.x }?.x ?: 1
                    top = it.minBy { it.y }?.y ?: 1
                    right = it.maxBy { it.x }?.x ?: totalColumns
                    bottom = it.maxBy { it.y }?.y ?: totalRows
                }
            }

        visibleColumns = onDisplay.right - onDisplay.left + 1
        visibleRows = onDisplay.bottom - onDisplay.top + 1

        // adapt sizes if board spec has changed to prevent glitching
        //boardWidth = (boardWidth * (oldCols + 1) * (gridColumns)) / ((oldCols) * (gridColumns + 1))
        resetSizes(boardWidth + paddingLeft + paddingRight)

        invalidate()
    }

    /**
     * Draws the view.
     *
     * First, it draws the grid representing the board, then the [Piece] and [Markup] data using the
     * supplied (or default) drawing components, then undo and redo buttons, and finally, the text
     * obtained from the [NodeInfo] data using the [SgfInfoTextMaker].
     *
     * @see useBoard
     * @see usePieceDrawer
     * @see useMarkupDrawer
     * @see useInfoTextMaker
     */
    public override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.run {
            with(board) {
                withTranslation(paddingLeft.toFloat(), paddingTop.toFloat()) {
                    drawBoard(totalColumns, totalRows, onDisplay, gridPaint)
                    drawPieces()
                    drawMarkup()
                    drawVariations(variationData, varPaint.apply {
                        color = markupColor
                        style = paint.style
                        textSize = paint.textSize
                    })
                }
            }
            if (showButtons) drawButtons()
            if (showText) drawText()
        }
    }

    private fun Canvas.drawPieces() = with(board) {
        pieces
            .map {
                it.stone?.point?.toScreenCoordinate()?.let { (x, y) ->
                    pieceDrawer(
                        this@drawPieces,
                        it,
                        x,
                        y,
                        gridSep.toFloat(),
                        piecePaint,
                        blackColor,
                        whiteColor
                    )
                }
            }
    }

    private fun Canvas.drawMarkup() {
        for (markup in markups) {
            val from = markup.point
            val to = markup.to
            with(board) {
                val (x, y) = from.toScreenCoordinate()
                val (x2, y2) = to?.toScreenCoordinate() ?: (0f to 0f)

                markupDrawer(
                    this@drawMarkup,
                    markup,
                    x,
                    y,
                    x2,
                    y2,
                    gridSep.toFloat(),
                    paint,
                    blackColor,
                    whiteColor,
                    markupColor
                )
            }
        }

        val visibles = markups.filter { it.type == MarkupType.VISIBLE }.map { it.point }
        if (visibles.isNotEmpty()) {
            with(board) {
                (getAllPoints(totalColumns, totalRows).toSet().minus(visibles)).map {
                    it.toScreenCoordinate().let { (x, y) ->
                        drawRect(
                            x - gridSep / 2,
                            y - gridSep / 2,
                            x + gridSep / 2,
                            y + gridSep / 2,
                            piecePaint.apply {
                                color = this@SgfView.getBackgroundColor() ?: Color.WHITE
                                style = Paint.Style.FILL
                            })
                    }
                }
            }
        }
    }

    // get the background actually in use by traversing up the view hierarchy
    // this is why I love kotlin
    private tailrec fun View.getBackgroundColor(): Int? =
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

        drawRect(undoRect, varPaint.apply { color = blackColor })
        drawRect(redoRect, varPaint.apply { color = whiteColor })

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
        val text = infoTextMaker(nodeInfos, lastMoveInfo)
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
            paddingLeft.toFloat(),
            paddingTop.toFloat() + boardHeight + (if (showButtons) buttonSize else 0) + belowButtonSep
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
        boardWidth = w - paddingLeft - paddingRight
        boardHeight = board.setBoard(onDisplay, boardWidth)
        gridSep = board.tileSize
        boardHeight += if (gridSep / 2 <= w / 16) (w / 16 - gridSep / 2) else gridSep / 10 // a little extra bottom padding

        buttonSize = w / 8 // should be invariant wrt col and row count
        val buttonSep = buttonSize / 2 // space between the buttons
        belowButtonSep = buttonSep
        undoRect = Rect().apply {
            left += paddingLeft + (boardWidth - buttonSep) / 2 - buttonSize
            top += paddingTop + boardHeight
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
        paddingTop + boardHeight + (if (showButtons) buttonSize else 0) + belowButtonSep +
                (if (showText) staticText?.height ?: 0 else 0) + paddingBottom

    /**
     * Categorizes touch events (`ACTION_UP` only) into moves carried out, undo button and redo button,
     * and triggers the corresponding event of the registered [SgfInputListener].
     *
     * Touch events targeting the board region of the view are passed to [SgfBoard.onTouch] to obtain
     * the corresponding move.
     */
    @SuppressLint("ClickableViewAccessibility")
    public override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            if (event.y <= paddingTop + boardHeight) {
                with(board) {
                    (event.x - paddingLeft to event.y - paddingTop).onTouch(pieces)
                        .let { (move, variation) ->
                            move?.let { inputListener?.onMove(move, variation) }
                        }
                }
            } else if (showButtons && (event.x to event.y) in redoRect) {
                inputListener?.onRedo()
            } else if (showButtons && (event.x to event.y) in undoRect) {
                inputListener?.onUndo()
            }

            return true
        } else if (event?.action == MotionEvent.ACTION_DOWN) return true

        return super.onTouchEvent(event)
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

        private val boardStrategies: MutableMap<Int, SgfBoard> = mutableMapOf(
            GameId.GO to GoBoard
        )

        private val pieceDrawingStrategies: MutableMap<Int, SgfPieceDrawer> = mutableMapOf(
            GameId.GO to GoPieceDrawer
        )

        private var markupDrawer: SgfMarkupDrawer =
            DefaultMarkupDrawer
        private var infoTextMaker: SgfInfoTextMaker =
            DefaultInfoTextMaker

        /**
         * Makes the [SgfView] use the given [board] to handle the drawing the board
         * specific to the game of type [gameId].
         *
         * If no [SgfBoard] implementation is found for this game, the [GoBoard] is used.
         *
         * @see GameId
         */
        public fun useBoard(gameId: Int, board: SgfBoard) {
            boardStrategies[gameId] = board
        }

        /**
         * Makes the [SgfView] use the given [pieceDrawer] to handle the drawing of the pieces
         * specific to the game of type [gameId].
         *
         * If no [SgfPieceDrawer] implementation is found for this game, the [GoPieceDrawer] is used.
         *
         * @see GameId
         */
        public fun usePieceDrawer(gameId: Int, pieceDrawer: SgfPieceDrawer) {
            pieceDrawingStrategies[gameId] = pieceDrawer
        }

        /**
         * Makes the [SgfView] use the given [markupDrawer] to draw the [Markup] to the [Canvas]
         * at screen coordinates `x` and `y`, with `xTo` and `yTo` if the markup type uses
         * two coordinates, using the `paint`.
         *
         * The `size` parameter is the distance between two lines on the board. The passed `paint` is
         * the text paint used by the [SgfView].
         *
         * Note that you can *not* use this to override the drawing behavior for markup of type
         * [MarkupType.VISIBLE]. This will always draw a square in the background color over the invisible
         * parts of the board, no matter what you specify here.
         *
         * The used default is [DefaultMarkupDrawer].
         */
        public fun useMarkupDrawer(markupDrawer: SgfMarkupDrawer) {
            this.markupDrawer = markupDrawer
        }

        /**
         * Makes the [SgfView] use the given [infoTextMaker] to format the [NodeInfo]s and the [MoveInfo]
         * from the last move into a string. Note that the [MoveInfo] can be `null` if there were no moves so far.
         *
         * The used default is [DefaultInfoTextMaker].
         */
        public fun useInfoTextMaker(infoTextMaker: SgfInfoTextMaker) {
            this.infoTextMaker = infoTextMaker
        }
    }
}

/** Allows infix usage of `in` to check if the [point] pair is in this [Rect], including the bounds. */
@Status.Util
public infix operator fun Rect.contains(point: Pair<Float, Float>): Boolean =
    point.first >= left && point.first <= right && point.second >= top && point.second <= bottom
