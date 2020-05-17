[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse](../index.md) / [SgfNavigator](index.md) / [makeMove](./make-move.md)

# makeMove

`fun makeMove(move: `[`SgfProperty`](../-sgf-property/index.md)`<Move>, variationNumber: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null): `[`SgfNode`](../-sgf-node.md)`?` [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/parse/SgfNavigator.kt#L95)

Carries out the [move](make-move.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfNavigator$makeMove(onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfProperty((onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType.Move)), kotlin.Int)/move) in the tree.

There are three possible situations:

* the next node in the tree has the [move](make-move.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfNavigator$makeMove(onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfProperty((onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType.Move)), kotlin.Int)/move): move to that node
* we are at the last node of the current sequence and there are variation trees, one of which
contains the [move](make-move.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfNavigator$makeMove(onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfProperty((onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType.Move)), kotlin.Int)/move): move to the tree containing it
* neither of the above: the user is branching off, a new variation is created

If [variationNumber](make-move.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfNavigator$makeMove(onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfProperty((onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType.Move)), kotlin.Int)/variationNumber) is not `null`, the navigator is forced to move to the variation with
that index if it exists. This is useful for letting the user navigate to variations without
moves in their root nodes, for example by displaying markup at custom places on the board.

