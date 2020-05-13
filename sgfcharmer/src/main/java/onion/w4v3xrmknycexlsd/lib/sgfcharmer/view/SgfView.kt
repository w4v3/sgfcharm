package onion.w4v3xrmknycexlsd.lib.sgfcharmer.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.StaticLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.withTranslation
import androidx.core.text.bold
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.R
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.handle.*
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.parse.SgfType
import kotlin.math.*


/**
 * Draws the Board together with undo/redo buttons and additional informational text. It's a
 * `TextView`, so the text it displays is styled according to the `TextView` attributes.
 *
 * @property[pieces] a list of [Piece]s currently displayed.
 * @property[nodeInfos] a list of additional [NodeInfo]s to be shown.
 * @property[gridColumns] number of columns on the board.
 * @property[gridRows] number of rows on the board.
 * @property[sgfDrawer] the [SgfDrawer] used to draw pieces and markup
 *
 * The layout will be as follows: The grid is drawn with an additional padding to account for
 * pieces at the borders of the board. Below the grid, there are undo and redo buttons, square
 * shaped, in the size of half the space between two lines on the grid. Directly underneath, the
 * text is drawn with the `android:text*` attributes.
 *
 * For best results, you should not set the height of the view to an absolute value, use
 * `MATCH_PARENT` or `WRAP_CONTENT` instead. Otherwise, the view might be clipped.
 *
 * XML attributes:
 * @property[blackColor] the color of black's pieces.
 * @property[whiteColor] the color of white's pieces.
 * @property[gridColor] the color of the grid.
 * @property[showText] whether or not to display information text.
 */
