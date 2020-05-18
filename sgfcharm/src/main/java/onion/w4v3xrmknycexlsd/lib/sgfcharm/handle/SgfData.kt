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

import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType.*
import onion.w4v3xrmknycexlsd.lib.sgfcharm.view.GoSgfView
import onion.w4v3xrmknycexlsd.lib.sgfcharm.view.SgfView
import onion.w4v3xrmknycexlsd.lib.sgfcharm.SgfInfoKeys

/**
 * Base class for all instructions understandable by [GoSgfView].
 *
 * This is to cast the semantics of the `sgf` format into the semantics of what actually
 * needs to be drawn in the end, which might be different. For example, the [GoSgfView] always
 * needs a complete representation of the current board configuration, but each `sgf` node
 * contains only incremental changes to the board.
 */
sealed class SgfData

/**
 * A gaming piece.
 *
 * @property[color] the color of this piece
 * @property[stone] the [Stone] of this piece, or `null` to indicate a move without a stone
 */
data class Piece(val color: Color.Value, val stone: Stone?) : SgfData()

/**
 * Information about the last move played.
 *
 * @property[moveNumber] the number of the move
 * @property[lastPlaced] the [Piece] played in the move
 * @property[prisoners] a pair of Black and White prisoner counts
 */
data class MoveInfo(var moveNumber: Int, var lastPlaced: Piece, var prisoners: Pair<Int, Int>) :
    SgfData()

/**
 * Markup of nodes on the board.
 *
 * @property[type] the [MarkupType] of the markup
 * @property[point] the point on the board where to place the markup
 * @property[to] if the markup type has two points, like arrows and lines, this is the second point
 * @property[label] the text associated with the markup type, if any (like node labels)
 */
data class Markup(
    val type: MarkupType,
    val point: Point,
    val to: Point? = null,
    val label: String? = null
) : SgfData()

/**
 * The possible values for board Markup.
 */
enum class MarkupType {
    /** Labels the currently possible variations. */
    VARIATION,

    /** Draws an arrow. */
    ARROW,

    /** Draws a circle. */
    CIRCLE,

    /** Dims given points. */
    DIM,

    /** Labels points. */
    LABEL,

    /** Draws a line. */
    LINE,

    /** Marks points with an `x`. */
    X,

    /** Highlights points. */
    SELECT,

    /** Draws a square. */
    SQUARE,

    /** Draws a triangle. */
    TRIANGLE,

    /** Declares points as visible, rest becomes invisible. */
    VISIBLE,

    /** Marks black territory (Go). */
    BLACK_TERRITORY,

    /** Marks white territory (Go). */
    WHITE_TERRITORY,
}

/**
 * A piece of information relating to the current node to be displayed in the text window.
 *
 * Anything can be used for the [key] as long as the [SgfView] knows how to deal with it,
 * but by default, the [SgfNodeHandler] uses the values from [SgfInfoKeys] to encode which
 * `SGF` property this info came from.
 *
 * @property[key] the property type dependent part of the info
 * @property[message] the variable part of the info
 */
data class NodeInfo(val key: String? = null, val message: String? = null) : SgfData()

/**
 * Communicates the current board dimensions.
 *
 * @property[columns] number of columns on the board
 * @property[rows] number of rows on the board
 */
data class BoardConfig(val columns: Int, val rows: Int) : SgfData()