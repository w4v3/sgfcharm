package onion.w4v3xrmknycexlsd.lib.sgfview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.withTranslation
import onion.w4v3xrmknycexlsd.lib.sgfview.data.ColorValue
import onion.w4v3xrmknycexlsd.lib.sgfview.data.Piece
import kotlin.math.min
import kotlin.math.round


/**
 * Draws the Board together with undo/redo buttons and additional informational text. It's a
 * `TextView`, so the text it displays is styled according to the `TextView` attributes.
 *
 * @property[pieces] a list of [Piece]s currently displayed.
 * @property[text] the additional text to be shown.
 * @property[gridColumns] number of columns on the board.
 * @property[gridRows] number of rows on the board.
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
 * @property[borderColor] the color of the border of the pieces.
 * @property[showText] whether or not to display information text.
 */
class SgfView(context: Context, attrs: AttributeSet?) : AppCompatTextView(context, attrs) {
    // data properties
    var pieces = listOf<Piece>()
        set(pieces) {
            field = pieces
            invalidate()
        }
    var text = ""
        set(text) {
            field = text
            invalidate()
        }
    var gridColumns = 19
    var gridRows = 19

    // XML properties
    private var blackColor = DEFAULT_BLACK_COLOR
    private var whiteColor = DEFAULT_WHITE_COLOR
    private var gridColor = DEFAULT_GRID_COLOR
    private var borderColor = DEFAULT_BORDER_COLOR
    private var showText = DEFAULT_SHOW_TEXT

