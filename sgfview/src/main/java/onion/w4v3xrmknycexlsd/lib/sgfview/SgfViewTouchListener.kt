package onion.w4v3xrmknycexlsd.lib.sgfview

/** Interface for communicating touch events from the view to the controller. */
interface SgfViewTouchListener {
    /** Triggered when user has touched the board at grid coordinates [x] and [y]. */
    fun onMove(x: Int, y: Int)

    /** Triggered when user has touched the undo button. */
    fun onUndo()

    /** Triggered when user has touched the redo button. */
    fun onRedo()
}