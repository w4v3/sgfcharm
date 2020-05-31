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

import onion.w4v3xrmknycexlsd.lib.sgfcharm.SgfInfoKeys
import onion.w4v3xrmknycexlsd.lib.sgfcharm.Status
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.*
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.XYPoint
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.parseDouble
import kotlin.math.absoluteValue

/** Move handler for chess; captures target stones irrespective of color or legality of the move. */
@Status.Beta
@OptIn(Status.Impl::class)
public val ChessMoveHandler: SgfMoveHandler =
    lambda@{ state: SgfState, colorValue: SgfType.Color.Value, move: SgfType.Move ->
        with(state) {
            var prisoners = 0
            var prisonerColor = SgfType.Color.Value.BLACK

            fun checkCapture(point: XYPoint) =
                currentPieces.find { it.stone?.point == point }?.let {
                    prisonerColor = it.color
                    removePiece(it)
                    prisoners += when ((it.stone as? ChessStone)?.type) {
                        ChessStoneType.KING -> 0
                        ChessStoneType.QUEEN -> 9
                        ChessStoneType.ROOK -> 5
                        ChessStoneType.BISHOP -> 3
                        ChessStoneType.KNIGHT -> 3
                        ChessStoneType.PAWN -> 1
                        null -> 0
                    }
                }

            when (move) {
                is StandardChessMove -> {
                    removePiece(Piece(colorValue, move.stone))
                    checkCapture(move.point)
                    addPiece(Piece(colorValue, ChessStone(move.stone.type, move.point)))
                }
                is ChessPass -> MoveInfo(1, colorValue, move, (0 to 0))
                is CastlingMove -> {
                    val king = currentPieces.find {
                        (it.stone as? ChessStone)?.type == ChessStoneType.KING &&
                                it.color == colorValue
                    }

                    val rook = currentPieces.find {
                        (it.stone as? ChessStone)?.type == ChessStoneType.ROOK &&
                                it.color == colorValue &&
                                (it.stone?.point as? XYPoint)?.x?.minus(
                                    (king?.stone as? ChessStone)?.point?.x ?: 0
                                )?.absoluteValue == if (move.long) 4 else 3
                    }

                    king?.let { removePiece(it) }
                    rook?.let { removePiece(it) }

                    val row = when (colorValue) {
                        SgfType.Color.Value.BLACK -> 8
                        SgfType.Color.Value.WHITE -> 1
                    }

                    val kingcol = if (move.long) 3 else 7
                    val rookcol = if (move.long) 4 else 6

                    if (move.long) {
                        addPiece(
                            Piece(
                                colorValue,
                                ChessStone(ChessStoneType.KING, XYPoint(kingcol, row))
                            )
                        )
                        addPiece(
                            Piece(
                                colorValue,
                                ChessStone(ChessStoneType.ROOK, XYPoint(rookcol, row))
                            )
                        )
                    }
                }
                is PromotionMove -> {
                    removePiece(Piece(colorValue, move.stone))
                    checkCapture(move.point)
                    addPiece(Piece(colorValue, ChessStone(move.promotedTo, move.point)))
                }
                is EnPassantMove -> {
                    removePiece(Piece(colorValue, move.stone))
                    checkCapture(XYPoint(move.point.x, move.stone.point.y))
                    addPiece(Piece(colorValue, ChessStone(move.stone.type, move.point)))
                }
                else -> return@lambda null
            }

            MoveInfo(
                1,
                colorValue,
                move,
                if (prisonerColor == SgfType.Color.Value.BLACK) (prisoners to 0) else (0 to prisoners)
            )
        }
    }

/** Info key for the custom `CHK` property for chess indicating check/checkmate. */
@Status.Beta
public val SgfInfoKeys.CHK: Map<SgfType.Double.Value, String>
    get() = mapOf(
        SgfType.Double.Value.MUCH to "Check",
        SgfType.Double.Value.VERY_MUCH to "Checkmate"
    )

/**
 * Handles the custom chess properties.
 *
 * The `DEF[]` property is used to set up the board with the default starting configuration for chess.
 * The `CHK[1/2]` property is used to indicate a check (1) or checkmate (2) move in the current node.
 */
