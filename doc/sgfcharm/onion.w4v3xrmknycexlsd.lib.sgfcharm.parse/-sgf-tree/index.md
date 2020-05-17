[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse](../index.md) / [SgfTree](./index.md)

# SgfTree

`data class SgfTree` [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/parse/SgfTree.kt#L43)

Representation of the `sgf GameTree`, which consists of an `Sequence`, and zero or more children `GameTree`s.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Representation of the `sgf GameTree`, which consists of an `Sequence`, and zero or more children `GameTree`s.`SgfTree(parent: `[`SgfTree`](./index.md)`? = null, nodes: `[`SgfSequence`](../-sgf-sequence.md)` = mutableListOf(), children: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`SgfTree`](./index.md)`> = mutableListOf())` |

### Properties

| Name | Summary |
|---|---|
| [children](children.md) | the child `GameTree`s`val children: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`SgfTree`](./index.md)`>` |
| [nodes](nodes.md) | the nodes making up the `sgf Sequence` of the tree`val nodes: `[`SgfSequence`](../-sgf-sequence.md) |
| [parent](parent.md) | a reference to its parent tree for backtracking.`var parent: `[`SgfTree`](./index.md)`?` |
