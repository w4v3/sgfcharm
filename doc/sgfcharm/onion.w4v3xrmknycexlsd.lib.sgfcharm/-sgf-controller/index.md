[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm](../index.md) / [SgfController](./index.md)

# SgfController

`class SgfController : `[`SgfInputListener`](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-sgf-input-listener/index.md) [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/SgfController.kt#L49)

Controls interaction between the [GoSgfView](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-go-sgf-view/index.md) and [SgfTree](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-tree/index.md) objects.

The typical call would be `SgfController().load(someSgfString).into(someSgfView)`. This creates an
[SgfTree](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-tree/index.md) from the given string and loads its current representation into the view. The controller
handles the touch events from the view and supplies it with new data to display accordingly.

For this task, the controller consists of three components:

* an [SgfNavigator](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-navigator/index.md) (private), which creates and holds the [SgfTree](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-tree/index.md) from the loaded string and
from which it requests the next or previous node
* an [SgfState](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-sgf-state/index.md) object (private), which holds the data to communicate to view, as well as their
history to make undoing moves possible
* an [SgfNodeHandler](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-sgf-node-handler/index.md), which modifies the [SgfState](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-sgf-state/index.md) object according to the current node

If you want to change anything about the way the nodes are processed, you need to modify the
[sgfNodeHandler](sgf-node-handler.md). See [SgfNodeHandler](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-sgf-node-handler/index.md) for more information.

### Constructors

| [&lt;init&gt;](-init-.md) | Controls interaction between the [GoSgfView](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-go-sgf-view/index.md) and [SgfTree](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-tree/index.md) objects.`SgfController(showVariations: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`? = null)` |

### Properties

| [sgfNodeHandler](sgf-node-handler.md) | the [SgfNodeHandler](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-sgf-node-handler/index.md) used to transform the state with the information from the nodes`var sgfNodeHandler: `[`SgfNodeHandler`](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-sgf-node-handler/index.md) |
| [sgfView](sgf-view.md) | the [GoSgfView](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-go-sgf-view/index.md) to control`var sgfView: `[`SgfView`](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-sgf-view/index.md)`?` |
| [showVariations](show-variations.md) | whether or not to hint at the possible variations, overrides the settings by the `sgf`; `null` not to override them (default)`var showVariations: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`?` |

### Functions

| [into](into.md) | Loads the [SgfTree](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-tree/index.md) into the [sgfView](into.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.SgfController$into(onion.w4v3xrmknycexlsd.lib.sgfcharm.view.SgfView)/sgfView) and subscribes to it as [SgfInputListener](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-sgf-input-listener/index.md).`fun into(sgfView: `[`SgfView`](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-sgf-view/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [load](load.md) | Sets up parser and navigator for the [sgfString](load.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.SgfController$load(kotlin.String)/sgfString), returning this [SgfController](./index.md) for convenience.`fun load(sgfString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SgfController`](./index.md) |
| [onMove](on-move.md) | Finds out of there is a variation to play at move point, or otherwise simply places the piece.`fun onMove(move: Move): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onRedo](on-redo.md) | Redoes the last undone move, if any, or plays the main variation otherwise.`fun onRedo(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onUndo](on-undo.md) | Undoes the last move, regardless of whether it is part of the `sgf` or not.`fun onUndo(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

