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
package onion.w4v3xrmknycexlsd.lib.sgfcharm

import android.os.Bundle
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.*
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.*
import onion.w4v3xrmknycexlsd.lib.sgfcharm.view.SgfViewAdapter
import onion.w4v3xrmknycexlsd.lib.sgfcharm.view.SgfInputListener

/**
 * Controls interaction between an [SgfViewAdapter] and [SgfTree] objects.
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
 * @property[sgfViewAdapter] the [SgfViewAdapter] to control
 * @property[showVariations] whether or not to hint at the possible variations, overrides the settings
 * by the `sgf`; `null` not to override them (default)
 * @property[interactionMode] specifies how the user can interact with the controlled [SgfViewAdapter], one of [InteractionMode]
 */
@Status.Api
public class SgfController(
    var showVariations: Boolean? = null,
    var interactionMode: InteractionMode = InteractionMode.FREE_PLAY
) : SgfInputListener {
    public var sgfViewAdapter: SgfViewAdapter? = null
        set(value) {
            value?.registerInputListener(this)
            field = value
            state?.loadIntoView()
        }

    private var sgfNodeHandler: SgfNodeHandler = SgfNodeHandler()
    internal var navigator: SgfNavigator? = null // internal for save and restore
    private var state: SgfState? = null

    internal var sgfString: String? = null // for save and restore

    /**
     * How the user can interact with the [SgfViewAdapter] controlled by this [SgfController].
     *
     * Note that the behavior of the undo/redo buttons is not affected by this setting.
     */
    public enum class InteractionMode {
        /** Let the user execute all moves. */
        FREE_PLAY,

        /** Let the user execute a move, and immediately play the next move from the `SGF` file, if any. */
        COUNTERMOVE,

        /** Do not let the user execute any moves. */
        DISABLE
    }

    /** Sets up parser and navigator for the [sgfString], returning this [SgfController] for convenience. */
    @Status.Api
    public fun load(sgfString: String): SgfController = loadAtIndices(sgfString)

    @Status.Impl
    internal fun loadAtIndices(
        sgfString: String,
        indices: IntArray = listOf(0).toIntArray()
    ): SgfController {
        this.sgfString = sgfString

        SgfParser().parseSgfCollection(sgfString).getOrNull(0)?.let {
            navigator = SgfNavigator(it)
            state = SgfState()
        }

        navigator?.goToIndices(indices)?.forEach { it.process() }

        // in case the view was assigned already, we should update it
        state?.loadIntoView()

        return this
    }

    /** Loads the [SgfTree] into the [adapter] and subscribes to it as [SgfInputListener]. */
    @Status.Api
    public fun into(adapter: SgfViewAdapter) {
        this.sgfViewAdapter = adapter
    }

    // loading the [state] into the view
    private fun SgfState.loadIntoView() = sgfViewAdapter?.onReceiveSgfData(data)

    // processes this node using the handler, and then determining which variations to show
    // returning the transformed state
    private fun SgfNode.process(): SgfState? {
        with(sgfNodeHandler) {
            state?.processNode(this@process)
            if (showVariations ?: (state?.showVariations == true)) {
                navigator?.variations(state?.variationMode == SgfState.VariationMode.SUCCESSORS)
                    ?.let { variations ->
                        state?.setVariationInfos(variations)
                    }
            }
        }
        return state
    }

    /** Play the [move], or loads the variation at the given [variationIndex] if this is not `null`. */
    public override fun onMove(move: SgfType.Move, variationIndex: Int?): Unit =
        if (interactionMode == InteractionMode.DISABLE) Unit else
            state?.let { state ->
                if (
                    state.variationMode == SgfState.VariationMode.SIBLINGS
                    && showVariations ?: state.showVariations
                    && variationIndex != null
                ) onUndo() // going into a sibling variation

                navigator?.makeMove(
                    when (state.nextColor) {
                        SgfType.Color.Value.BLACK -> SgfProperty.B(move)
                        SgfType.Color.Value.WHITE -> SgfProperty.W(move)
                    }, variationIndex
                )?.process()?.loadIntoView()
                    ?.also { if (interactionMode == InteractionMode.COUNTERMOVE) onRedo() }
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
    putString("$key#sgfstring", controller?.sgfString)
    putIntArray("$key#indices", controller?.navigator?.currentIndices)
    putSerializable("$key#showvariations", Ternary[controller?.showVariations])
    putSerializable("$key#interactionmode", controller?.interactionMode)
}

/** Retrieves a controller from [this] `Bundle` stored at the [key]. */
@Status.Beta
public fun Bundle.getSgfController(key: String?): SgfController? =
    getString("$key#sgfstring")?.let { sgfString ->
        getIntArray("$key#indices")?.let { indices ->
            SgfController(
                (getSerializable("$key#showvariations") as? Ternary)?.value,
                (getSerializable("$key#interactionmode") as? SgfController.InteractionMode
                    ?: SgfController.InteractionMode.FREE_PLAY)
            ).loadAtIndices(
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

