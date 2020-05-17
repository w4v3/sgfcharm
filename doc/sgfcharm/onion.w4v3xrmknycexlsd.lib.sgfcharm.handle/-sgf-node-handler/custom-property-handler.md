[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle](../index.md) / [SgfNodeHandler](index.md) / [customPropertyHandler](./custom-property-handler.md)

# customPropertyHandler

`var customPropertyHandler: `[`SgfState`](../-sgf-state/index.md)`.(`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/handle/SgfNodeHandler.kt#L252)

receives the identifier and value of a custom property (see
[SgfProperty.CUSTOM](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-property/-c-u-s-t-o-m/index.md)) along with the current [SgfState](../-sgf-state/index.md). You are free to do with it whatever
you want; typically you would call [SgfState.addNodeInfo](../-sgf-state/add-node-info.md) on the state to communicate arbitrary
information to the view.

By default, this does nothing.

### Property

`customPropertyHandler` -

receives the identifier and value of a custom property (see
[SgfProperty.CUSTOM](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-property/-c-u-s-t-o-m/index.md)) along with the current [SgfState](../-sgf-state/index.md). You are free to do with it whatever
you want; typically you would call [SgfState.addNodeInfo](../-sgf-state/add-node-info.md) on the state to communicate arbitrary
information to the view.



By default, this does nothing.

