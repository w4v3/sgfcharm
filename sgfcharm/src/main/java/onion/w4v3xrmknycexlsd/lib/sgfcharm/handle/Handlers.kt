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

/**
 * An [SgfNodeHandler] for Go.
 *
 * @property[moveHandler] checks for captured stones after each move according to the `sgf` specifications.
 * @property[variationMarker] implements a simple variation markup with letters, where variations without
 * moves are put to the middle of the board, with as equal spacing as possible, respecting other variations
 * that might have been printed there already (i.e., those with moves).
 */
@Status.Beta
object GoNodeHandler {
    private var board = listOf<Piece>()
    private val alreadyChecked = mutableListOf<Piece>()

    // make move, including check for beating
    @Status.Beta
    public val moveHandler: SgfMoveHandler =
        { state, color, move -> state.makeMove(color, move) }

    private fun SgfState.makeMove(
        colorValue: SgfType.Color.Value,
        move: SgfType.Move
    ): MoveInfo? {
        if (move !is SgfType.XYMove) return null
        val point = move.point
        // first of all, SGF has this rule where e.g. B[tt] means pass for boards <= 19x19
        // which cannot be checked by the parser as it doesn't know about board sizes
        // so we do it here
        // if point is null this also means pass
        if ((numCols <= 19 && numRows <= 19 && move == SgfType.XYMove(20, 20))
            || point == null
        ) return MoveInfo(
            (lastMoveInfo?.moveNumber ?: 0) + 1,
            Piece(colorValue, null),
            lastMoveInfo?.prisoners ?: (0 to 0)
        )

        // we need not check if there was a stone before; the move is always executed
        // so we carry it out first:
        addPiece(Piece(colorValue, SgfType.XYStone(point)))

        // now we find the stones of the other player that have lost their liberties due to that point
        board = currentPieces
        alreadyChecked.clear()

        // left, top, right, bottom:
        var otherPrisoners = 0
        otherPrisoners += handleCheck(
            !checkLiberties(
                Piece(
                    !colorValue ?: return null,
                    SgfType.XYStone(point.x - 1, point.y)
                )
            )
        )
        otherPrisoners += handleCheck(
            !checkLiberties(
                Piece(
                    !colorValue ?: return null,
                    SgfType.XYStone(point.x, point.y - 1)
                )
            )
        )
        otherPrisoners += handleCheck(
            !checkLiberties(
                Piece(
                    !colorValue ?: return null,
                    SgfType.XYStone(point.x + 1, point.y)
                )
            )
        )
        otherPrisoners += handleCheck(
            !checkLiberties(
                Piece(
                    !colorValue ?: return null,
                    SgfType.XYStone(point.x, point.y + 1)
                )
            )
        )

        // now we check for suicide
        board = currentPieces
        alreadyChecked.clear()
        val ownPrisoners =
            handleCheck(
                !checkLiberties(
                    Piece(
                        colorValue,
                        SgfType.XYStone(point.x, point.y)
                    )
                )
            )

        return MoveInfo(
            (lastMoveInfo?.moveNumber ?: 0) + 1,
            Piece(
                colorValue,
                SgfType.XYStone(point.x, point.y)
            ),
            ((lastMoveInfo?.prisoners?.first
                ?: 0) + if (colorValue == SgfType.Color.Value.BLACK) ownPrisoners else otherPrisoners) to
                    (lastMoveInfo?.prisoners?.second
                        ?: 0) + if (colorValue == SgfType.Color.Value.BLACK) otherPrisoners else ownPrisoners
        )
    }

    // helping function to remove the pieces in [alreadyChecked] accumulated by [checkLiberties]
    // and to clean up regardless
    private fun SgfState.handleCheck(remove: Boolean): Int {
        if (remove) removePieces(alreadyChecked)
        board = currentPieces
        return (if (remove) alreadyChecked.distinct().size else 0).also { alreadyChecked.clear() }
    }

