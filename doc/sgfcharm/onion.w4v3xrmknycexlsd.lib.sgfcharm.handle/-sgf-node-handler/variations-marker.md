[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle](../index.md) / [SgfNodeHandler](index.md) / [variationsMarker](./variations-marker.md)

# variationsMarker

`var variationsMarker: `[`SgfState`](../-sgf-state/index.md)`.(`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<Move?>) -> `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Markup`](../-markup/index.md)`>`

receives a list of variations given as `[SgfType.Move]?`s, where each
entry corresponds to one variation `GameTree` in the order they appear in the `sgf` file.
The respective entry is the first move in the first node of the variation, or `null` if there is
no move in that node. The lambda returns a list of [Markup](../-markup/index.md) representing the board markup to apply
for each variation.

By default, this is [GoHandler.variationMarker](../-go-handler/variation-marker.md).

The different types of properties are treated in different ways:

* most of the annotation, markup and game info properties are simply wrapped up into the
appropriate [SgfData](../-sgf-data.md) structure
* move and setup properties are handled and remembered, as the user might want to go back;
also, removal of stones must be handled by the [moveHandler](move-handler.md)
* variations might be shown as markup on the board
* changes to the board size are communicated directly to the [GoSgfView](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-go-sgf-view/index.md)
* inheritable markup properties are be remembered, so that they persist and can be undone

### Property

`variationsMarker` -

receives a list of variations given as `[SgfType.Move]?`s, where each
entry corresponds to one variation `GameTree` in the order they appear in the `sgf` file.
The respective entry is the first move in the first node of the variation, or `null` if there is
no move in that node. The lambda returns a list of [Markup](../-markup/index.md) representing the board markup to apply
for each variation.



By default, this is [GoHandler.variationMarker](../-go-handler/variation-marker.md).



The different types of properties are treated in different ways:


* most of the annotation, markup and game info properties are simply wrapped up into the
appropriate [SgfData](../-sgf-data.md) structure
* move and setup properties are handled and remembered, as the user might want to go back;
also, removal of stones must be handled by the [moveHandler](move-handler.md)
* variations might be shown as markup on the board
* changes to the board size are communicated directly to the [GoSgfView](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-go-sgf-view/index.md)
* inheritable markup properties are be remembered, so that they persist and can be undone
