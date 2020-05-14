package onion.w4v3xrmknycexlsd.lib.sgfcharmer.handle

import onion.w4v3xrmknycexlsd.lib.sgfcharmer.Impl
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.parse.SgfType
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.parse.not
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.view.GoSgfView

/**
 * This class holds state relevant to the [GoSgfView].
 *
 * Although I believe that this class is fairly generally applicable, I would not recommend using
 * it directly. Instead, the [SgfNodeHandler] should define everything which is necessary to manipulate
 * this object.
 *
 * @property[currentPieces] gets the current configuration of the board as a list of [Piece]s
 * @property[showVariations] whether or not to show variations according to the `sgf`
 * @property[variationMode] the [VariationMode] indicating how to display variations
 * @property[numRows] the number of rows of the board
 * @property[numCols] the number of columns of the board
 * @property[lastMoveInfo] the [MoveInfo] object from the last move played
 */
@Impl
class SgfState {
    // this is a list containing for each node the incremental change of pieces on the board,
    // given as a pair of Change and the piece to be added or removed
    // only represents the current path in the tree
    private val incrementalPieces: MutableList<MutableList<Pair<Change, Piece>>> = mutableListOf()

    // the [incrementalMoves] are transformed into the list of actual pieces
    // later occurrences replace earlier ones
    public val currentPieces: List<Piece>
        get() =
            incrementalPieces
                .asSequence()
                .flatten() // in which node the move was made does not matter
                .let { flattenedPieces ->
                    flattenedPieces.distinct()
                        .map { move -> // for every unique move, find the last occurrence
                            flattenedPieces
                                .findLast { it.second == move.second }
                        }
                }
                .filter { it?.first == Change.PLUS } // only include those with PLUS
                .mapNotNull { it?.second }
                .toList()

    /** An incremental change to the board. */
    private enum class Change {
        /** The [Piece] was added. */
        PLUS,

        /** The [Piece] was removed. */
        MINUS
    }

    // for node specific information:
    internal val nodeInfo: MutableList<NodeInfo> = mutableListOf()

    // for node specific board markup:
    internal val markup: MutableList<Markup> = mutableListOf()

    // for variation display
    internal val variationMarkup: MutableList<Markup> = mutableListOf()
    public var showVariations: Boolean = true
        internal set
    public var variationMode: VariationMode = VariationMode.SUCCESSORS
        internal set

    /** How to display variations. */
    enum class VariationMode {
        /** Show variations of successor nodes. */
        SUCCESSORS,

        /** Show variations of current nodes. */
        SIBLINGS
    }

    // root properties
    public var numRows: Int = 19
        internal set
    public var numCols: Int = 19
        internal set

    // counters that are dragged along for undoing
    // only the last non empty one is transmitted
    private val moveInfo: MutableList<MoveInfo?> = mutableListOf()
    public val lastMoveInfo: MoveInfo? get() = moveInfo.findLast { it != null }

    internal var colorJustSet: SgfType.Color.Value? = null
    internal val nextColor: SgfType.Color.Value
        get() = colorJustSet ?: !(lastMoveInfo?.lastPlaced?.color) ?: SgfType.Color.Value.BLACK

    internal var moveNumberJustSet: Int? = null
    internal val lastMoveNumber: Int
        get() = moveNumberJustSet ?: lastMoveInfo?.moveNumber?.plus(1) ?: 0

    // for inherited properties
    internal val inherited: MutableList<MutableList<Markup>> = mutableListOf()

    internal fun initStep() {
        incrementalPieces.add(mutableListOf()) // each node is in a separate list
        nodeInfo.clear()
        markup.clear()
        variationMarkup.clear()
        moveInfo.add(null)
        colorJustSet = null
        moveNumberJustSet = null
        inherited.add(mutableListOf())
    }

    // we actually undo two nodes before processing the next one
    // this is required to get the text information and color etc. back
    internal fun stepBack() = repeat(2) {
        incrementalPieces.removeAt(incrementalPieces.lastIndex)
        moveInfo.removeAt(moveInfo.lastIndex)
        inherited.removeAt(inherited.lastIndex)
    }

    // for manipulation by custom handlers
    /** Adds the [piece] to the current board. */
    public fun addPiece(piece: Piece): Boolean =
        incrementalPieces.last().add(Change.PLUS to piece)

    internal fun addPieces(pieces: List<Piece>) =
        incrementalPieces.last().addAll(pieces.map { Change.PLUS to it })

    /** Removes the [piece] from the current board. */
    public fun removePiece(piece: Piece): Boolean =
        incrementalPieces.last().add(Change.MINUS to piece)

    internal fun removePieces(pieces: List<Piece>) =
        incrementalPieces.last().addAll(pieces.map { Change.MINUS to it })

    /** Adds the [markups] to the current board. */
    public fun addMarkups(markups: List<Markup>): Boolean =
        markup.addAll(markups)

    internal fun addVariationMarkups(markups: List<Markup>): Boolean =
        variationMarkup.addAll(markups)

    /**
     * Adds the [inheritMarkups] to the current board.
     * They will stay on the board until that setting is cleared.
     * */
    public fun addInherits(inheritMarkups: List<Markup>): Boolean =
        inherited.last().addAll(inheritMarkups)

    /** Adds the [info] to the [NodeInfo] communicated to the [GoSgfView]. */
    public fun addNodeInfo(info: NodeInfo): Boolean =
        nodeInfo.add(info)

    internal fun addMoveInfo(info: MoveInfo): Unit {
        moveInfo[moveInfo.lastIndex] = MoveInfo(
            moveNumberJustSet ?: info.moveNumber,
            info.lastPlaced,
            info.prisoners
        )
    }
}
