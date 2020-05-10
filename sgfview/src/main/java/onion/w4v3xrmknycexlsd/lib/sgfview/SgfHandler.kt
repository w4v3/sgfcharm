package onion.w4v3xrmknycexlsd.lib.sgfview

import onion.w4v3xrmknycexlsd.lib.sgfview.data.*

/**
 * This class converts between [SgfTree] and [SgfData] representations.
 *
 * @param[sgfString] the string representing the `sgf` data associated with this handler
 */
class SgfHandler(sgfString: String) {
    private var currentTree: SgfTree? = SgfParser.parseSgfTree(sgfString)
    private var currentNodeIndex: Int = -1
    private var currentColor = ColorValue.BLACK
    private var currentMoveNumber = 0

    private var rowNum = 19
    private var colNum = 19

    // this is a list containing for each node the incremental change of pieces on the board,
    // given as a pair of Change and the piece to be added or removed
    // only represents the current path in the tree
    private var incrementalMoves: MutableList<MutableList<Pair<Change, Piece>>> = mutableListOf()
    enum class Change { PLUS, MINUS }

    private var userBranch = false // is the user currently branching off on their own?

    // for checking of liberties:
    private var board: List<Piece> = listOf()
    private var alreadyChecked = mutableListOf<Piece>()

    // condense the various lists into a list of instructions for [SgfView]
    private fun getCurrentBoard(): List<SgfData> = listOf(getPieces()).flatten()

    // transform the [incrementalMoves] into the list of actual pieces
    // later occurrences replace earlier ones
    private fun getPieces(): List<Piece> =
        incrementalMoves
            .asSequence()
            .flatten() // in which node the move was made does not matter
            .let { flattenedMoves ->
                flattenedMoves.distinct()
                    .map { move -> // for every unique move, find the last occurrence
                        flattenedMoves
                            .findLast { it.second.x == move.second.x && it.second.y == move.second.y }
                    }
            }
            .filter { it?.first == Change.PLUS } // only include those with PLUS
            .mapNotNull { it?.second }
            .toList()


    fun getNextBoard(): List<SgfData> {
        // as long as we have a tree, try to get its next node and process it
        // if such a node does not exist, descent into its children
        (currentTree?.nodes?.getOrNull(currentNodeIndex + 1))?.let { // advance one node if possible
            currentNodeIndex++
            it.processNode()
            return getCurrentBoard()
        } ?: currentTree?.children?.getOrNull(0)?.let { // else descent, always into first variation
            currentTree = it
            currentNodeIndex = 0 // only reset if we actually descent
            currentTree?.nodes?.getOrNull(currentNodeIndex)?.processNode()
        }

        return getCurrentBoard() // we are at the end of the tree already, nothing changes
    }

    fun getPreviousBoard(): List<SgfData> {
        // analogous to [getNextBoard]
        (currentTree?.nodes?.getOrNull(currentNodeIndex - 1))?.let { // go back one node if possible
            // we actually undo two nodes, and process the next one
            // this is required to get the text information and color etc. back
            incrementalMoves = incrementalMoves.dropLast(2).toMutableList()
            currentNodeIndex--
            it.processNode()
        } ?: currentTree?.parent?.let { // else go up
            currentTree = it
            currentNodeIndex = it.nodes.lastIndex // only reset if we actually ascent
            incrementalMoves = incrementalMoves.dropLast(2).toMutableList()
            // if we ascended from a user branch, it gets deleted
            if (userBranch) {
                currentTree?.apply {
                    nodes.addAll(children[1].nodes) // add the old nodes back in first
                    children.removeAt(1) // remove old node branch
                    children.removeAt(0) // remove user branch
                }
                userBranch = false
            }
            currentTree?.nodes?.getOrNull(currentNodeIndex)?.processNode()
        }

        // in case the root node doesn't set the color, we just set it to default
        if (currentTree?.parent == null && currentNodeIndex == 0) currentColor = ColorValue.BLACK

        return getCurrentBoard()
    }

    fun addToBoard(x: Int, y: Int): List<SgfData> {
        // if the user attempts to put the stone on an already existing one, we return immediately
        if (getPieces().find { it.x == x && it.y == y} != null) return getCurrentBoard()
        val userMove = when (currentColor) {
            ColorValue.BLACK -> SgfProperty.B(SgfType.Move(x to y))
            ColorValue.WHITE -> SgfProperty.W(SgfType.Move(x to y))
        }
        // we have to distinguish between the case where the user is playing on their own
        // (userBranch == true) and the one where a variation from the tree might have been selected
        if (!userBranch) {
            // if the next node, or the first nodes in the children if we are at the last node,
            // contains the move carried out by the user, we move to that node or tree
            // otherwise, the user goes branching on their own
            if (currentTree?.nodes?.getOrNull(currentNodeIndex + 1)?.contains(userMove) == true) {
                currentNodeIndex++
            } else {
                // try to find the move in the children trees
                val containingChildTree = currentTree?.children
                    ?.find { it.nodes.getOrNull(0)?.contains(userMove) == true }
                if (containingChildTree != null) { // if successful, descent
                    currentTree = containingChildTree
                    currentNodeIndex = 0
                } else {
                    // branching off, we add a new tree child for the user as the primary variation
                    // and put the remaining sequence of the current tree into another variation
                    val userTree = SgfTree(
                        currentTree,
                        mutableListOf(mutableListOf(userMove as SgfProperty<SgfType<*>>))
                    )
                    currentTree?.apply {
                        // splitting the nodes at the current index
                        val oldNodes = nodes.take(currentNodeIndex + 1)
                        val remainingNodes = nodes.drop(currentNodeIndex + 1).toMutableList()
                        nodes.clear()
                        nodes.addAll(oldNodes) // the ones before get added back
                        children.add(0, userTree)
                        // the others go into a separate tree at position 1
                        children.add(1, SgfTree(currentTree, remainingNodes))
                    }
                    currentTree = userTree
                    currentNodeIndex = 0
                    userBranch = true
                }
            }
        } else { // already branching, just add the move and discard the rest of the branch
            val keep = currentTree?.nodes?.take(currentNodeIndex + 1)
            currentTree?.nodes?.clear()
            keep?.let { currentTree?.nodes?.addAll(it) }
            currentTree?.nodes?.add(mutableListOf(userMove))
            currentNodeIndex++
        }

        currentTree?.nodes?.getOrNull(currentNodeIndex)?.processNode()
        return getCurrentBoard()
    }

