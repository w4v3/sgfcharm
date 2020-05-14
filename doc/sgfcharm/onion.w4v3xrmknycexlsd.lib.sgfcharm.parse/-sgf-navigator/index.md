[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse](../index.md) / [SgfNavigator](./index.md)

# SgfNavigator

`class SgfNavigator`

This class offers the facilities to navigate through an `sgf GameTree` given by its constructor parameter.
It calls the [SgfParser](../-sgf-parser/index.md) to get the first [SgfTree](../-sgf-tree/index.md) arising from the string, and provides
functions to obtain the next and preceding nodes as well as to list and descend into possible
variations.

The user can also navigate away from the tree by entering their own moves, in which case a new
variation is created in which the user can move back and forth. Once the last user move is undone,
the variation is deleted and [nextNode](next-node.md) will return the primary variation of the actual tree.

### Parameters

`sgfString` - the string containing the `sgf GameTree` associated with this navigator

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | creates the [SgfTree](../-sgf-tree/index.md) from the given string and initializes the navigator instance on it`SgfNavigator(sgfString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)` |

### Functions

| Name | Summary |
|---|---|
| [makeMove](make-move.md) | Carries out the [move](make-move.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfNavigator$makeMove(onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfProperty((onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType.Move)), kotlin.Int)/move) in the tree.`fun makeMove(move: `[`SgfProperty`](../-sgf-property/index.md)`<Move>, variationNumber: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null): `[`SgfNode`](../-sgf-node.md)`?` |
| [nextNode](next-node.md) | Returns the next node of the current tree, or descends into the first variation if no next node exists in the current sequence, or returns null if already at the last node of the tree.`fun nextNode(): `[`SgfNode`](../-sgf-node.md)`?` |
| [previousNode](previous-node.md) | Returns the previous node of the current tree, or ascends to the parent tree if no previous node exists in the current sequence, or returns null if already at the root node of the tree.`fun previousNode(): `[`SgfNode`](../-sgf-node.md)`?` |
| [variations](variations.md) | Returns the currently possible variations.`fun variations(successors: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<Move?>` |
