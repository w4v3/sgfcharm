[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle](../index.md) / [SgfNodeHandler](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`SgfNodeHandler()`

This class processes [SgfNode](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-node.md)s given the current [SgfState](../-sgf-state/index.md) and modifies that state to include
the processed data.

The [SgfTree](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-tree/index.md) contains the incremental changes of the board from node to node, but the [GoSgfView](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-go-sgf-view/index.md)
should know about the current state of the whole board (and nothing more). Therefore, no [SgfTree](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-tree/index.md)
types should appear anywhere higher up the flow from here; only [SgfData](../-sgf-data.md) may be used to communicate
with the [GoSgfView](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-go-sgf-view/index.md).

To this end, this class consists purely of extension functions to [SgfState](../-sgf-state/index.md) which turn [SgfProperty](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-property/index.md)s
into [SgfData](../-sgf-data.md) and feed it back into the state. Most of the implementation is private, but you can
supply some game specific operations (unstable):

