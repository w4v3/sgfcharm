package onion.w4v3xrmknycexlsd.lib.sgfcharmer.handle

import onion.w4v3xrmknycexlsd.lib.sgfcharmer.Impl
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.parse.*
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.SgfInfoKeys
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.view.GoSgfView

/** See [SgfNodeHandler.moveHandler]. */
typealias SgfMoveHandler = (SgfState, SgfType.Color.Value, SgfType.Move) -> MoveInfo?
/** See [SgfNodeHandler.customPropertyHandler]. */
typealias SgfCustomPropertyHandler = (SgfState, String, String) -> Unit
/** See [SgfNodeHandler.variationsMarker]. */
typealias SgfVariationsMarker = (SgfState, List<SgfType.Move?>) -> List<Markup>

/**
 * This class processes [SgfNode]s given the current [SgfState] and modifies that state to include
 * the processed data.
 *
 * The [SgfTree] contains the incremental changes of the board from node to node, but the [GoSgfView]
 * should know about the current state of the whole board (and nothing more). Therefore, no [SgfTree]
 * types should appear anywhere higher up the flow from here; only [SgfData] may be used to communicate
 * with the [GoSgfView].
 *
 * To this end, this class consists purely of extension functions to [SgfState] which turn [SgfProperty]s
 * into [SgfData] and feed it back into the state. Most of the implementation is private, but you can
 * supply some game specific operations (unstable):
 *
 * @property[moveHandler] modifies the given state by making the given move of the given color, returning
 * a [MoveInfo] object for the move executed or `null` if the move was invalid.
 * When implementing this function, it is your responsibility to make [SgfState.addPiece] and
 * [SgfState.removePiece] calls on the [SgfState] object passed. Note that if the move involves
 * moving a piece from one position to another, you must add it to the final position and remove
 * it from the previous one. You also need to increase move count and prisoner counts in the [MoveInfo]
 * object you return, for which you may want to use the info provided by [SgfState.lastMoveInfo].
 *
 * By default, this is [GoHandler.moveHandler].
 *
 * @property[customPropertyHandler] receives the identifier and value of a custom property (see
 * [SgfProperty.CUSTOM]) along with the current [SgfState]. You are free to do with it whatever
 * you want; typically you would call [SgfState.addNodeInfo] on the state to communicate arbitrary
 * information to the view.
 *
 * By default, this does nothing.
 *
 * @property[variationsMarker] receives a list of variations given as `[SgfType.Move]?`s, where each
 * entry corresponds to one variation `GameTree` in the order they appear in the `sgf` file.
 * The respective entry is the first move in the first node of the variation, or `null` if there is
 * no move in that node. The lambda returns a list of [Markup] representing the board markup to apply
 * for each variation.
 *
 * By default, this is [GoHandler.variationMarker].
 *
 * The different types of properties require different treatment:
 * - most of the annotation, markup and game info properties can simply be wrapped up into the
 * appropriate [SgfData] structure
 * - move and setup properties need to be handled and remembered, as the user might want to go back;
 * also, removal of stones must be handled
 * - variations might be shown as markup on the board
 * - root properties can influence the further processing, and the board size must be communicated
 * to the [GoSgfView]
 * - inheritable markup properties must be remembered, so that they persist and can be undone
 * - some information, such as move numbers and prisoner counts, are not in the `sgf` file, but they
 * need to be retrieved and calculated somehow
 */
