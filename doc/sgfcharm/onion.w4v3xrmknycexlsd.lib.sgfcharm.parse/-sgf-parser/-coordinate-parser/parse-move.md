[sgfcharm](../../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse](../../index.md) / [SgfParser](../index.md) / [CoordinateParser](index.md) / [parseMove](./parse-move.md)

# parseMove

`abstract fun parseMove(from: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): Move` [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/parse/SgfParser.kt#L102)

Parses the [from](parse-move.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfParser.CoordinateParser$parseMove(kotlin.String)/from) string representing an `sgf Move` into an [Move](../../-sgf-type/-move/index.md) object.

Should return `null` if the move is a pass or input is invalid (which will be treated as pass).

