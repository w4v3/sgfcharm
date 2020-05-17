[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle](../index.md) / [SgfNodeHandler](./index.md)

# SgfNodeHandler

`class SgfNodeHandler` [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/handle/SgfNodeHandler.kt#L82)

This class processes [SgfNode](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-node.md)s given the current [SgfState](../-sgf-state/index.md) and modifies that state to include
the processed data.

The [SgfTree](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-tree/index.md) contains the incremental changes of the board from node to node, but the [GoSgfView](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-go-sgf-view/index.md)
should know about the current state of the whole board (and nothing more). Therefore, no [SgfTree](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-tree/index.md)
types should appear anywhere higher up the flow from here; only [SgfData](../-sgf-data.md) may be used to communicate
with the [GoSgfView](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-go-sgf-view/index.md).

To this end, this class consists purely of extension functions to [SgfState](../-sgf-state/index.md) which turn [SgfProperty](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-property/index.md)s
into [SgfData](../-sgf-data.md) and feed it back into the state. Most of the implementation is private, but you can
supply some game specific operations (unstable):

### Constructors

| [&lt;init&gt;](-init-.md) | This class processes [SgfNode](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-node.md)s given the current [SgfState](../-sgf-state/index.md) and modifies that state to include the processed data.`SgfNodeHandler()` |

### Properties

| [customPropertyHandler](custom-property-handler.md) | receives the identifier and value of a custom property (see [SgfProperty.CUSTOM](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-property/-c-u-s-t-o-m/index.md)) along with the current [SgfState](../-sgf-state/index.md). You are free to do with it whatever you want; typically you would call [SgfState.addNodeInfo](../-sgf-state/add-node-info.md) on the state to communicate arbitrary information to the view.`var customPropertyHandler: `[`SgfState`](../-sgf-state/index.md)`.(`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [moveHandler](move-handler.md) | modifies the given state by making the given move of the given color, returning a [MoveInfo](../-move-info/index.md) object for the move executed or `null` if the move was invalid. When implementing this function, it is your responsibility to make [SgfState.addPiece](../-sgf-state/add-piece.md) and [SgfState.removePiece](../-sgf-state/remove-piece.md) calls on the [SgfState](../-sgf-state/index.md) object passed. Note that if the move involves moving a piece from one position to another, you must add it to the final position and remove it from the previous one. You also need to increase move count and prisoner counts in the [MoveInfo](../-move-info/index.md) object you return, for which you may want to use the info provided by [SgfState.lastMoveInfo](../-sgf-state/last-move-info.md).`var moveHandler: `[`SgfState`](../-sgf-state/index.md)`.(Value, Move) -> `[`MoveInfo`](../-move-info/index.md)`?` |
| [variationsMarker](variations-marker.md) | receives a list of variations given as `[SgfType.Move]?`s, where each entry corresponds to one variation `GameTree` in the order they appear in the `sgf` file. The respective entry is the first move in the first node of the variation, or `null` if there is no move in that node. The lambda returns a list of [Markup](../-markup/index.md) representing the board markup to apply for each variation.`var variationsMarker: `[`SgfState`](../-sgf-state/index.md)`.(`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<Move?>) -> `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Markup`](../-markup/index.md)`>` |

