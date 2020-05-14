package onion.w4v3xrmknycexlsd.lib.sgfcharmer

import onion.w4v3xrmknycexlsd.lib.sgfcharmer.handle.*
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.parse.*
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.view.ISgfView
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.view.SgfInputListener
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.view.GoSgfView

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
public class SgfController(var showVariations: Boolean? = null) : SgfInputListener {
    public var sgfView: ISgfView? = null
        set(sgfView) {
            sgfView?.inputListener = this
            field = sgfView
            navigator?.nextNode()?.process()
        }

    public var sgfNodeHandler: SgfNodeHandler? = SgfNodeHandler()
    private var navigator: SgfNavigator? = null
    private var state: SgfState? = null

    /** Sets up parser and navigator for the [sgfString], returning this [SgfController] for convenience. */
    @Api
    public fun load(sgfString: String): SgfController {
        navigator = SgfNavigator(sgfString)
        state = SgfState()
        return this
    }

    /** Loads the [SgfTree] into the [sgfView] and subscribes to it as [SgfInputListener]. */
    @Api
    public fun into(sgfView: ISgfView) {
        this.sgfView = sgfView
    }

    // loading the [state] into the view
    private fun SgfState.load() {
        sgfView?.apply {
            pieces = this@load.currentPieces
            nodeInfos = this@load.nodeInfo
            markups =
                this@load.markup + this@load.variationMarkup + (this@load.inherited.lastOrNull()
                    ?: emptyList())
            gridRows = this@load.numRows
            gridColumns = this@load.numCols
            lastMoveInfo = this@load.lastMoveInfo

            invalidate()
        }
    }

    // processes this node using the handler, and then determining which variations to show
    private fun SgfNode.process() {
        sgfNodeHandler?.let {
            with(it) {
                state?.processNode(this@process)
                if (showVariations ?: (state?.showVariations == true)) {
                    navigator?.variations(state?.variationMode == SgfState.VariationMode.SUCCESSORS)
                        ?.let { variations ->
                            state?.variationsMarker(variations)
                                ?.let { markups -> state?.addVariationMarkups(markups) }
                        }
                }
            }
        }
        state?.load()
    }

    /** Finds out of there is a variation to play at move point, or otherwise simply places the piece. */
    override fun onMove(move: SgfType.Move): Unit =
        state?.let { state ->
            navigator?.makeMove(
                when (state.nextColor) {
                    SgfType.Color.Value.BLACK -> SgfProperty.B(move)
                    SgfType.Color.Value.WHITE -> SgfProperty.W(move)
                }, state.variationMarkup.indexOfFirst { it.from == move.point }.takeIf { it >= 0 })
                ?.process()
        } ?: Unit

    /** Redoes the last undone move, if any, or plays the main variation otherwise. */
    override fun onRedo(): Unit = navigator?.nextNode()?.process() ?: Unit

    /** Undoes the last move, regardless of whether it is part of the `sgf` or not. */
    override fun onUndo(): Unit =
        navigator?.previousNode()?.let { node ->
            state?.stepBack()
            node.process()
        } ?: Unit
}