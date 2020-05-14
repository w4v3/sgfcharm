[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm](../index.md) / [SgfController](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`SgfController(showVariations: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`? = null)`

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