    // drawing properties
    private val boardPaint = Paint(Paint.ANTI_ALIAS_FLAG) // for pieces and the grid, to keep paint from TextView preserved
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG) // for piece outlines
    private var boardSize = 0 // width of the board
    private var gridSep = 0 // distance between two lines on the grid
    private var boardPadding = 0 // additional padding
    private var pieceSize = 0f // radius of circle
    private var buttonSize = 0 // side length of button
    private var buttonSep = 0 // space between the buttons as well as above and below them
    private lateinit var undoRect: Rect // rectangle representing the undo button
    private lateinit var redoRect: Rect // rectangle representing the redo button
    private lateinit var staticText: StaticLayout // for multiline text

    var listener: OnTouchListener? = null

    init {
        setupAttributes(attrs)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            drawGrid()
            drawPieces()
            drawButtons()
            if (showText) drawText()
        }
    }

    private fun Canvas.drawGrid() {
        boardPaint.color = gridColor

        for (row in 1..gridRows) {
            drawLine(
                getXCoordinateFromBoard(1),
                getYCoordinateFromBoard(row),
                getXCoordinateFromBoard(gridColumns),
                getYCoordinateFromBoard(row),
                boardPaint
            )
        }

        for (col in 1..gridColumns) {
            drawLine(
                getXCoordinateFromBoard(col),
                getYCoordinateFromBoard(1),
                getXCoordinateFromBoard(col),
                getYCoordinateFromBoard(gridColumns),
                boardPaint
            )
        }
    }

    private fun Canvas.drawPieces() = pieces.map { drawPiece(it) }

    private fun Canvas.drawPiece(piece: Piece) {
        boardPaint.color = when (piece.color.value) {
            ColorValue.BLACK -> blackColor
            ColorValue.WHITE -> whiteColor
        }

        drawCircle(
            getXCoordinateFromBoard(piece.x),
            getYCoordinateFromBoard(piece.y),
            pieceSize,
            boardPaint
        )

        strokePaint.apply {
            color = borderColor
            strokeWidth = pieceSize * 0.05f
            style = Paint.Style.STROKE
        }

        drawCircle(
            getXCoordinateFromBoard(piece.x),
            getYCoordinateFromBoard(piece.y),
            pieceSize,
            strokePaint
        )
    }

    private fun Canvas.drawButtons() {
        val undo = ContextCompat.getDrawable(context, R.drawable.ic_undo_white_24dp)
        val redo = ContextCompat.getDrawable(context, R.drawable.ic_redo_black_24dp)

        drawRect(undoRect, boardPaint.apply { color = blackColor })
        drawRect(redoRect, boardPaint.apply { color = whiteColor })

        undo?.bounds = undoRect
        redo?.bounds = redoRect

        undo?.draw(this)
        redo?.draw(this)
    }

    private fun Canvas.drawText() {
        withTranslation (
            paddingLeft + boardPadding.toFloat(),
            paddingTop + boardPadding.toFloat() + boardSize + buttonSep + buttonSize + buttonSep
        ) {
            staticText.draw(this)
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // we try to fit the view into the smaller one of the requested dimensions
        val w = min(MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec))
        resetSizes(w)

        // we request the optimal height arising from the given width
        val h = View.resolveSizeAndState(calculateHeight(), heightMeasureSpec, 0)

        setMeasuredDimension(w,h)
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

        if (showText) {
            staticText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                StaticLayout.Builder.obtain(text, 0, text.length, paint, boardSize)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setLineSpacing(0f, 1.1f)
                    .setIncludePad(false)
                    .build()
            } else {
                @Suppress("DEPRECATION")
                StaticLayout(text, paint, boardSize, Layout.Alignment.ALIGN_NORMAL, 1.1f, 0f, false)
            }
        }
    }

    private fun calculateHeight() = paddingTop + boardPadding + boardSize + buttonSep +
                buttonSize + buttonSep + (if (showText) staticText.height else 0) + paddingBottom


    /** Interface for communicating touch events from the view to the controller. */
    interface OnTouchListener {
        /** Triggered when user has touched the board at grid coordinates [x] and [y]. */
        fun onMove(x: Int, y: Int)

        /** Triggered when user has touched the undo button. */
        fun onUndo()

        /** Triggered when user has touched the redo button. */
        fun onRedo()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val x = getXCoordinateFromScreen(event.x)
            val y = getYCoordinateFromScreen(event.y)

            if (round(x) >= 1 && round(x) <= gridColumns && round(y) >= 1 && round(y) <= gridRows) {
                listener?.onMove(round(x).toInt(), round(y).toInt())
            } else if (redoRect.isIn(event.x, event.y)) {
                listener?.onRedo()
            } else if (undoRect.isIn(event.x, event.y)) {
                listener?.onUndo()
            }
        }

        return super.onTouchEvent(event)
    }

    private fun Rect.isIn(x: Float, y: Float) = x >= left && x <= right && y >= top && y <= bottom


    // convert between coordinates on board and on screen
    private val getXCoordinateFromBoard = { x: Int -> paddingLeft + boardPadding.toFloat() +
            (x - 1) * gridSep }
    private val getYCoordinateFromBoard = { x: Int -> paddingTop + boardPadding.toFloat() +
            (x - 1) * gridSep }
    private val getXCoordinateFromScreen = { x: Float -> (x - paddingLeft - boardPadding.toFloat()) /
        gridSep + 1 }
    private val getYCoordinateFromScreen = { x: Float -> (x - paddingTop - boardPadding.toFloat()) /
        gridSep + 1 }

    private fun setupAttributes(attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.SgfView, 0, 0).apply {
            try {
                blackColor = getColor(R.styleable.SgfView_blackColor, DEFAULT_BLACK_COLOR)
                whiteColor = getColor(R.styleable.SgfView_whiteColor, DEFAULT_WHITE_COLOR)
                gridColor = getColor(R.styleable.SgfView_gridColor, DEFAULT_GRID_COLOR)
                borderColor = getColor(R.styleable.SgfView_borderColor, DEFAULT_BORDER_COLOR)
                showText = getBoolean(R.styleable.SgfView_showText, DEFAULT_SHOW_TEXT)
            } finally {
                recycle()
            }
        }
    }

    companion object {
        private const val DEFAULT_BLACK_COLOR = Color.BLACK
        private const val DEFAULT_WHITE_COLOR = Color.WHITE
        private const val DEFAULT_GRID_COLOR = Color.LTGRAY
        private const val DEFAULT_BORDER_COLOR = Color.BLACK
        private const val DEFAULT_SHOW_TEXT = true
    }
}