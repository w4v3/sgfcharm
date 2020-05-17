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
package onion.w4v3xrmknycexlsd.lib.sgfcharm

import android.os.Bundle
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.*
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.*
import onion.w4v3xrmknycexlsd.lib.sgfcharm.view.SgfView
import onion.w4v3xrmknycexlsd.lib.sgfcharm.view.SgfInputListener
import onion.w4v3xrmknycexlsd.lib.sgfcharm.view.GoSgfView

/**
 * Controls interaction between the [GoSgfView] and [SgfTree] objects.
 *
 * The typical call would be `SgfController().load(someSgfString).into(someSgfView)`. This creates an
 * [SgfTree] from the given string and loads its current representation into the view. The controller
 * handles the touch events from the view and supplies it with new data to display accordingly.
 *
 * For this task, the controller consists of three components:
 * - an [SgfNavigator] (private), which creates and holds the [SgfTree] from the loaded string and
 * from which it requests the next or previous node
 * - an [SgfState] object (private), which holds the data to communicate to view, as well as their
 * history to make undoing moves possible
 * - an [SgfNodeHandler], which modifies the [SgfState] object according to the current node
 *
 * If you want to change anything about the way the nodes are processed, you need to modify the
 * [sgfNodeHandler]. See [SgfNodeHandler] for more information.
 *
 * @property[sgfView] the [GoSgfView] to control
 * @property[sgfNodeHandler] the [SgfNodeHandler] used to transform the state with the information from the nodes
 * @property[showVariations] whether or not to hint at the possible variations, overrides the settings
 * by the `sgf`; `null` not to override them (default)
 */
@Status.Api
public class SgfController(var showVariations: Boolean? = null) : SgfInputListener {
    public var sgfView: SgfView? = null
        set(sgfView) {
            sgfView?.registerInputListener(this)
            field = sgfView
            state?.loadIntoView()
        }

    public var sgfNodeHandler: SgfNodeHandler = SgfNodeHandler()
    internal var navigator: SgfNavigator? = null
    private var state: SgfState? = null

    /** Sets up parser and navigator for the [sgfString], returning this [SgfController] for convenience. */
    @Status.Api
    public fun load(sgfString: String): SgfController = loadAtIndices(sgfString)

    @Status.Impl
    internal fun loadAtIndices(
        sgfString: String,
        indices: IntArray = listOf(0).toIntArray()
    ): SgfController {
        navigator =
            SgfNavigator(sgfString)
        state = SgfState()

        navigator?.goToIndices(indices)?.forEach { it.process() }

        // in case the view was assigned already, we should update it
        state?.loadIntoView()

        return this
    }

    /** Loads the [SgfTree] into the [sgfView] and subscribes to it as [SgfInputListener]. */
    @Status.Api
    public fun into(sgfView: SgfView) {
        this.sgfView = sgfView
    }

    // loading the [state] into the view
    private fun SgfState.loadIntoView() = sgfView?.onReceiveSgfData(data)

    // processes this node using the handler, and then determining which variations to show
    // returning the transformed state
    private fun SgfNode.process(): SgfState? {
        with(sgfNodeHandler) {
            state?.processNode(this@process)
            if (showVariations ?: (state?.showVariations == true)) {
                navigator?.variations(state?.variationMode == SgfState.VariationMode.SUCCESSORS)
                    ?.let { variations ->
                        state?.variationsMarker(variations)
                            ?.let { markups -> state?.addVariationMarkups(markups) }
                    }
            }
        }
        return state
    }

    /** Finds out of there is a variation to play at move point, or otherwise simply places the piece. */
    public override fun onMove(move: SgfType.Move): Unit =
        state?.let { state ->
            val variationNumber =
                state.variationMarkup.indexOfFirst { it.point == move.point }.takeIf { it >= 0 }
            // if the user has tapped a sibling variation, we undo the last move before proceeding
            if (state.variationMode == SgfState.VariationMode.SIBLINGS && variationNumber != null && showVariations ?: (state.showVariations))
                onUndo()
            navigator?.makeMove(
                when (state.nextColor) {
                    SgfType.Color.Value.BLACK -> SgfProperty.B(move)
                    SgfType.Color.Value.WHITE -> SgfProperty.W(move)
                }, variationNumber
            )
                ?.process()?.loadIntoView()
        } ?: Unit

    /** Redoes the last undone move, if any, or plays the main variation otherwise. */
    public override fun onRedo(): Unit = navigator?.nextNode()?.process()?.loadIntoView() ?: Unit

    /** Undoes the last move, regardless of whether it is part of the `sgf` or not. */
    public override fun onUndo(): Unit =
        navigator?.previousNode()?.let { node ->
            state?.stepBack()
            node.process()?.loadIntoView()
        } ?: Unit
}

/**
 * Puts the current state of the [controller] into [this] `Bundle` with the [key].
 *
 * Note that user placed stones not in the `SGF` file will not be stored; instead, the last state
 * from the `SGF` is put into the bundle.
 */
@Status.Beta
public fun Bundle.putSgfController(key: String?, controller: SgfController?) {
    putString("$key#sgfstring", controller?.navigator?.sgfString)
    putIntArray("$key#indices", controller?.navigator?.currentIndices)
    putSerializable("$key#showvariations", Ternary[controller?.showVariations])
}

/** Retrieves a controller from [this] `Bundle` stored at the [key]. */
@Status.Beta
public fun Bundle.getSgfController(key: String?): SgfController? =
    getString("$key#sgfstring")?.let { sgfString ->
        getIntArray("$key#indices")?.let { indices ->
            SgfController((getSerializable("$key#showvariations") as? Ternary)?.value).loadAtIndices(
                sgfString,
                indices
            )
        }
    }

// all of this because Bundle.getBoolean returns false if the key doesn't exist instead of null
private enum class Ternary(val value: Boolean?) {
    TRUE(true),
    FALSE(false),
    NONE(null);

    companion object {
        private val map = values().associateBy(Ternary::value)
        operator fun get(value: Boolean?) = map[value]
    }
}

