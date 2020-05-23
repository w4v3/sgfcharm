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

package onion.w4v3xrmknycexlsd.lib.sgfcharm.view

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import onion.w4v3xrmknycexlsd.lib.sgfcharm.Status
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.Markup
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.Piece
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.VariationData
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType

/**
 * This interface represents a board for a specific game for interaction with the [SgfView].
 *
 * It can be thought of as a subview of the [SgfView] handling everything related to the board, which
 * is game specific. Therefore, it is responsible for converting the [SgfType.Point] type used by the game
 * to screen coordinates, for drawing the board, handling variation markup and converting touch events
 * to the [SgfType.Move] type of the game.
 *
 * Implement this interface if you want to keep the overall structure of the [SgfView], but handle
 * a Game other than Go. Add your implementation to the view using [SgfView.useBoard].
 *
 * @see GoBoard
 */
interface SgfBoard {
    /**
     * Returns a measure for the size of a board tile on screen, like the sidelength of a tile
     * or the distance between two gridlines.
     *
     * The [SgfView] uses this to draw the pieces and the markup at the appropriate size.
     */
    @Status.Beta
    public var tileSize: Int

    /**
     * Initialize the board dimensions to the requested [width], where [toDisplay] should consist of
     * the borders of the currently visible board in terms of board coordinates.
     *
     * Note that this might only be a part of the total board if the `VW` property was used to make
     * some points invisible. For example, `toDisplay = Rect(2, 3, 8, 8)` would mean to draw the board
     * from `(2, 3)` to `(8, 8)`.
     */
    @Status.Beta
    public fun setBoard(toDisplay: Rect, width: Int): Int

    /** Returns a set of all the [SgfType.Point]s on the board, given column count [numColumns] and row count [numRows]. */
    @Status.Beta
    public fun getAllPoints(numColumns: Int, numRows: Int): Set<SgfType.Point>

    /** Converts this [SgfType.Point] to a coordinate `(x to y)` on screen. */
    @Status.Beta
    public fun SgfType.Point.toScreenCoordinate(): Pair<Float, Float>

    /**
     * Draws the portion of the board given by [onDisplay], when the board has total column count
     * [trueColumns] and total row count [trueRows] onto this [Canvas] using the given [paint].
     */
    @Status.Beta
    public fun Canvas.drawBoard(trueColumns: Int, trueRows: Int, onDisplay: Rect, paint: Paint)

    /** Puts the variations given by the [variationData] on the board. */
    @Status.Beta
    public fun Canvas.drawVariations(variationData: List<VariationData>, paint: Paint)

    /**
     * Handle a touch event on the board at these coordinates `(x, y)`, with the [currentPieces]
     * on the board, and return a triggered [SgfType.Move], if any, and the index of the variation
     * selected by this event, if any.
     */
    @Status.Beta
    public fun Pair<Float, Float>.onTouch(currentPieces: List<Piece>): Pair<SgfType.Move?, Int?>
}