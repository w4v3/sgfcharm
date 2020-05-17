[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse](../index.md) / [GoCoordinateParser](./index.md)

# GoCoordinateParser

`object GoCoordinateParser : CoordinateParser<XYPoint>` [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/parse/CoordinateParsers.kt#L34)

A coordinate parser for the Go game.

The `Point`, `Move` and `Stone` data types are all the same in Go. A `Point` consists of two
letters, 'a'..'z' + 'A'..'Z', where the first letter represents the column and the second one
the row (see [SgfType.Point](../-sgf-type/-point/index.md) for details).

### Functions

| [parseMove](parse-move.md) | Parses the [from](../-sgf-parser/-coordinate-parser/parse-move.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfParser.CoordinateParser$parseMove(kotlin.String)/from) string representing an `sgf Move` into an [Move](../-sgf-type/-move/index.md) object.`fun parseMove(from: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): XYMove` |
| [parsePoint](parse-point.md) | Parses the [from](../-sgf-parser/-coordinate-parser/parse-point.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfParser.CoordinateParser$parsePoint(kotlin.String)/from) string representing an `sgf Point` into an [Point](../-sgf-type/-point/index.md) object.`fun parsePoint(from: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): XYPoint?` |
| [parseStone](parse-stone.md) | Parses the [from](../-sgf-parser/-coordinate-parser/parse-stone.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfParser.CoordinateParser$parseStone(kotlin.String)/from) string representing an `sgf Stone` into an [Stone](../-sgf-type/-stone/index.md) object.`fun parseStone(from: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): XYStone?` |
| [parseXYCoordinate](parse-x-y-coordinate.md) | Parses this string as a Go coordinate.`fun `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`.parseXYCoordinate(): `[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`>?` |
| [rangeTo](range-to.md) | Returns a list of [Point](../-sgf-type/-point/index.md)s contained in a rectangle between [this](../-sgf-parser/-coordinate-parser/range-to/-this-.md) upper left and the [other](../-sgf-parser/-coordinate-parser/range-to.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfParser.CoordinateParser$rangeTo(onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfParser.CoordinateParser.T, onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfParser.CoordinateParser.T)/other) lower right [Point](../-sgf-type/-point/index.md). Returns an empty list if that rectangle is empty, including the case where the other point lies to the left or above this point.`fun XYPoint.rangeTo(other: XYPoint): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<XYPoint>` |

