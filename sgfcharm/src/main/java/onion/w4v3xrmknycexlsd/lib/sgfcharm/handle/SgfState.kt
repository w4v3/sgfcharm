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

package onion.w4v3xrmknycexlsd.lib.sgfcharm.handle

import onion.w4v3xrmknycexlsd.lib.sgfcharm.Status
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.not
import onion.w4v3xrmknycexlsd.lib.sgfcharm.view.SgfView

/**
 * This class holds state relevant to the [SgfView].
 *
 * Although I believe that this class is fairly generally applicable, I would not recommend using
 * it directly. Instead, the [SgfNodeHandler] should define everything which is necessary to manipulate
 * this object.
 *
 * @property[currentPieces] gets the current configuration of the board as a list of [Piece]s
 * @property[showVariations] whether or not to show variations according to the `sgf`
 * @property[variationMode] the [VariationMode] indicating how to display variations
 * @property[gameId] the type of game encoded in the `SGF` file (`GM` property)
 * @property[numRows] the number of rows of the board
 * @property[numCols] the number of columns of the board
 * @property[lastMoveInfo] the [MoveInfo] object from the last move played
 * @property[colorJustSet] can be set to force the next color to play
 */
@Status.Impl
class SgfState {
    // all the state bundled together
    @Status.Impl
    internal val data: List<SgfData>
        get() = currentPieces + nodeInfo + markup + variationData +
                (inherited.findLast { it.isNotEmpty() }?.filterNotNull() ?: emptyList()) +
                GameConfig(gameId, numCols, numRows) +
                (lastMoveInfo?.let { listOf(it) } ?: emptyList())

    // this is a list containing for each node the incremental change of pieces on the board,
    // given as a pair of Change and the piece to be added or removed
    // only represents the current path in the tree
    private val incrementalPieces: MutableList<MutableList<Pair<Change, Piece>>> = mutableListOf()

    // changes to [incrementalPieces] and undoing change the current board, represented here:
    private val _currentPieces = mutableListOf<Piece>()

    @Status.Beta
    public val currentPieces: List<Piece> = _currentPieces

    /** An incremental change to the board. */
    private enum class Change {
        /** The [Piece] was added. */
        PLUS,

        /** The [Piece] was removed. */
        MINUS
    }

    // for node specific information:
    @Status.Impl
    internal val nodeInfo: MutableList<NodeInfo> = mutableListOf()

    // for node specific board markup:
    @Status.Impl
    internal val markup: MutableList<Markup> = mutableListOf()

    // for variation display
    @Status.Impl
    internal var variationData: List<VariationData> = listOf()

    @Status.Beta
    public var showVariations: Boolean = true
        internal set

    @Status.Beta
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
    @Status.Beta
    public var gameId: Int = 1
        internal set

    @Status.Beta
    public var numRows: Int = 19
        internal set

    @Status.Beta
    public var numCols: Int = 19
        internal set

    // counters that are dragged along for undoing
    // only the last non empty one is transmitted
    private val moveInfo: MutableList<MoveInfo?> = mutableListOf()

    @Status.Beta
    public val lastMoveInfo: MoveInfo?
        get() = moveInfo.findLast { it != null }

    @Status.Impl
    public var colorJustSet: SgfType.Color.Value? = null

    @Status.Impl
    internal val nextColor: SgfType.Color.Value
        get() = colorJustSet ?: !(lastMoveInfo?.lastColor ?: SgfType.Color.Value.WHITE)

    @Status.Impl
    internal var moveNumberJustSet: Int? = null
        set(value) {
            field = value
            value?.let { // in case this came after a move property
                moveInfo[moveInfo.lastIndex]?.moveNumber = it
            }
        }

    // for inherited properties, contains list with null if settings were cleared
    @Status.Impl
    internal val inherited: MutableList<MutableList<Markup?>> = mutableListOf()

    @Status.Impl
    internal fun initStep() {
        incrementalPieces.add(mutableListOf()) // each node is in a separate list
        nodeInfo.clear()
        markup.clear()
        variationData = listOf()
        moveInfo.add(null)
        colorJustSet = null
        moveNumberJustSet = null
        inherited.add(mutableListOf())
    }

    // we actually undo two nodes before processing the next one
    // this is required to get the text information and color etc. back
    @Status.Impl
    internal fun stepBack() = repeat(2) {
        // this must be traversed in reverse order because during insertion, PLUS -> MINUS leaves
        // the piece gone and both changes inserted, while MINUS -> PLUS leads to insertion of PLUS only
        incrementalPieces.removeAt(incrementalPieces.lastIndex).reversed().map {
            when (it.first) {
                Change.PLUS -> _currentPieces.remove(it.second)
                Change.MINUS -> _currentPieces.add(it.second)
            }
        }
        moveInfo.removeAt(moveInfo.lastIndex)
        inherited.removeAt(inherited.lastIndex)
    }

    // for manipulation by custom handlers
    /** Adds the [piece] to the current board, potentially replacing pieces at the same point. */
    @Status.Beta
    public fun addPiece(piece: Piece) {
        incrementalPieces.last().add(Change.PLUS to piece)
        // other pieces at the same position must be replaced in [currentPieces], otherwise
        // capture calculations would be inaccurate
        removePieces(_currentPieces.filter { it.stone?.point == piece.stone?.point })
        _currentPieces.add(piece)
    }

    @Status.Impl
    internal fun addPieces(pieces: List<Piece>) = pieces.map { addPiece(it) }

    /** Removes the [piece] from the current board. */
    @Status.Beta
    public fun removePiece(piece: Piece) {
        // adds Change.MINUS piece only if the piece is currently on the board
        // to accurately reflect the change in [incrementalPieces]
        if (piece in _currentPieces) {
            incrementalPieces.last().add(Change.MINUS to piece)
            _currentPieces.remove(piece)
        }
    }

    @Status.Impl
    internal fun removePieces(pieces: List<Piece>) = pieces.map { removePiece(it) }

    /** Adds the [markups] to the current board. */
    @Status.Beta
    public fun addMarkups(markups: List<Markup>): Boolean =
        markup.addAll(markups)

    @Status.Impl
    internal fun setVariationInfos(infos: List<SgfType.Move?>) {
        variationData = (infos.indices zip infos)
            .map { (idx, inf) -> VariationData(idx, inf) }
    }

    /**
     * Adds the [inheritMarkups] to the current board.
     * They will stay on the board until that setting is cleared by adding a `listOf(null)`.
     * */
    @Status.Beta
    public fun addInherits(inheritMarkups: List<Markup?>): Boolean =
        inherited.last().addAll(inheritMarkups)

    /** Adds the [info] to the [NodeInfo] communicated to the [SgfView]. */
    @Status.Beta
    public fun addNodeInfo(info: NodeInfo): Boolean =
        nodeInfo.add(info)

    @Status.Impl
    internal fun addMoveInfo(info: MoveInfo): Unit {
        moveInfo[moveInfo.lastIndex] = MoveInfo(
            moveNumberJustSet ?: ((lastMoveInfo?.moveNumber ?: 0) + info.moveNumber),
            info.lastColor,
            info.lastPlayed,
            (((lastMoveInfo?.prisoners?.first ?: 0) + info.prisoners.first) to
                    ((lastMoveInfo?.prisoners?.second ?: 0) + info.prisoners.second))
        )
    }
}
