[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse](../index.md) / [SgfNavigator](index.md) / [variations](./variations.md)

# variations

`fun variations(successors: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<Move?>` [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/parse/SgfNavigator.kt#L181)

Returns the currently possible variations.

If [successors](variations.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfNavigator$variations(kotlin.Boolean)/successors) is set to `true` (default), the variations of the successor node are returned,
otherwise, the variations of the current node. The returned value is a list of
`[SgfType.Move]?`s, each of which represents a variation in the order in which
they appear in the tree. If the variation does not contain a move, the corresponding entry is `null`.