    private fun SgfNode.processNode() {
        incrementalMoves.add(mutableListOf()) // each node is in a separate list
        for (property in this) {
            when (property) {
                is SgfProperty.B -> makeMove(ColorValue.BLACK, property.value)
                is SgfProperty.W -> makeMove(ColorValue.WHITE, property.value)
                is SgfProperty.KO -> {} // irrelevant for viewers
                is SgfProperty.MN -> currentMoveNumber = property.value.content
                is SgfProperty.AB -> addStones(ColorValue.BLACK, property.value)
                is SgfProperty.AW -> addStones(ColorValue.WHITE, property.value)
                is SgfProperty.AE -> removePoints(property.value)
                is SgfProperty.PL -> currentColor = property.value.content
                else -> {}
            }
        }
    }

    // add stones without checking
    private fun addStones(colorValue: ColorValue, stones: SgfType.List<SgfType.Stone>) =
        incrementalMoves.last().addAll(
            stones.content.map {
                Change.PLUS to
                        Piece(
                            colorValue,
                            it.content.first,
                            it.content.second
                        )
            }
        )

    // make move, including check for beating
    private fun makeMove(colorValue: ColorValue, move: SgfType.Move) {
        // we need not check if there was a stone before; the move is always executed
        // so we carry it out first:
        incrementalMoves.last().add(
            Change.PLUS to
                    Piece(
                        colorValue,
                        move.content.first,
                        move.content.second
                    )
        )

        // now we find the stones of the other player that have lost their liberties due to that move
        board = getPieces()
        alreadyChecked.clear()
        // left, top, right, bottom:
        handleCheck(!checkLiberties(Piece(!colorValue, move.content.first - 1, move.content.second)))
        handleCheck(!checkLiberties(Piece(!colorValue, move.content.first, move.content.second - 1)))
        handleCheck(!checkLiberties(Piece(!colorValue, move.content.first + 1, move.content.second)))
        handleCheck(!checkLiberties(Piece(!colorValue, move.content.first, move.content.second + 1)))

        // now we check for suicide
        board = getPieces()
        alreadyChecked.clear()
        handleCheck(!checkLiberties(Piece(colorValue, move.content.first, move.content.second)))

        currentMoveNumber++
        currentColor = !colorValue
    }

    // helping function to remove the pieces in [alreadyChecked] accumulated by [checkLiberties]
    // and to clean up regardless
    private fun handleCheck(remove: Boolean) {
        if (remove) incrementalMoves.last().addAll(alreadyChecked.map { Change.MINUS to it })
        board = getPieces()
        alreadyChecked.clear()
    }

    // check if the group connected to [stone] has any liberties left
    private fun checkLiberties(stone: Piece?): Boolean {
        if (stone == null
            || stone.x !in 1..colNum
            || stone.y !in 1..rowNum)
            return false // for stones without liberties
        if (stone !in board) return true // a valid position, unoccupied, is a liberty

        // first, get all the potential neighbors
        var lft = board.find { it.x == stone.x - 1 && it.y == stone.y     }
        var top = board.find { it.x == stone.x     && it.y == stone.y - 1 }
        var rht = board.find { it.x == stone.x + 1 && it.y == stone.y     }
        var bot = board.find { it.x == stone.x     && it.y == stone.y + 1 }
        // if any of them does not exist and there is still space in that direction, a liberty is found
        lft ?: if (stone.x >      1) return true
        top ?: if (stone.y >      1) return true
        rht ?: if (stone.x < colNum) return true
        bot ?: if (stone.y < rowNum) return true

        // otherwise, the stone has no liberties and we need to check the others
        // if they have the same color and were not checked yet
        lft = if (lft?.color == stone.color && lft !in alreadyChecked) lft else null
        rht = if (rht?.color == stone.color && rht !in alreadyChecked) rht else null
        top = if (top?.color == stone.color && top !in alreadyChecked) top else null
        bot = if (bot?.color == stone.color && bot !in alreadyChecked) bot else null

        alreadyChecked.add(stone)
        return checkLiberties(lft)
                || checkLiberties(rht)
                || checkLiberties(top)
                || checkLiberties(bot)
    }

    // remove pieces if they are at the specified points
    private fun removePoints(points: SgfType.List<SgfType.Point>) {
        board = getPieces()
        incrementalMoves.last().addAll(
            points.content.flatMap { point ->
                board.filter { it.x == point.content.first && it.y == point.content.second }
                    .map { Change.MINUS to it }
            }
        )
    }
}