class SgfNodeHandler {
    internal fun SgfState.processNode(node: SgfNode) {
        initStep() // prepare the state for a new node
        for (property in node) {
            when (property) {
                is SgfProperty.B -> makeMove(SgfType.Color.Value.BLACK, property.value)
                is SgfProperty.W -> makeMove(SgfType.Color.Value.WHITE, property.value)
                SgfProperty.KO -> {
                } // irrelevant for viewers
                is SgfProperty.MN -> setMoveNumber(property.value)
                is SgfProperty.AB -> addStones(SgfType.Color.Value.BLACK, property.value)
                is SgfProperty.AW -> addStones(SgfType.Color.Value.WHITE, property.value)
                is SgfProperty.AE -> removePoints(property.value)
                is SgfProperty.PL -> setColor(property.value)
                is SgfProperty.C -> addNodeInfo(SgfInfoKeys.C, property.value.text)
                is SgfProperty.DM -> addNodeInfo(SgfInfoKeys.DM[property.value.value])
                is SgfProperty.GB -> addNodeInfo(SgfInfoKeys.DM[property.value.value])
                is SgfProperty.GW -> addNodeInfo(SgfInfoKeys.GW[property.value.value])
                is SgfProperty.HO -> addNodeInfo(SgfInfoKeys.HO[property.value.value])
                is SgfProperty.N -> addNodeInfo(property.value.text, SgfInfoKeys.N)
                is SgfProperty.UC -> addNodeInfo(SgfInfoKeys.UC[property.value.value])
                is SgfProperty.V -> addNodeInfo(SgfInfoKeys.V, "${property.value.number}")
                is SgfProperty.BM -> addNodeInfo(SgfInfoKeys.BM[property.value.value])
                SgfProperty.DO -> addNodeInfo(SgfInfoKeys.DO)
                SgfProperty.IT -> addNodeInfo(SgfInfoKeys.IT)
                is SgfProperty.TE -> addNodeInfo(SgfInfoKeys.TE[property.value.value])
                is SgfProperty.AR -> addComposeMarkup(MarkupType.ARROW, property.value)
                is SgfProperty.CR -> addPointMarkup(MarkupType.CIRCLE, property.value)
                is SgfProperty.DD -> addPointInherits(MarkupType.DIM, property.value)
                is SgfProperty.LB -> addLabelMarkup(MarkupType.LABEL, property.value)
                is SgfProperty.LN -> addComposeMarkup(MarkupType.LINE, property.value)
                is SgfProperty.MA -> addPointMarkup(MarkupType.X, property.value)
                is SgfProperty.SL -> addPointMarkup(MarkupType.SELECT, property.value)
                is SgfProperty.SQ -> addPointMarkup(MarkupType.SQUARE, property.value)
                is SgfProperty.TR -> addPointMarkup(MarkupType.TRIANGLE, property.value)
                is SgfProperty.AP -> {
                    addNodeInfo(SgfInfoKeys.APN, property.value.first.text)
                    addNodeInfo(SgfInfoKeys.APV, property.value.second.text)
                }
                is SgfProperty.CA -> {
                } // I'm sorry but it should be UTF-8
                is SgfProperty.FF -> {
                } // I'm sorry but it should be FF[4]
                is SgfProperty.GM -> {
                } // no automatic file format detection yet, as only go is implemented
                is SgfProperty.ST -> configureVariations(property.value.number)
                is SgfProperty.SZ -> setBoardSize(property.value)
                is SgfProperty.AN -> addNodeInfo(SgfInfoKeys.AN, property.value.text)
                is SgfProperty.BR -> addNodeInfo(SgfInfoKeys.BR, property.value.text)
                is SgfProperty.BT -> addNodeInfo(SgfInfoKeys.BT, property.value.text)
                is SgfProperty.CP -> addNodeInfo(SgfInfoKeys.CP, property.value.text)
                is SgfProperty.DT -> addNodeInfo(SgfInfoKeys.DT, property.value.text)
                is SgfProperty.EV -> addNodeInfo(SgfInfoKeys.EV, property.value.text)
                is SgfProperty.GN -> addNodeInfo(SgfInfoKeys.GN, property.value.text)
                is SgfProperty.GC -> addNodeInfo(SgfInfoKeys.GC, property.value.text)
                is SgfProperty.ON -> addNodeInfo(SgfInfoKeys.ON, property.value.text)
                is SgfProperty.OT -> addNodeInfo(SgfInfoKeys.OT, property.value.text)
                is SgfProperty.PB -> addNodeInfo(SgfInfoKeys.PB, property.value.text)
                is SgfProperty.PC -> addNodeInfo(SgfInfoKeys.PC, property.value.text)
                is SgfProperty.PW -> addNodeInfo(SgfInfoKeys.PW, property.value.text)
                is SgfProperty.RE -> addNodeInfo(SgfInfoKeys.RE, property.value.text)
                is SgfProperty.RO -> addNodeInfo(SgfInfoKeys.RO, property.value.text)
                is SgfProperty.RU -> addNodeInfo(SgfInfoKeys.RU, property.value.text)
                is SgfProperty.SO -> addNodeInfo(SgfInfoKeys.SO, property.value.text)
                is SgfProperty.US -> addNodeInfo(SgfInfoKeys.US, property.value.text)
                is SgfProperty.WR -> addNodeInfo(SgfInfoKeys.WR, property.value.text)
                is SgfProperty.WT -> addNodeInfo(SgfInfoKeys.WT, property.value.text)
                is SgfProperty.TM -> addNodeInfo(SgfInfoKeys.TM, "${property.value.number}")
                is SgfProperty.BL -> addNodeInfo(SgfInfoKeys.BL, "${property.value.number}")
                is SgfProperty.OB -> addNodeInfo(SgfInfoKeys.OB, "${property.value.number}")
                is SgfProperty.OW -> addNodeInfo(SgfInfoKeys.OW, "${property.value.number}")
                is SgfProperty.WL -> addNodeInfo(SgfInfoKeys.WL, "${property.value.number}")
                is SgfProperty.HA -> addNodeInfo(SgfInfoKeys.HA, "${property.value.number}")
                is SgfProperty.KM -> addNodeInfo(SgfInfoKeys.KM, "${property.value.number}")
                is SgfProperty.FG -> {
                } // the View is not suitable for printing
                is SgfProperty.PM -> {
                } // the View is not suitable for printing
                is SgfProperty.VW -> addPointInherits(MarkupType.VISIBLE, property.value)
                is SgfProperty.TB -> addPointMarkup(MarkupType.BLACK_TERRITORY, property.value)
                is SgfProperty.TW -> addPointMarkup(MarkupType.WHITE_TERRITORY, property.value)
                is SgfProperty.CUSTOM -> customPropertyHandler(
                    property.value.first.text,
                    property.value.second.text
                )
            }
        }
    }

