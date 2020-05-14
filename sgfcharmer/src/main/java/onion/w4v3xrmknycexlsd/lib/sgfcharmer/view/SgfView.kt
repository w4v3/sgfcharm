package onion.w4v3xrmknycexlsd.lib.sgfcharmer.view

import onion.w4v3xrmknycexlsd.lib.sgfcharmer.handle.Markup
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.handle.MoveInfo
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.handle.NodeInfo
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.handle.Piece
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.parse.SgfType
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.SgfController

/**
 * If you want to use the `sgf` parsing and handling architecture provided by this library but make
 * your own view with fundamental changes, you can make it implement this interface so that it can be
 * controlled by the [SgfController].
 *
 * If you only need to make adjustments to the way some components are drawn, consider implementing
 * [SgfDrawer] instead.
 *
 * @property[inputListener] the [SgfController] uses this to register as [SgfInputListener]
 * @property[pieces] the [SgfController] puts the current [Piece]s on the board here
 * @property[nodeInfos] the [SgfController] puts the [NodeInfo]s from the current node here
 * @property[markups] the [SgfController] puts the current board [Markup]s here
 * @property[lastMoveInfo] the [SgfController] puts the last [MoveInfo] here
 * @property[gridColumns] the number of columns of the board (also filled by [SgfController])
 * @property[gridRows] the number of rows of the board (also filled by [SgfController])
 */
public interface ISgfView {

    public var inputListener: SgfInputListener?

    public var pieces: List<Piece>
    public var nodeInfos: List<NodeInfo>
    public var markups: List<Markup>
    public var lastMoveInfo: MoveInfo?

    public var gridColumns: Int
    public var gridRows: Int

    /**
     * Make the view redraw itself. Triggered after all the information from the node was loaded into the View.
     * You probably do not need to override this as this is implemented by the `View` class itself.
     */
    public fun invalidate(): Unit
}

/** Interface for communicating touch events from the view to the controller. */
public interface SgfInputListener {
    /** Triggered when user has input a [move]. */
    public fun onMove(move: SgfType.Move)

    /** Triggered when user has touched the undo button. */
    public fun onUndo()

    /** Triggered when user has touched the redo button. */
    public fun onRedo()
}

