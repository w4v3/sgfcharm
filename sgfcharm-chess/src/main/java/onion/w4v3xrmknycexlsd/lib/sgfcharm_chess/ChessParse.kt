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

package onion.w4v3xrmknycexlsd.lib.sgfcharm_chess

import onion.w4v3xrmknycexlsd.lib.sgfcharm.Status
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.*

/**
 * [SgfType.Stone] type for Chess.
 *
 * @property[type] the [ChessStoneType] of the piece
 * @property[point] its position.
 */
@Status.Beta
data class ChessStone(val type: ChessStoneType, override val point: XYPoint) : SgfType.Stone(point)

/** The possible types of [ChessStone]s. */
@Status.Beta
enum class ChessStoneType {
    /** The king. */
    KING,

    /** The queen. */
    QUEEN,

    /** A rook. */
    ROOK,

    /** A bishop. */
    BISHOP,

    /** A knight. */
    KNIGHT,

    /** A pawn. */
    PAWN
}

/** Base class for the different types of moves for Chess. */
@Status.Beta
abstract class ChessMove(override val point: XYPoint?) : SgfType.Move(point)

/**
 * A move of a [ChessStone] to a point, potentially capturing what was at the target point before.
 *
 * @property[stone] the moved piece
 * @property[point] the target of the move
 */
@Status.Beta
data class StandardChessMove(val stone: ChessStone, override val point: XYPoint) : ChessMove(point)

/**
 * A castling move.
 *
 * @property[long] if true, this is long castling, otherwise short castling.
 */
@Status.Beta
data class CastlingMove(val long: Boolean) : ChessMove(null)

/** A pass move. */
@Status.Beta
object ChessPass : ChessMove(null)

/**
 * A move where a pawn reaches the last row and is then promoted to another piece.
 *
 * @property[stone] the pawn played
 * @property[point] the target of the pawn move
 * @property[promotedTo] the type of stone the pawn was promoted to
 */
@Status.Beta
data class PromotionMove(
    val stone: ChessStone,
    override val point: XYPoint,
    val promotedTo: ChessStoneType
) : ChessMove(point)

/**
 * An en passant capture pawn move.
 *
 * @property[stone] the pawn capturing en passant
 * @property[point] the target move (i.e., diagonal to the original position, not the captured pawn)
 */
@Status.Beta
data class EnPassantMove(val stone: ChessStone, override val point: XYPoint) : ChessMove(point)

/**
 * The coordinate parser for used to parse chess `SGF`.
 *
 * As no official specification for the `SGF` encoding for chess exists, we use our own rules here:
 * - a `Point` is encoded by a lowercase letter indicating the column, starting from the right;
 * and a number indicating the row, counted from the *bottom* (as opposed to Go, but in accordance
 * with standard algebraic notation)
 * - a `Stone` consists of a letter `KQRBNP` indicating its type, followed by a `Point`
 * - a `Move` is either a `StandardMove`, a `CastlingMove`, a `PromotionMove` or an `EnPassantMove`
 * - a `StandardMove` is a `Stone` and a `Move`, separated by ':'
 * - a `CastlingMove` is either "0-0" for short castling or "0-0-0" for long castling
 * - a `PromotionMove` is a `StandardMove` followed by '=' and a letter `QRBN` indicating which type
 * of piece the pawn is promoted to
 * - an `EnPassantMove` is a `StandardMove` followed by "e.p."
 * - any move property with a value not parseable into one of these categories is passed on as a pass move
 */
@Status.Beta
public object ChessCoordinateParser : SgfParser.CoordinateParser<XYPoint>() {
    public override fun parsePoint(from: String): XYPoint? = from.getOrNull(0)?.let { column ->
        from.drop(1).toIntOrNull()?.let { row ->
            XYPoint(('a'..'z').indexOf(column) + 1, row)
        }
    }

    public override fun parseStone(from: String): ChessStone? = with(from) {
        firstOrNull()?.toChessStoneType()?.let { stone ->
            parsePoint(drop(1))?.let { point ->
                ChessStone(stone, point)
            }
        }
    }

    private fun Char.toChessStoneType(): ChessStoneType? =
        when (this) {
            'K' -> ChessStoneType.KING
            'Q' -> ChessStoneType.QUEEN
            'R' -> ChessStoneType.ROOK
            'B' -> ChessStoneType.BISHOP
            'N' -> ChessStoneType.KNIGHT
            'P' -> ChessStoneType.PAWN
            else -> null
        }

    public override fun parseMove(from: String): ChessMove = when {
        from == "0-0" -> CastlingMove(false)
        from == "0-0-0" -> CastlingMove(true)
        from.contains("=") -> from.split("=").let {
            it.getOrNull(0)?.parseStandardMove()?.let { move ->
                it.getOrNull(1)?.getOrNull(0)?.toChessStoneType()?.let { type ->
                    PromotionMove(move.stone, move.point, type)
                }
            }
        }
        from.contains("e.p.") -> from.dropLast(4).parseStandardMove()?.let { (stone, point) ->
            EnPassantMove(stone, point)
        }
        else -> from.parseStandardMove()
    } ?: ChessPass

    @OptIn(Status.Util::class)
    private fun String.parseStandardMove(): StandardChessMove? =
        parseCompose(::parseStone, ::parsePoint)?.let { (stone, point) ->
            StandardChessMove(stone, point)
        }

    public override fun XYPoint.rangeTo(other: XYPoint): List<XYPoint> =
        (x..other.x).flatMap { x -> (other.y downTo y).map { y -> XYPoint(x, y) } }
}

