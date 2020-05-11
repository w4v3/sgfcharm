package onion.w4v3xrmknycexlsd.lib.sgfview.data

import android.annotation.SuppressLint

/**
 * Base class for all instructions understandable by [SgfView].
 *
 * This is to cast the semantics of the `sgf` format into the semantics of what actually
 * needs to be drawn in the end, which might be different. For example, the [SgfView] always
 * has a complete representation of the current board configuration, but each `sgf` node
 * contains only incremental changes to the board.
 */
sealed class SgfData

/**
 * A gaming piece.
 *
 * @property[color] color of this piece.
 * @property[x] x coordinate where the piece is placed.
 * @property[y] y coordinate where the piece is placed.
 */
data class Piece(val color: ColorValue, val x: Int, val y: Int) : SgfData()

/**
 * A piece of information relating to the current node to be displayed in the text window.
 *
 * @property[propKey] the property type dependent part of the info
 * @property[message] the variable part of the info
 */
data class NodeInfo(val message: String, val propKey: String? = null) : SgfData()

// info property value keys
object SgfString {
    val PL: (ColorValue) -> String = { "${it.string()}to play" }
    val DM: (DoubleValue) -> String = { "${it.string()}even position" }
    val GB: (DoubleValue) -> String = { "${it.string()}good for black" }
    val GW: (DoubleValue) -> String = { "${it.string()}good for white" }
    @SuppressLint("DefaultLocale")
    val HO: (DoubleValue) -> String = { "${it.string()}interesting position" }
    val UC: (DoubleValue) -> String = { "${it.string()}unclear position" }
    const val V = "Score"
    val BM: (DoubleValue) -> String = { "${it.string()}bad move"}
    const val DO = "doubtful move"
    const val IT = "interesting move"
    val TE: (DoubleValue) -> String = { "${it.string()}good move"}
    const val AN = "Annotations by"
    const val BR = "Black player rank"
    const val BT = "Black team"
    const val CP = "Copyright"
    const val DT = "Date of game"
    const val EV = "Event"
    const val GN = "Game"
    const val ON = "Opening"
    const val OT = "Byo-yomi"
    const val PB = "Black player"
    const val PC = "Place"
    const val PW = "White player"
    const val RE = "Result"
    const val RO = "Round"
    const val RU = "Rule set"
    const val SO = "Source"
    const val TM = "Time limits"
    const val US = "Game entered by"
    const val WR = "White player rank"
    const val WT = "White team"
    const val BL = "Time left for black/s"
    const val OB = "Moves left for black"
    const val OW = "Moves left for white"
    const val WL = "Time left for white/s"
    const val HA = "Handicap"
    const val KM = "Komi"
}

fun DoubleValue.string() = if (this == DoubleValue.VERY_MUCH) "very " else ""
fun ColorValue.string() = if (this == ColorValue.BLACK) "black " else "white "

/** Communicates information specific to the game (root properties) */
data class GameInfo(
    val numRows: Int,
    val numCols: Int,
    val applicationName: String? = null,
    val applicationVersion: String? = null
) : SgfData()

data class MoveInfo(var moveNumber: Int, var lastPlaced: Piece, var prisoners: Pair<Int, Int>) : SgfData()

/** For markup of nodes on the board */
data class Markup(val type: MarkupType, val x: Int, val y: Int, val x2: Int? = null, val y2: Int? = null, val label: String? = null) : SgfData()

enum class MarkupType {
    VARIATION,
    ARROW,
    CIRCLE,
    DIM,
    LABEL,
    LINE,
    X,
    SELECT,
    SQUARE,
    TRIANGLE,
    VISIBLE,
    BLACK_TERRITORY,
    WHITE_TERRITORY,
}
