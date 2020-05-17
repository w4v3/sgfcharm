[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle](../index.md) / [SgfNodeHandler](index.md) / [moveHandler](./move-handler.md)

# moveHandler

`var moveHandler: `[`SgfState`](../-sgf-state/index.md)`.(Value, Move) -> `[`MoveInfo`](../-move-info/index.md)`?` [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/handle/SgfNodeHandler.kt#L248)

modifies the given state by making the given move of the given color, returning
a [MoveInfo](../-move-info/index.md) object for the move executed or `null` if the move was invalid.
When implementing this function, it is your responsibility to make [SgfState.addPiece](../-sgf-state/add-piece.md) and
[SgfState.removePiece](../-sgf-state/remove-piece.md) calls on the [SgfState](../-sgf-state/index.md) object passed. Note that if the move involves
moving a piece from one position to another, you must add it to the final position and remove
it from the previous one. You also need to increase move count and prisoner counts in the [MoveInfo](../-move-info/index.md)
object you return, for which you may want to use the info provided by [SgfState.lastMoveInfo](../-sgf-state/last-move-info.md).

In case of a pass, you should still return a [MoveInfo](../-move-info/index.md) instance, but with [Piece.stone](../-piece/stone.md) of [MoveInfo.lastPlaced](../-move-info/last-placed.md)
set to `null`.

By default, this is [GoNodeHandler.moveHandler](../-go-node-handler/move-handler.md).

### Property

`moveHandler` -

modifies the given state by making the given move of the given color, returning
a [MoveInfo](../-move-info/index.md) object for the move executed or `null` if the move was invalid.
When implementing this function, it is your responsibility to make [SgfState.addPiece](../-sgf-state/add-piece.md) and
[SgfState.removePiece](../-sgf-state/remove-piece.md) calls on the [SgfState](../-sgf-state/index.md) object passed. Note that if the move involves
moving a piece from one position to another, you must add it to the final position and remove
it from the previous one. You also need to increase move count and prisoner counts in the [MoveInfo](../-move-info/index.md)
object you return, for which you may want to use the info provided by [SgfState.lastMoveInfo](../-sgf-state/last-move-info.md).



In case of a pass, you should still return a [MoveInfo](../-move-info/index.md) instance, but with [Piece.stone](../-piece/stone.md) of [MoveInfo.lastPlaced](../-move-info/last-placed.md)
set to `null`.



By default, this is [GoNodeHandler.moveHandler](../-go-node-handler/move-handler.md).