class SgfView(context: Context, attrs: AttributeSet?) : AppCompatTextView(context, attrs),
    ISgfView {
    // data properties
    public override var pieces: List<Piece> = listOf()
    public override var nodeInfos: List<NodeInfo> = listOf()
    public override var markups: List<Markup> = listOf()
    public override var lastMoveInfo: MoveInfo? = null

    public override var gridColumns: Int = 19
    public override var gridRows: Int = 19

    // XML properties
    private var blackColor =
        DEFAULT_BLACK_COLOR
    private var whiteColor =
        DEFAULT_WHITE_COLOR
    private var gridColor =
        DEFAULT_GRID_COLOR
    private var showText =
        DEFAULT_SHOW_TEXT

    // drawing properties
    public val piecePaint =
        Paint(Paint.ANTI_ALIAS_FLAG) // for pieces and the grid, to keep paint from TextView preserved
    public val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val varPaint = Paint(Paint.ANTI_ALIAS_FLAG) // for piece outlines and markup
    private var boardSize = 0 // width of the board
    private var gridSep = 0 // distance between two lines on the grid
    private var boardPadding = 0 // additional padding
    private var pieceSize = 0f // radius of circle
    private var buttonSize = 0 // side length of button
    private var buttonSep = 0 // space between the buttons as well as above and below them
    private lateinit var undoRect: Rect // rectangle representing the undo button
    private lateinit var redoRect: Rect // rectangle representing the redo button
    private var staticText: StaticLayout? = null // for multiline text

    public override var inputListener: SgfInputListener? = null
    public var sgfDrawer: SgfDrawer = object : SgfDrawer {}

    init {
        setupAttributes(attrs)

        gridPaint.color = gridColor
    }

    /**
     * Draws the view.
     *
     * First, it draws the grid representing the board, then the [pieces] and [markups] using the
     * [SgfDrawer], then undo and redo buttons, and finally, the text obtained from the [nodeInfos]
     * using [SgfDrawer.makeInfoText].
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            drawGrid()
            drawPieces()
            drawMarkup()
            drawButtons()
            if (showText) drawText()
        }
    }

    private fun Canvas.drawGrid() {
        for (row in 1..gridRows) {
            drawLine(
                getXCoordinateFromBoard(1),
                getYCoordinateFromBoard(row),
                getXCoordinateFromBoard(gridColumns),
                getYCoordinateFromBoard(row),
                gridPaint
            )
        }

        for (col in 1..gridColumns) {
            drawLine(
                getXCoordinateFromBoard(col),
                getYCoordinateFromBoard(1),
                getXCoordinateFromBoard(col),
                getYCoordinateFromBoard(gridColumns),
                gridPaint
            )
        }
    }

    private fun Canvas.drawPieces() = pieces.map { drawPiece(it) }

    private fun Canvas.drawPiece(piece: Piece) {
        val stone = piece.stone
        if (stone !is SgfType.XYStone) return

        piecePaint.color = when (piece.color) {
            SgfType.Color.Value.BLACK -> blackColor
            SgfType.Color.Value.WHITE -> whiteColor
        }

        with(sgfDrawer) {
            if (drawPiece(
                    piece,
                    getXCoordinateFromBoard(stone.point.x),
                    getYCoordinateFromBoard(stone.point.y),
                    pieceSize,
                    piecePaint
                )
            )
                return
        }

        drawCircle(
            getXCoordinateFromBoard(stone.point.x),
            getYCoordinateFromBoard(stone.point.y),
            pieceSize,
            piecePaint
        )
        varPaint.apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 8f
        }
        drawCircle(
            getXCoordinateFromBoard(stone.point.x),
            getYCoordinateFromBoard(stone.point.y),
            pieceSize,
            varPaint
        )
    }

    private fun Canvas.drawMarkup() {
        for (markup in markups) {
            val from = markup.from as? SgfType.XYPoint ?: continue
            val to = markup.to as? SgfType.XYPoint
            val x = getXCoordinateFromBoard(from.x)
            val y = getYCoordinateFromBoard(from.y)
            val x2 = getXCoordinateFromBoard(to?.x ?: 0)
            val y2 = getYCoordinateFromBoard(to?.y ?: 0)

            val absorbed = with(sgfDrawer) {
                drawMarkup(markup, x, y, x2, y2, paint)
            }
            if (absorbed) continue

            when (markup.type) {
                MarkupType.VARIATION -> drawText(
                    markup.label ?: "",
                    x - markup.label.width() / 2,
                    y + markup.label.height() / 2,
                    paint
                )
                MarkupType.ARROW -> drawArrow(x, y, x2, y2, paint)
                MarkupType.CIRCLE -> drawCircle(x, y, pieceSize * 7 / 10, paint)
                MarkupType.DIM -> drawRect(
                    x - gridSep / 2,
                    y - gridSep / 2,
                    x + gridSep / 2,
                    y + gridSep / 2,
                    piecePaint.apply { color = Color.argb(100, 255, 255, 255) })
                MarkupType.LABEL -> drawText(
                    markup.label ?: "",
                    x - markup.label.width() / 2,
                    y + markup.label.height() / 2,
                    paint
                )
                MarkupType.LINE -> drawLine(x, y, x2, y2, paint)
                MarkupType.X -> drawText("X", x - "X".width() / 2, y + "X".height() / 2, paint)
                MarkupType.SELECT -> drawCircle(x, y, pieceSize, paint)
                MarkupType.SQUARE -> drawRect(
                    x - gridSep * 0.3f,
                    y - gridSep * 0.3f,
                    x + gridSep * 0.3f,
                    y + gridSep * 0.3f,
                    paint
                )
                MarkupType.TRIANGLE -> drawTriangle(
                    x.toInt(),
                    y.toInt() - gridSep / 3,
                    gridSep * 6 / 10,
                    paint
                )
                MarkupType.VISIBLE -> {
                }
                MarkupType.BLACK_TERRITORY -> drawRect(
                    x - gridSep * 0.3f,
                    y - gridSep * 0.3f,
                    x + gridSep * 0.3f,
                    y + gridSep * 0.3f,
                    piecePaint.apply { color = blackColor })
                MarkupType.WHITE_TERRITORY -> drawRect(
                    x - gridSep * 0.3f,
                    y - gridSep * 0.3f,
                    x + gridSep * 0.3f,
                    y + gridSep * 0.3f,
                    piecePaint.apply { color = whiteColor })
            }
        }

        val visibles = markups.filter { it.type == MarkupType.VISIBLE }
            .mapNotNull { it.from as? SgfType.XYPoint }
            .map { (x, y) -> x to y }
        if (visibles.isNotEmpty()) {
            val board = (1..gridColumns).flatMap { x -> (1..gridRows).map { y -> x to y } }
            (board.toSet().minus(visibles)).map { (xb, yb) ->
                getXCoordinateFromBoard(xb).let { x ->
                    getYCoordinateFromBoard(yb).let { y ->
                        drawRect(
                            x - gridSep / 2,
                            y - gridSep / 2,
                            x + gridSep / 2,
                            y + gridSep / 2,
                            piecePaint.apply {
                                color = (background as? ColorDrawable)?.color ?: Color.WHITE
                            })
                    }
                }
            }
        }
    }

    private fun Canvas.drawArrow(
        from_x: Float,
        from_y: Float,
        to_x: Float,
        to_y: Float,
        paint: Paint
    ) {
        val anglerad: Float

        //values to change for other appearance *CHANGE THESE FOR OTHER SIZE ARROWHEADS*
        val radius = 10f
        val angle = 15f

        //some angle calculations
        anglerad = (PI.toFloat() * angle / 180.0f)
        val lineangle: Float = atan2(to_y - from_y, to_x - from_x)

        //tha line
        drawLine(from_x, from_y, to_x, to_y, paint)

        //tha triangle
        val path = Path()
        path.fillType = Path.FillType.EVEN_ODD
        path.moveTo(to_x, to_y)
        path.lineTo(
            (to_x - radius * cos(lineangle - anglerad / 2.0)).toFloat(),
            (to_y - radius * sin(lineangle - anglerad / 2.0)).toFloat()
        )
        path.lineTo(
            (to_x - radius * cos(lineangle + anglerad / 2.0)).toFloat(),
            (to_y - radius * sin(lineangle + anglerad / 2.0)).toFloat()
        )
        path.close()
        drawPath(path, paint)
    }

    private fun Canvas.drawTriangle(topx: Int, topy: Int, sidelength: Int, paint: Paint) {
        val a = Point(topx, topy)
        // y middle point of base: dy = sl sqrt(3)/2
        val b = Point(topx - sidelength / 2, topy + (sidelength * sqrt(3f) / 2).toInt())
        val c = Point(topx + sidelength / 2, topy + (sidelength * sqrt(3f) / 2).toInt())
        val path = Path()
        path.moveTo(a.x.toFloat(), a.y.toFloat())
        path.lineTo(b.x.toFloat(), b.y.toFloat())
        path.lineTo(c.x.toFloat(), c.y.toFloat())
        path.lineTo(a.x.toFloat(), a.y.toFloat())
        drawPath(path, paint)
    }

    // getting dimensions of text when printed
    private var txtBounds = Rect()
    private fun String?.width(): Int {
        paint.getTextBounds(this, 0, (this ?: "").length, txtBounds)
        return txtBounds.width()
    }

    private fun String?.height(): Int {
        paint.getTextBounds(this, 0, (this ?: "").length, txtBounds)
        return txtBounds.height()
    }

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

        undo?.bounds = undoRect
        redo?.bounds = redoRect

        undo?.draw(this)
        redo?.draw(this)
    }

    private fun Canvas.drawText() {
        val text = sgfDrawer.makeInfoText(nodeInfos, lastMoveInfo)
        staticText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder.obtain(text, 0, text.length, paint, boardSize)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(0f, 1.1f)
                .setIncludePad(false)
                .build()
        } else {
            @Suppress("DEPRECATION")
            StaticLayout(
                text,
                paint,
                boardSize,
                Layout.Alignment.ALIGN_NORMAL,
                1.1f,
                0f,
                false
            )
        }

        withTranslation(
            paddingLeft + boardPadding.toFloat(),
            paddingTop + boardPadding.toFloat() + boardSize + buttonSep + buttonSize + buttonSep
        ) {
            staticText?.draw(this)
        }

        requestLayout() // dimensions might have changed
    }

    /**
     * Measures the view by taking the minimum of the requested sizes, using this as the board width
     * and then requesting a height, including the dimensions of the text currently displayed.
     */
    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // we try to fit the view into the smaller one of the requested dimensions
        val w = min(
            MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec)
        )
        resetSizes(w)

        // we request the optimal height arising from the given width
        val h = View.resolveSizeAndState(calculateHeight(), heightMeasureSpec, 0)

        setMeasuredDimension(w, h)
    }

    // recalculate layout quantities from new width
    private fun resetSizes(w: Int) {
        // board size leaves room for half a column on each side => 1 additional column in total
        boardSize = (w - paddingLeft - paddingRight) / gridColumns * (gridColumns - 1)
        gridSep = boardSize / (gridColumns - 1)
        boardPadding = gridSep / 2
        pieceSize = gridSep * 0.47f

        buttonSize = boardSize / 8
        buttonSep = buttonSize / 2
        undoRect = Rect().apply {
            left += paddingLeft + boardPadding + (boardSize - buttonSep) / 2 - buttonSize
            top += paddingTop + boardPadding + boardSize + buttonSep
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

    private fun calculateHeight() = paddingTop + boardPadding + boardSize + buttonSep +
            buttonSize + buttonSep + (if (showText) staticText?.height ?: 0 else 0) + paddingBottom


    /**
     * Categorizes touch events into moves carried out, undo button and redo button, and triggers
     * the corresponding event of the [inputListener].
     */
    @SuppressLint("ClickableViewAccessibility")
    public override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val x = getXCoordinateFromScreen(event.x)
            val y = getYCoordinateFromScreen(event.y)

            if (round(x) >= 1 && round(x) <= gridColumns && round(y) >= 1 && round(y) <= gridRows) {
                inputListener?.onMove(SgfType.XYMove(round(x).toInt(), round(y).toInt()))
            } else if (redoRect.isIn(event.x, event.y)) {
                inputListener?.onRedo()
            } else if (undoRect.isIn(event.x, event.y)) {
                inputListener?.onUndo()
            }
        }

        return super.onTouchEvent(event)
    }

    private fun Rect.isIn(x: Float, y: Float) = x >= left && x <= right && y >= top && y <= bottom


    // convert between coordinates on board and on screen
    private val getXCoordinateFromBoard = { x: Int ->
        paddingLeft + boardPadding.toFloat() +
                (x - 1) * gridSep
    }
    private val getYCoordinateFromBoard = { x: Int ->
        paddingTop + boardPadding.toFloat() +
                (x - 1) * gridSep
    }
    private val getXCoordinateFromScreen = { x: Float ->
        (x - paddingLeft - boardPadding.toFloat()) /
                gridSep + 1
    }
    private val getYCoordinateFromScreen = { x: Float ->
        (x - paddingTop - boardPadding.toFloat()) /
                gridSep + 1
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
                showText = getBoolean(
                    R.styleable.SgfView_showText,
                    DEFAULT_SHOW_TEXT
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
        private const val DEFAULT_SHOW_TEXT = true


        fun defaultMakeInfoText(nodeInfos: List<NodeInfo>, lastMoveInfo: MoveInfo?): CharSequence =
            SpannableStringBuilder().apply {
                lastMoveInfo?.let {
                    (it.lastPlaced.stone as? SgfType.XYStone)?.let { stone ->
                        bold { append("${it.lastPlaced.color} #${it.moveNumber} @ ${stone.point.x}-${stone.point.y}\n") }
                        append("# prisoners black ${it.prisoners.first} white ${it.prisoners.second}\n")
                    }
                }
                nodeInfos.map {
                    when (it.key) {
                        null -> append(it.message + "\n")
                        else -> bold { append(it.key + " ") }.append(it.message + "\n")
                    }
                }
            }
    }
}