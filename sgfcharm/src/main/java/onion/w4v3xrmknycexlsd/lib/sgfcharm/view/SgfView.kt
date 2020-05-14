/*
 *    Copyright [2020] [w4v3]
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

import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.Markup
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.MoveInfo
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.NodeInfo
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.Piece
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType
import onion.w4v3xrmknycexlsd.lib.sgfcharm.SgfController
import onion.w4v3xrmknycexlsd.lib.sgfcharm.Status

/**
 * If you want to use the `sgf` parsing and handling architecture provided by this library but make
 * your own view with fundamental changes, you can make it implement this interface so that it can be
 * controlled by the [SgfController]. You do not need to actually implement anything, just override
 * the properties here and they will be populated by the [SgfController].
 *
 * You will however need to call the appropriate [inputListener] members when handling touch events
 * if you want anything to happen in response to them.
 *
 * If you only need to make adjustments to the way some components are drawn, consider implementing
 * [SgfDrawer] instead.
 *
 * @see SgfInputListener
 */
@Status.Beta
public interface SgfView {

    /** The [SgfInputListener] to trigger in response to touch events. */
    @Status.Beta
    public var inputListener: SgfInputListener?

    /** The current [Piece]s on the board. */
    @Status.Beta
    public var pieces: List<Piece>

    /** The [NodeInfo]s from the current node. */
    @Status.Beta
    public var nodeInfos: List<NodeInfo>

    /** The current board [Markup]s. */
    @Status.Beta
    public var markups: List<Markup>

    /** The [MoveInfo] object from the last move. */
    @Status.Beta
    public var lastMoveInfo: MoveInfo?

    /** The number of columns of the board.. */
    @Status.Beta
    public var gridColumns: Int

    /** The number of rows of the board. */
    @Status.Beta
    public var gridRows: Int

    /**
     * Make the view redraw itself. Triggered after all the information from the node was loaded into the View.
     * You probably do not need to override this as this is implemented by the `View` class itself.
     */
    public fun invalidate(): Unit
}

/** Interface for communicating touch events from the view to the controller. */
public interface SgfInputListener {
    /** Should be triggered when the user has input a [move]; transmits that move to the controller. */
    @Status.Impl
    public fun onMove(move: SgfType.Move)

    /** Should be triggered when the user has touched the undo button; navigates back. */
    @Status.Impl
    public fun onUndo()

    /** Should be triggered when the user has touched the redo button; navigates forward. */
    @Status.Impl
    public fun onRedo()
}

