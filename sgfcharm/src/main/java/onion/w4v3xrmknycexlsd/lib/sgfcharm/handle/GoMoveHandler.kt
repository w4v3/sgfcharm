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
@file:JvmName("SgfMoveHandler")

package onion.w4v3xrmknycexlsd.lib.sgfcharm.handle

import onion.w4v3xrmknycexlsd.lib.sgfcharm.Status
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.XYMove
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.XYStone
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.not

private var board = listOf<Piece>()
private val alreadyChecked = mutableListOf<Piece>()

// make move, including check for beating
/** The move handler used by [SgfNodeHandler] to handle Go moves.
 *
 * Checks for captured stones after each move according to the `SGF` specifications.
 */
@Status.Beta
public val GoMoveHandler: SgfMoveHandler =
    { state, color, move -> state.makeMove(color, move) }

private fun SgfState.makeMove(
    colorValue: SgfType.Color.Value,
    move: SgfType.Move
): MoveInfo? {
    if (move !is XYMove) return null
    val point = move.point
    // first of all, SGF has this rule where e.g. B[tt] means pass for boards <= 19x19
    // which cannot be checked by the parser as it doesn't know about board sizes
    // so we do it here
    // if point is null this also means pass
    if ((numCols <= 19 && numRows <= 19 && move == XYMove(
            20,
            20
        ))
        || point == null
    ) return MoveInfo(
        1,
        colorValue,
        XYMove(null),
        (0 to 0)
    )

    // we need not check if there was a stone before; the move is always executed
    // so we carry it out first:
    addPiece(Piece(colorValue, XYStone(point)))

    // now we find the stones of the other player that have lost their liberties due to that point
    board = currentPieces
    alreadyChecked.clear()

    // left, top, right, bottom:
    var otherPrisoners = 0
    otherPrisoners +=
        handleCheck(!checkLiberties(Piece(!colorValue, XYStone(point.x - 1, point.y))))
    otherPrisoners +=
        handleCheck(!checkLiberties(Piece(!colorValue, XYStone(point.x, point.y - 1))))
    otherPrisoners +=
        handleCheck(!checkLiberties(Piece(!colorValue, XYStone(point.x + 1, point.y))))
    otherPrisoners +=
        handleCheck(!checkLiberties(Piece(!colorValue, XYStone(point.x, point.y + 1))))

    // now we check for suicide
    board = currentPieces
    alreadyChecked.clear()
    val ownPrisoners =
        handleCheck(!checkLiberties(Piece(colorValue, XYStone(point.x, point.y))))

    return MoveInfo(
        1,
        colorValue,
        XYMove(point.x, point.y),
        (if (colorValue == SgfType.Color.Value.BLACK) ownPrisoners else otherPrisoners) to
                (if (colorValue == SgfType.Color.Value.BLACK) otherPrisoners else ownPrisoners)
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
    val point = (piece?.stone as? XYStone)?.point
    if (point == null
        || point.x !in 1..numCols
        || point.y !in 1..numRows
    )
        return false // for stones without liberties
    if (piece !in board) return true // a valid position, unoccupied, is a liberty

    // first, get all the potential neighbors
    var lft =
        board.find { (it.stone as? XYStone)?.point?.x == point.x - 1 && (it.stone as? XYStone)?.point?.y == point.y }
    var top =
        board.find { (it.stone as? XYStone)?.point?.x == point.x && (it.stone as? XYStone)?.point?.y == point.y - 1 }
    var rht =
        board.find { (it.stone as? XYStone)?.point?.x == point.x + 1 && (it.stone as? XYStone)?.point?.y == point.y }
    var bot =
        board.find { (it.stone as? XYStone)?.point?.x == point.x && (it.stone as? XYStone)?.point?.y == point.y + 1 }

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
