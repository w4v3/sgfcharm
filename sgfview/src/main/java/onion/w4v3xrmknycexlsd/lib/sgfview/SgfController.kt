package onion.w4v3xrmknycexlsd.lib.sgfview

import onion.w4v3xrmknycexlsd.lib.sgfview.data.Piece
import onion.w4v3xrmknycexlsd.lib.sgfview.data.SgfData

/**
 * Controls interaction between the [SgfView] and the [SgfHandler].
 *
 * The typical call would be `SgfController().load(someSgfString).into(someView)`. The [SgfHandler]
 * creates an [SgfTree] from the input string, and the controller requests the current sequence
 * from it. It also decides on what to do on user input.
 *
 * @property[sgfView] the [SgfView] to control.
 * @property[showVariations] whether or not to hint at the possible variations
 */
class SgfController(var showVariations: Boolean = true) : SgfView.OnTouchListener {
    var sgfView: SgfView? = null
        set(sgfView) {
            sgfView?.listener = this
            field = sgfView
            sgfHandler?.getNextBoard()?.loadInstructions()
        }

    private var sgfHandler: SgfHandler? = null

    /** Parses the [sgfString] into an [data.SgfTree], returning this [SgfController] for convenience. */
    fun load(sgfString: String): SgfController {
        sgfHandler = SgfHandler(sgfString)
        return this
    }

    /** Loads the [data.SgfTree] into the [sgfView] and subscribes to it as OnTouchListener. */
    fun into(sgfView: SgfView) {
        this.sgfView = sgfView
    }

    /** Decides what to do to the [sgfView] for each [data.SgfData] object */
    private fun List<SgfData>.loadInstructions() {
        sgfView?.apply {
            val tmpPieces = mutableListOf<Piece>()
            for (data in this@loadInstructions) {
                when (data) {
                    is Piece -> tmpPieces.add(data)
                }
            }

            pieces = tmpPieces
        }
    }

    /** Asks the [SgfHandler] for the next move, or places the piece if no such move exists. */
    override fun onMove(x: Int, y: Int) = sgfHandler?.addToBoard(x, y)?.loadInstructions() ?: Unit

    /** Undoes the last move, regardless of whether it is part of the `sgf` or not. */
    override fun onRedo() = sgfHandler?.getNextBoard()?.loadInstructions() ?: Unit

    /** Redoes the last undone move, if any, or plays the main variation otherwise. */
    override fun onUndo() = sgfHandler?.getPreviousBoard()?.loadInstructions() ?: Unit
}