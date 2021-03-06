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

import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType
import onion.w4v3xrmknycexlsd.lib.sgfcharm.SgfController
import onion.w4v3xrmknycexlsd.lib.sgfcharm.Status
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.*

/**
 * If you want to use the `SGF` parsing and handling architecture provided by this library but make
 * your own view with fundamental changes, you can make it implement this interface so that it can be
 * controlled by the [SgfController].
 *
 * If you only need to make adjustments to the way some components are drawn, consider customizing
 * the provided [SgfView]. Support for different games can also be achieved by implementing the
 * [SgfBoard] interface.
 *
 * A cleaner architecture would be to have a different class than the view implement this adapter,
 * and then have the adapter tell the view what to draw. For ease of standard usage, in this library,
 * the [SgfView] implements this interface directly.
 *
 * You can also reuse the [GoBoard], [GoPieceDrawer], [DefaultMarkupDrawer] and [DefaultInfoTextMaker]
 * components used by the [SgfView] implementation for your own view.
 *
 * @see SgfInputListener
 */
@Status.Beta
public interface SgfViewAdapter {
    /**
     * Registers the [listener] as [SgfInputListener] to trigger in response to touch events.
     * @see[SgfInputListener]
     */
    @Status.Beta
    public fun registerInputListener(listener: SgfInputListener): Unit

    /**
     * Called by the [SgfController] to transmit new [data] for the view to display.
     *
     * This is a replacement of the old data, not an addition. See [SgfData] for a list of possible
     * types of data.
     */
    @Status.Beta
    public fun onReceiveSgfData(data: List<SgfData>): Unit
}

/** Interface for communicating touch events from the view to the controller. */
public interface SgfInputListener {
    /**
     * Should be triggered when the user has input a [move]; transmits that move to the controller.
     *
     * If the user has selected a variation that does not have an [SgfType.Move], this should transmit
     * the [variationIndex] of the selected variation. The [move] will then be ignored.
     */
    @Status.Impl
    public fun onMove(move: SgfType.Move, variationIndex: Int? = null)

    /** Should be triggered when the user has touched the undo button; navigates back. */
    @Status.Impl
    public fun onUndo()

    /** Should be triggered when the user has touched the redo button; navigates forward. */
    @Status.Impl
    public fun onRedo()
}
