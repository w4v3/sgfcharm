[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle](../index.md) / [MoveInfo](./index.md)

# MoveInfo

`data class MoveInfo : `[`SgfData`](../-sgf-data.md) [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/handle/SgfData.kt#L50)

Information about the last move played.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Information about the last move played.`MoveInfo(moveNumber: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, lastPlaced: `[`Piece`](../-piece/index.md)`, prisoners: `[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`>)` |

### Properties

| Name | Summary |
|---|---|
| [lastPlaced](last-placed.md) | the [Piece](../-piece/index.md) played in the move`var lastPlaced: `[`Piece`](../-piece/index.md) |
| [moveNumber](move-number.md) | the number of the move`var moveNumber: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [prisoners](prisoners.md) | a pair of Black and White prisoner counts`var prisoners: `[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`>` |
