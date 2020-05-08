package onion.w4v3xrmknycexlsd.lib.sgfview

/**
 * Controls interaction between the [SgfView] and the [SgfHandler].
 *
 * The typical call would be `SgfController().load(someSgfString).into(someView)`. The [SgfHandler]
 * creates an [SgfTree] from the input string, and the controller requests the current sequence
 * from it. It also decides on what to do on user input.
 *
 * @property[sgfView] the [SgfView] to control.
 * @property[showVariations] whether or not to hin
 */
class SgfController(var showVariations: Boolean = true) : SgfView.OnTouchListener {
    var sgfView: SgfView? = null
        set(sgfView) {
            sgfView?.listener = this
            sgfView?.pieces = sgfHandler.getBoard()
            field = sgfView
        }

    private val sgfHandler = SgfHandler()

    /** Parses the [sgfString] into an [SgfTree], returning this [SgfController] for convenience. */
    fun load(sgfString: String): SgfController {
        sgfHandler.parseString(sgfString)
        return this
    }

    /** Loads the [SgfTree] into the [sgfView] and subscribes to it as OnTouchListener. */
    fun into(sgfView: SgfView) {
        this.sgfView = sgfView
    }

    /** Asks the [SgfHandler] for the next move, or places the piece if no such move exists. */
    override fun onMove(x: Int, y: Int) {

    }

    /** Undoes the last move, regardless of whether it is part of the `sgf` or not. */
    override fun onRedo() {

    }

    /** Redoes the last undone move, if any, or plays the main variation otherwise. */
    override fun onUndo() {

    }
}