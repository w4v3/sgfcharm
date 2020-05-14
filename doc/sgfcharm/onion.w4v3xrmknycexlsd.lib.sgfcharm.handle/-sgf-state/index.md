[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle](../index.md) / [SgfState](./index.md)

# SgfState

`class SgfState`

This class holds state relevant to the [GoSgfView](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-go-sgf-view/index.md).

Although I believe that this class is fairly generally applicable, I would not recommend using
it directly. Instead, the [SgfNodeHandler](../-sgf-node-handler/index.md) should define everything which is necessary to manipulate
this object.

### Types

| Name | Summary |
|---|---|
| [VariationMode](-variation-mode/index.md) | How to display variations.`enum class VariationMode` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | This class holds state relevant to the [GoSgfView](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-go-sgf-view/index.md).`SgfState()` |

### Properties

| Name | Summary |
|---|---|
| [currentPieces](current-pieces.md) | gets the current configuration of the board as a list of [Piece](../-piece/index.md)s`val currentPieces: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Piece`](../-piece/index.md)`>` |
| [lastMoveInfo](last-move-info.md) | the [MoveInfo](../-move-info/index.md) object from the last move played`val lastMoveInfo: `[`MoveInfo`](../-move-info/index.md)`?` |
| [numCols](num-cols.md) | the number of columns of the board`var numCols: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [numRows](num-rows.md) | the number of rows of the board`var numRows: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [showVariations](show-variations.md) | whether or not to show variations according to the `sgf``var showVariations: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [variationMode](variation-mode.md) | the [VariationMode](-variation-mode/index.md) indicating how to display variations`var variationMode: VariationMode` |

### Functions

| Name | Summary |
|---|---|
| [addInherits](add-inherits.md) | Adds the [inheritMarkups](add-inherits.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.SgfState$addInherits(kotlin.collections.List((onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.Markup)))/inheritMarkups) to the current board. They will stay on the board until that setting is cleared.`fun addInherits(inheritMarkups: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Markup`](../-markup/index.md)`>): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [addMarkups](add-markups.md) | Adds the [markups](add-markups.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.SgfState$addMarkups(kotlin.collections.List((onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.Markup)))/markups) to the current board.`fun addMarkups(markups: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Markup`](../-markup/index.md)`>): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [addNodeInfo](add-node-info.md) | Adds the [info](add-node-info.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.SgfState$addNodeInfo(onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.NodeInfo)/info) to the [NodeInfo](../-node-info/index.md) communicated to the [GoSgfView](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-go-sgf-view/index.md).`fun addNodeInfo(info: `[`NodeInfo`](../-node-info/index.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [addPiece](add-piece.md) | Adds the [piece](add-piece.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.SgfState$addPiece(onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.Piece)/piece) to the current board.`fun addPiece(piece: `[`Piece`](../-piece/index.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [removePiece](remove-piece.md) | Removes the [piece](remove-piece.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.SgfState$removePiece(onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.Piece)/piece) from the current board.`fun removePiece(piece: `[`Piece`](../-piece/index.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