    // check if the group connected to [stone] has any liberties left
    private fun SgfState.checkLiberties(piece: Piece?): Boolean {
        val point = (piece?.stone as? SgfType.XYStone)?.point
        if (point == null
            || point.x !in 1..numCols
            || point.y !in 1..numRows
        )
            return false // for stones without liberties
        if (piece !in board) return true // a valid position, unoccupied, is a liberty

        // first, get all the potential neighbors
        var lft =
            board.find { (it.stone as? SgfType.XYStone)?.point?.x == point.x - 1 && (it.stone as? SgfType.XYStone)?.point?.y == point.y }
        var top =
            board.find { (it.stone as? SgfType.XYStone)?.point?.x == point.x && (it.stone as? SgfType.XYStone)?.point?.y == point.y - 1 }
        var rht =
            board.find { (it.stone as? SgfType.XYStone)?.point?.x == point.x + 1 && (it.stone as? SgfType.XYStone)?.point?.y == point.y }
        var bot =
            board.find { (it.stone as? SgfType.XYStone)?.point?.x == point.x && (it.stone as? SgfType.XYStone)?.point?.y == point.y + 1 }

        // if any of them does not exist and there is still space in that direction, a liberty is found
        lft ?: if (point.x > 1) return true
        top ?: if (point.y > 1) return true
        rht ?: if (point.x < numCols) return true
        bot ?: if (point.y < numRows) return true

        // otherwise, the stone has no liberties and we need to check the others
        // if they have the same color and were not checked yet
        lft = if (lft?.color == piece.color && lft !in alreadyChecked) lft else null
        rht = if (rht?.color == piece.color && rht !in alreadyChecked) rht else null
        top = if (top?.color == piece.color && top !in alreadyChecked) top else null
        bot = if (bot?.color == piece.color && bot !in alreadyChecked) bot else null

        alreadyChecked.add(piece)
        return checkLiberties(lft)
                || checkLiberties(rht)
                || checkLiberties(top)
                || checkLiberties(bot)
    }

    @Status.Beta
    val variationMarker: SgfVariationsMarker = { state, vars -> state.markupVariations(vars) }

    private fun SgfState.markupVariations(variations: List<SgfType.Move?>): List<Markup> {
        // the variations will be labeled 'A'..'Z' and in theory continuing further by 16 bit unicode
        // some of the nodes might contain a move, for which we would like to place the label
        // at the position of that move, but others might not, so these will be put onto the middle
        // line, equally spaced apart if possible
        val labeledVariations = variations
            .mapIndexed { idx, move ->
                ('A'.toInt() + idx).toChar() to if (move?.point != null) move else null // pass nodes are marked like other non-move nodes
            }

        val variationMarkup = // move properties can be added directly
            labeledVariations
                .filter { it.second != null }
                .map {
                    Markup(
                        MarkupType.VARIATION,
                        it.second!!.point as SgfType.XYPoint,
                        label = it.first.toString()
                    )
                }.toMutableList()
        // for the rest, we first find out the optimal spacing
        val noMove = labeledVariations.filter { it.second == null }
        var y = ((numRows + 1) / 2).coerceAtLeast(1)
        val dx = ((numCols - 1) / (noMove.size + 1)).coerceAtLeast(1)
        // now we try and place them there if there is place, otherwise we shift things around
        noMove.forEachIndexed { idx, (first) ->
            var x = (idx + 1) * dx + 1 // column count starts at 1 so +1
            while (variationMarkup.find { it.point == SgfType.XYPoint(x, y) } != null) {
                // here the place is already taken, so we increase x
                x += 1
                if (x > numCols) {
                    // or y if x became too big; worst case is that it falls off the board in y direction
                    x = (idx + 1) * dx
                    y++
                }
            }
            // unoccupied space found, add here
            variationMarkup.add(
                Markup(
                    MarkupType.VARIATION,
                    SgfType.XYPoint(x, y),
                    label = first.toString()
                )
            )
        }
        variationMarkup.sortBy { it.label } // important for processing of touch events

        return variationMarkup
    }
}