@Status.Beta
@OptIn(Status.Impl::class, Status.Util::class)
public val ChessCustomPropertyHandler: SgfCustomPropertyHandler =
    { state: SgfState, propIdent: String, propValue: String ->
        with(state) {
            when (propIdent) {
                "CHK" -> propValue.parseDouble()?.value?.let { addNodeInfo(NodeInfo(SgfInfoKeys.CHK[it])) }
                "DEF" -> {
                    colorJustSet = SgfType.Color.Value.WHITE
                    addPiece(
                        Piece(
                            SgfType.Color.Value.WHITE,
                            ChessStone(ChessStoneType.PAWN, XYPoint(1, 2))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.WHITE,
                            ChessStone(ChessStoneType.PAWN, XYPoint(2, 2))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.WHITE,
                            ChessStone(ChessStoneType.PAWN, XYPoint(3, 2))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.WHITE,
                            ChessStone(ChessStoneType.PAWN, XYPoint(4, 2))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.WHITE,
                            ChessStone(ChessStoneType.PAWN, XYPoint(5, 2))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.WHITE,
                            ChessStone(ChessStoneType.PAWN, XYPoint(6, 2))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.WHITE,
                            ChessStone(ChessStoneType.PAWN, XYPoint(7, 2))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.WHITE,
                            ChessStone(ChessStoneType.PAWN, XYPoint(8, 2))
                        )
                    )

                    addPiece(
                        Piece(
                            SgfType.Color.Value.WHITE,
                            ChessStone(ChessStoneType.ROOK, XYPoint(1, 1))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.WHITE,
                            ChessStone(ChessStoneType.KNIGHT, XYPoint(2, 1))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.WHITE,
                            ChessStone(ChessStoneType.BISHOP, XYPoint(3, 1))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.WHITE,
                            ChessStone(ChessStoneType.QUEEN, XYPoint(4, 1))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.WHITE,
                            ChessStone(ChessStoneType.KING, XYPoint(5, 1))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.WHITE,
                            ChessStone(ChessStoneType.BISHOP, XYPoint(6, 1))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.WHITE,
                            ChessStone(ChessStoneType.KNIGHT, XYPoint(7, 1))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.WHITE,
                            ChessStone(ChessStoneType.ROOK, XYPoint(8, 1))
                        )
                    )

                    addPiece(
                        Piece(
                            SgfType.Color.Value.BLACK,
                            ChessStone(ChessStoneType.PAWN, XYPoint(1, 7))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.BLACK,
                            ChessStone(ChessStoneType.PAWN, XYPoint(2, 7))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.BLACK,
                            ChessStone(ChessStoneType.PAWN, XYPoint(3, 7))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.BLACK,
                            ChessStone(ChessStoneType.PAWN, XYPoint(4, 7))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.BLACK,
                            ChessStone(ChessStoneType.PAWN, XYPoint(5, 7))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.BLACK,
                            ChessStone(ChessStoneType.PAWN, XYPoint(6, 7))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.BLACK,
                            ChessStone(ChessStoneType.PAWN, XYPoint(7, 7))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.BLACK,
                            ChessStone(ChessStoneType.PAWN, XYPoint(8, 7))
                        )
                    )

                    addPiece(
                        Piece(
                            SgfType.Color.Value.BLACK,
                            ChessStone(ChessStoneType.ROOK, XYPoint(1, 8))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.BLACK,
                            ChessStone(ChessStoneType.KNIGHT, XYPoint(2, 8))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.BLACK,
                            ChessStone(ChessStoneType.BISHOP, XYPoint(3, 8))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.BLACK,
                            ChessStone(ChessStoneType.QUEEN, XYPoint(4, 8))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.BLACK,
                            ChessStone(ChessStoneType.KING, XYPoint(5, 8))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.BLACK,
                            ChessStone(ChessStoneType.BISHOP, XYPoint(6, 8))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.BLACK,
                            ChessStone(ChessStoneType.KNIGHT, XYPoint(7, 8))
                        )
                    )
                    addPiece(
                        Piece(
                            SgfType.Color.Value.BLACK,
                            ChessStone(ChessStoneType.ROOK, XYPoint(8, 8))
                        )
                    )
                }
                else -> {
                }
            }
        }
    }

