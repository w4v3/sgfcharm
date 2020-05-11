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

    private var numRows = 19
    private var numCols = 19
    private var appName: String? = null
    private var appVersion: String? = null

    // this is a list containing for each node the incremental change of pieces on the board,
    // given as a pair of Change and the piece to be added or removed
    // only represents the current path in the tree
    private var incrementalMoves: MutableList<MutableList<Pair<Change, Piece>>> = mutableListOf()
    enum class Change { PLUS, MINUS }

    // for node specific information:
    private var nodeInfo: MutableList<NodeInfo> = mutableListOf()

    // for node specific board markup:
    private var markup: MutableList<Markup> = mutableListOf()
    private var variationMarkup: MutableList<Markup> = mutableListOf()

    // counters that are dragged along for undoing
    // only the last non empty one is transmitted
    private var moves: MutableList<MoveInfo?> = mutableListOf()

    // for variation display
    private var showVariations = true
    private var variationMode = VariationMode.SUCCESSORS
    enum class VariationMode { SUCCESSORS, SIBLINGS }

    // for inherited properties
    private var inherited: MutableList<MutableList<SgfData>> = mutableListOf()

    private var userBranch = false // is the user currently branching off on their own?

    // for checking of liberties:
    private var board: List<Piece> = listOf()
    private var alreadyChecked = mutableListOf<Piece>()

    // condense the various lists into a list of instructions for [SgfView]
    private fun getCurrentBoard(): List<SgfData> = listOf(
        listOf(GameInfo(numRows, numCols, appName, appVersion)),
        moves.lastOrNull()?.let { listOf(it) } ?: emptyList(),
        getPieces(),
        markup,
        inherited.lastOrNull() ?: emptyList(),
        getVariationMarkup(),
        nodeInfo
    ).flatten()

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

    // reads the current possible variations into a list of markup positions on the board if applicable
    private fun getVariationMarkup(): List<Markup> {
        // first we get a list of possible variations
        var variationNodes = listOf<SgfNode>()
        if (showVariations) { // only relevant if we want to show variations
            when (variationMode) {
                VariationMode.SUCCESSORS -> {
                    // only makes sense if this is the last node of the current sequence
                    // and if there are children
                    if (currentTree?.nodes?.getOrNull(currentNodeIndex + 1) == null) {
                        currentTree?.children?.mapNotNull { it.nodes.getOrNull(0) }
                            ?.let { variationNodes = it }
                    }
                }
                VariationMode.SIBLINGS -> {
                    // this must be the first node of the sequence
                    // and there must be other children in the parent
                    if (currentNodeIndex == 0) {
                        currentTree?.parent?.children?.mapNotNull { it.nodes.getOrNull(0) }
                            ?.let { variationNodes = it }
                    }
                }
            }
        }

        // the variations will be labeled 'A'..'Z' and in theory continuing further by 16 bit unicode
        // some of the nodes might contain a move, for which we would like to place the label
        // at the position of that move, but others might not, so these will be put onto the middle
        // line, equally spaced apart if possible
        val labeledVariations = variationNodes.mapIndexed { idx, node ->
            ('A'.toInt() + idx).toChar() to
                    node.find { it is SgfProperty.B || it is SgfProperty.W }
        }
        variationMarkup.clear()
        variationMarkup.addAll( // move properties can be added directly
            labeledVariations
            .filter { it.second != null }
            .map { Markup(
                MarkupType.VARIATION,
                (it.second!!.value as SgfType.Move).content.first,
                (it.second!!.value as SgfType.Move).content.second,
                label = it.first.toString()
            ) }
        )
        // for the rest, we first find out the optimal spacing
        val noMove = labeledVariations.filter { it.second == null }
        var y = ((numRows + 1) / 2).coerceAtLeast(1)
        val dx = ((numCols - 1) / (noMove.size + 1)).coerceAtLeast(1)
        // now we try and place them there if there is place, otherwise we shift things around
        noMove.forEachIndexed { idx, variation ->
            var x = (idx + 1) * dx
            while (variationMarkup.find { (it.x to it.y) == (x to y) } != null ) {
                // here the place is already taken, so we increase x
                x += 1
                if (x > numCols) {
                    // or y if x became too big; worst case is that it falls off the board in y direction
                    x = (idx + 1) * dx
                    y++
                }
            }
            // unoccupied space found, add here
            variationMarkup.add(Markup(MarkupType.VARIATION, x, y, label = variation.first.toString()))
        }
        variationMarkup.sortBy { it.label } // important for processing of touch events

        return variationMarkup
    }

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
            moves = moves.dropLast(2).toMutableList()
            inherited = inherited.dropLast(2).toMutableList()
            currentNodeIndex--
            it.processNode()
        } ?: currentTree?.parent?.let { // else go up
            currentTree = it
            currentNodeIndex = it.nodes.lastIndex // only reset if we actually ascent
            incrementalMoves = incrementalMoves.dropLast(2).toMutableList()
            moves = moves.dropLast(2).toMutableList()
            inherited = inherited.dropLast(2).toMutableList()
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
                // if currently showing variations, the user might have tapped onto one, so we should check
                val containingVariation = variationMarkup.indexOfFirst { (it.x to it.y) == (x to y) }
                if (containingVariation != -1) {
                    currentTree?.children?.getOrNull(containingVariation)?.let {
                        currentTree = it
                        currentNodeIndex = 0
                    }
                } else { // no shown variation, but if not showing variations the user might have found one anyway
                    val containingChildTree = currentTree?.children
                        ?.find { it.nodes.getOrNull(0)?.contains(userMove) == true }
                    if (containingChildTree != null) { // if successful, descent
                        currentTree = containingChildTree
                        currentNodeIndex = 0
                    } else {
                        // branching off, we add a new tree child for the user as the primary variation
                        // and put the remaining sequence of the current tree into another variation
                        // however, if the user attempts to put the stone on an already existing one, we return immediately
                        if (getPieces().find { it.x == x && it.y == y} != null) return getCurrentBoard()
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
            }
        } else { // already branching, just add the move and discard the rest of the branch
            // but again, do nothing if placed on an existing stone
            if (getPieces().find { it.x == x && it.y == y} != null) return getCurrentBoard()

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
        inherited.add(mutableListOf())
        moves.add(null)
        nodeInfo.clear()
        markup.clear()
        variationMarkup.clear()
        for (property in this) {
            when (property) {
                is SgfProperty.B -> makeMove(ColorValue.BLACK, property.value)
                is SgfProperty.W -> makeMove(ColorValue.WHITE, property.value)
                SgfProperty.KO -> {} // irrelevant for viewers
                is SgfProperty.MN -> currentMoveNumber = property.value.content
                is SgfProperty.AB -> addStones(ColorValue.BLACK, property.value)
                is SgfProperty.AW -> addStones(ColorValue.WHITE, property.value)
                is SgfProperty.AE -> removePoints(property.value)
                is SgfProperty.PL -> currentColor = property.value.content.also { nodeInfo.add(NodeInfo(SgfString.PL(property.value.content))) }
                is SgfProperty.C -> nodeInfo.add(NodeInfo(property.value.content))
                is SgfProperty.DM -> nodeInfo.add(NodeInfo(SgfString.DM(property.value.content)))
                is SgfProperty.GB -> nodeInfo.add(NodeInfo(SgfString.DM(property.value.content)))
                is SgfProperty.GW -> nodeInfo.add(NodeInfo(SgfString.GW(property.value.content)))
                is SgfProperty.HO -> nodeInfo.add(NodeInfo(SgfString.HO(property.value.content)))
                is SgfProperty.N -> nodeInfo.add(NodeInfo(property.value.content))
                is SgfProperty.UC -> nodeInfo.add(NodeInfo(SgfString.UC(property.value.content)))
                is SgfProperty.V -> nodeInfo.add(NodeInfo(SgfString.V, "${property.value.content}"))
                is SgfProperty.BM -> nodeInfo.add(NodeInfo(SgfString.BM(property.value.content)))
                SgfProperty.DO -> nodeInfo.add(NodeInfo(SgfString.DO))
                SgfProperty.IT -> nodeInfo.add(NodeInfo(SgfString.IT))
                is SgfProperty.TE -> nodeInfo.add(NodeInfo(SgfString.TE(property.value.content)))
                is SgfProperty.AR -> addComposeMarkup(MarkupType.ARROW, property.value)
                is SgfProperty.CR -> addMarkup(MarkupType.CIRCLE, property.value)
                is SgfProperty.DD -> inherited.last().addAll(property.value.content.map { Markup(MarkupType.DIM, it.content.first, it.content.second) })
                is SgfProperty.LB -> addLabelMarkup(MarkupType.LABEL, property.value)
                is SgfProperty.LN -> addComposeMarkup(MarkupType.LINE, property.value)
                is SgfProperty.MA -> addMarkup(MarkupType.X, property.value)
                is SgfProperty.SL -> addMarkup(MarkupType.SELECT, property.value)
                is SgfProperty.SQ -> addMarkup(MarkupType.SQUARE, property.value)
                is SgfProperty.TR -> addMarkup(MarkupType.TRIANGLE, property.value)
                is SgfProperty.AP -> { appName = property.value.content.first.content; appVersion = property.value.content.second.content }
                is SgfProperty.CA -> {} // I'm sorry but it should be UTF-8
                is SgfProperty.FF -> {} // I'm sorry but it should be FF[4]
                is SgfProperty.GM -> {} // so far only Go is supported, will try to read anything as Go SGF
                is SgfProperty.ST -> configureVariations(property.value.content)
                is SgfProperty.SZ -> property.value.content.let { (c, r) -> numCols = c.content; numRows = r.content }
                is SgfProperty.AN -> nodeInfo.add(NodeInfo(property.value.content, SgfString.AN))
                is SgfProperty.BR -> nodeInfo.add(NodeInfo(property.value.content, SgfString.BR))
                is SgfProperty.BT -> nodeInfo.add(NodeInfo(property.value.content, SgfString.BT))
                is SgfProperty.CP -> nodeInfo.add(NodeInfo(property.value.content, SgfString.CP))
                is SgfProperty.DT -> nodeInfo.add(NodeInfo(property.value.content, SgfString.DT))
                is SgfProperty.EV -> nodeInfo.add(NodeInfo(property.value.content, SgfString.EV))
                is SgfProperty.GN -> nodeInfo.add(NodeInfo(property.value.content, SgfString.GN))
                is SgfProperty.GC -> nodeInfo.add(NodeInfo(property.value.content))
                is SgfProperty.ON -> nodeInfo.add(NodeInfo(property.value.content, SgfString.ON))
                is SgfProperty.OT -> nodeInfo.add(NodeInfo(property.value.content, SgfString.OT))
                is SgfProperty.PB -> nodeInfo.add(NodeInfo(property.value.content, SgfString.PB))
                is SgfProperty.PC -> nodeInfo.add(NodeInfo(property.value.content, SgfString.PC))
                is SgfProperty.PW -> nodeInfo.add(NodeInfo(property.value.content, SgfString.PW))
                is SgfProperty.RE -> nodeInfo.add(NodeInfo(property.value.content, SgfString.RE))
                is SgfProperty.RO -> nodeInfo.add(NodeInfo(property.value.content, SgfString.RO))
                is SgfProperty.RU -> nodeInfo.add(NodeInfo(property.value.content, SgfString.RU))
                is SgfProperty.SO -> nodeInfo.add(NodeInfo(property.value.content, SgfString.SO))
                is SgfProperty.TM -> nodeInfo.add(NodeInfo("${property.value.content}", SgfString.TM))
                is SgfProperty.US -> nodeInfo.add(NodeInfo(property.value.content, SgfString.US))
                is SgfProperty.WR -> nodeInfo.add(NodeInfo(property.value.content, SgfString.WR))
                is SgfProperty.WT -> nodeInfo.add(NodeInfo(property.value.content, SgfString.WT))
                is SgfProperty.BL -> nodeInfo.add(NodeInfo("${property.value.content}", SgfString.BL))
                is SgfProperty.OB -> nodeInfo.add(NodeInfo("${property.value.content}", SgfString.OB))
                is SgfProperty.OW -> nodeInfo.add(NodeInfo("${property.value.content}", SgfString.OW))
                is SgfProperty.WL -> nodeInfo.add(NodeInfo("${property.value.content}", SgfString.WL))
                is SgfProperty.FG -> {} // the View is not suitable for printing
                is SgfProperty.PM -> {} // the View is not suitable for printing
                is SgfProperty.VW -> inherited.last().addAll(property.value.content.map { Markup(MarkupType.VISIBLE, it.content.first, it.content.second) })
                is SgfProperty.HA -> nodeInfo.add(NodeInfo("${property.value.content}", SgfString.HA))
                is SgfProperty.KM -> nodeInfo.add(NodeInfo("${property.value.content}", SgfString.KM))
                is SgfProperty.TB -> addMarkup(MarkupType.BLACK_TERRITORY, property.value)
                is SgfProperty.TW -> addMarkup(MarkupType.WHITE_TERRITORY, property.value)
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
        // first of all, SGF has this rule where e.g. B[tt] means pass for boards <= 19x19
        // which cannot be checked by the parser as it doesn't know about board sizes
        // so we do it here
        if (numCols <= 19 && numRows <= 19 && move == SgfType.Move(20 to 20)) return

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
        var otherPrisoners = 0
        otherPrisoners += handleCheck(!checkLiberties(Piece(!colorValue, move.content.first - 1, move.content.second)))
        otherPrisoners += handleCheck(!checkLiberties(Piece(!colorValue, move.content.first, move.content.second - 1)))
        otherPrisoners += handleCheck(!checkLiberties(Piece(!colorValue, move.content.first + 1, move.content.second)))
        otherPrisoners += handleCheck(!checkLiberties(Piece(!colorValue, move.content.first, move.content.second + 1)))

        // now we check for suicide
        board = getPieces()
        alreadyChecked.clear()
        val ownPrisoners = handleCheck(!checkLiberties(Piece(colorValue, move.content.first, move.content.second)))

        currentColor = !colorValue

        moves[moves.lastIndex] = MoveInfo(
            (moves.findLast { it != null }?.moveNumber ?: 0) + 1,
            Piece(colorValue, move.content.first, move.content.second),
            ((moves.findLast { it != null}?.prisoners?.first ?: 0) + if (colorValue == ColorValue.BLACK) ownPrisoners else otherPrisoners) to
                    ((moves.findLast { it != null}?.prisoners?.second ?: 0) + if (colorValue == ColorValue.BLACK) otherPrisoners else ownPrisoners)
        )
    }

    // helping function to remove the pieces in [alreadyChecked] accumulated by [checkLiberties]
    // and to clean up regardless
    private fun handleCheck(remove: Boolean): Int {
        if (remove) incrementalMoves.last().addAll(alreadyChecked.map { Change.MINUS to it })
        board = getPieces()
        return (if (remove) alreadyChecked.distinct().size else 0).also { alreadyChecked.clear() }
    }

    // check if the group connected to [stone] has any liberties left
    private fun checkLiberties(stone: Piece?): Boolean {
        if (stone == null
            || stone.x !in 1..numCols
            || stone.y !in 1..numRows)
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
        rht ?: if (stone.x < numCols) return true
        bot ?: if (stone.y < numRows) return true

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

    // the passed [value] is the sum of 0/1 for successor/sibling and 0/2 for show on/off
    private fun configureVariations(value: Int) {
        variationMode = when {
            value % 2 == 0 -> VariationMode.SUCCESSORS
            else -> VariationMode.SIBLINGS
        }
        showVariations = value < 2
    }

    private fun addMarkup(type: MarkupType, values: SgfType.List<SgfType.Point>) {
        markup.addAll(values.content.map {
            Markup(type, it.content.first, it.content.second)
        })
    }

    private fun addComposeMarkup(type: MarkupType, values: SgfType.List<SgfType.Compose<SgfType.Point, SgfType.Point>>) {
        markup.addAll(values.content.map {
            Markup(type, it.content.first.content.first, it.content.first.content.second, it.content.second.content.first, it.content.second.content.second)
        })
    }

    private fun addLabelMarkup(type: MarkupType, values: SgfType.List<SgfType.Compose<SgfType.Point, SgfType.SimpleText>>) {
        markup.addAll(values.content.map {
            Markup(type, it.content.first.content.first, it.content.first.content.second, label = it.content.second.content)
        })
    }
}