[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse](../index.md) / [GoCoordinateParser](index.md) / [parseMove](./parse-move.md)

# parseMove

`fun parseMove(from: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): XYMove` [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/parse/CoordinateParsers.kt#L41)

Parses the [from](../-sgf-parser/-coordinate-parser/parse-move.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfParser.CoordinateParser$parseMove(kotlin.String)/from) string representing an `sgf Move` into an [Move](../-sgf-type/-move/index.md) object.

Should return `null` if the move is a pass or input is invalid (which will be treated as pass).