    private fun SgfState.makeMove(colorValue: SgfType.Color.Value, move: SgfType.Move) =
        moveHandler(colorValue, move)?.let { addMoveInfo(it) }

    // add stones without checking
    private fun SgfState.addStones(
        colorValue: SgfType.Color.Value,
        stones: SgfType.List<SgfType.Stone>
    ) =
        addPieces(stones.map { Piece(colorValue, it) })

    // remove pieces if they are at the specified points
    private fun SgfState.removePoints(points: SgfType.List<SgfType.Point>) {
        val board = currentPieces
        removePieces(
            points.flatMap { point ->
                board.filter { it.stone.point == point }
            }
        )
    }

    private fun SgfState.setMoveNumber(number: SgfType.Number) {
        moveNumberJustSet = number.number
    }

    private fun SgfState.setColor(color: SgfType.Color) {
        colorJustSet = color.value
        addNodeInfo(NodeInfo(SgfInfoKeys.PL[color.value]))
    }

    private fun SgfState.setBoardSize(dimensions: SgfType.Compose<SgfType.Number, SgfType.Number>) =
        dimensions.let { (c, r) -> numCols = c.number; numRows = r.number }


    // the passed [value] is the sum of 0/1 for successor/sibling and 0/2 for show on/off
    private fun SgfState.configureVariations(value: Int) {
        variationMode = when {
            value % 2 == 0 -> SgfState.VariationMode.SUCCESSORS
            else -> SgfState.VariationMode.SIBLINGS
        }
        showVariations = value < 2
    }

    private fun SgfState.addPointMarkup(type: MarkupType, values: SgfType.List<SgfType.Point>) =
        addMarkups(values.map { Markup(type, it) })

    private fun SgfState.addComposeMarkup(
        type: MarkupType,
        values: SgfType.List<SgfType.Compose<SgfType.Point, SgfType.Point>>
    ) =
        addMarkups(values.map {
            Markup(
                type,
                it.first,
                it.second
            )
        })

    private fun SgfState.addLabelMarkup(
        type: MarkupType,
        values: SgfType.List<SgfType.Compose<SgfType.Point, SgfType.SimpleText>>
    ) =
        addMarkups(values.map {
            Markup(
                type,
                it.first,
                label = it.second.text
            )
        })

    private fun SgfState.addPointInherits(type: MarkupType, values: SgfType.List<SgfType.Point>) =
        addInherits(values.map { Markup(type, it) })

    private fun SgfState.addNodeInfo(key: String? = null, message: String? = null) =
        addNodeInfo(NodeInfo(key, message))

    @Impl
    public var moveHandler: SgfState.(SgfType.Color.Value, SgfType.Move) -> MoveInfo? =
        GoHandler.moveHandler

    @Impl
    public var customPropertyHandler: SgfState.(String, String) -> Unit = { _, _ -> }

    @Impl
    public var variationsMarker: SgfState.(List<SgfType.Move?>) -> List<Markup> =
        GoHandler.variationMarker
}
