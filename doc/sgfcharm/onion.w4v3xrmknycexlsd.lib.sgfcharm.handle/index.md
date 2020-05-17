[sgfcharm](../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle](./index.md)

## Package onion.w4v3xrmknycexlsd.lib.sgfcharm.handle

This package may be seen as an extension to the [SgfController](../onion.w4v3xrmknycexlsd.lib.sgfcharm/-sgf-controller/index.md), which makes use of these components:

* the [SgfData](-sgf-data.md) class defines the types of instructions the [SgfView](../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-sgf-view/index.md) actually understands
* the [SgfState](-sgf-state/index.md) class holds [SgfData](-sgf-data.md) representing the current state of the view
* the [SgfNodeHandler](-sgf-node-handler/index.md) defines extension functions to [SgfState](-sgf-state/index.md), which are used to process
each node of the [SgfTree](../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-tree/index.md) by turning it into [SgfData](-sgf-data.md) and modifying the [SgfState](-sgf-state/index.md) with it

### Types

| Name | Summary |
|---|---|
| [BoardConfig](-board-config/index.md) | Communicates the current board dimensions.`data class BoardConfig : `[`SgfData`](-sgf-data.md) |
| [GoNodeHandler](-go-node-handler/index.md) | An [SgfNodeHandler](-sgf-node-handler/index.md) for Go.`object GoNodeHandler` |
| [Markup](-markup/index.md) | Markup of nodes on the board.`data class Markup : `[`SgfData`](-sgf-data.md) |
| [MarkupType](-markup-type/index.md) | The possible values for board Markup.`enum class MarkupType` |
| [MoveInfo](-move-info/index.md) | Information about the last move played.`data class MoveInfo : `[`SgfData`](-sgf-data.md) |
| [NodeInfo](-node-info/index.md) | A piece of information relating to the current node to be displayed in the text window.`data class NodeInfo : `[`SgfData`](-sgf-data.md) |
| [Piece](-piece/index.md) | A gaming piece.`data class Piece : `[`SgfData`](-sgf-data.md) |
| [SgfCustomPropertyHandler](-sgf-custom-property-handler.md) | See [SgfNodeHandler.customPropertyHandler](-sgf-node-handler/custom-property-handler.md).`typealias SgfCustomPropertyHandler = (`[`SgfState`](-sgf-state/index.md)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [SgfData](-sgf-data.md) | Base class for all instructions understandable by [GoSgfView](../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-go-sgf-view/index.md).`sealed class SgfData` |
| [SgfMoveHandler](-sgf-move-handler.md) | See [SgfNodeHandler.moveHandler](-sgf-node-handler/move-handler.md).`typealias SgfMoveHandler = (`[`SgfState`](-sgf-state/index.md)`, Value, Move) -> `[`MoveInfo`](-move-info/index.md)`?` |
| [SgfNodeHandler](-sgf-node-handler/index.md) | This class processes [SgfNode](../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-node.md)s given the current [SgfState](-sgf-state/index.md) and modifies that state to include the processed data.`class SgfNodeHandler` |
| [SgfState](-sgf-state/index.md) | This class holds state relevant to the [GoSgfView](../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-go-sgf-view/index.md).`class SgfState` |
| [SgfVariationsMarker](-sgf-variations-marker.md) | See [SgfNodeHandler.variationsMarker](-sgf-node-handler/variations-marker.md).`typealias SgfVariationsMarker = (`[`SgfState`](-sgf-state/index.md)`, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<Move?>) -> `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Markup`](-markup/index.md)`>` |
