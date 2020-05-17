[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse](../index.md) / [SgfParser](./index.md)

# SgfParser

`class SgfParser` [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/parse/SgfParser.kt#L59)

Parser for reading `sgf` strings into [SgfCollection](../-sgf-collection.md)s.

It is very lenient, in that it will not throw errors for incorrect input, but it will try to
parse it as good as it can. Of course, that means that for incorrect input, the results might be
completely wrong (it will not try to correct anything).

Note that the parser returns all `GameTree`s represented in the string, i.e., it returns a
collection. This is only to make the parser more reusable; the other components ignore all but the
first tree in the collection.

Only the current `sgf` version `FF[4]` is supported, and only coordinate parsing for Go
is implemented. Unknown properties will be included in the output as [CUSTOM](../-sgf-property/-c-u-s-t-o-m/index.md) types.
Thus, if you need to handle old file formats or game-specific properties for games other than Go,
you can run the parser as it is and handle the [CUSTOM](../-sgf-property/-c-u-s-t-o-m/index.md) properties manually later on.

Most of the parser is private as it simply implements the FF[4](#) specification. However, since
the parsing of [Point](../-sgf-type/-point/index.md), [Move](../-sgf-type/-move/index.md) and [Stone](../-sgf-type/-stone/index.md) is game-dependent, there is
a possibility of implementing the [SgfParser.CoordinateParser](-coordinate-parser/index.md) interface and setting the
[coordinateParser](coordinate-parser.md) property to the parser to be used.

### Types

| [CoordinateParser](-coordinate-parser/index.md) | The class to extend for parsing of the property values for the game-specific [Point](../-sgf-type/-point/index.md), [Move](../-sgf-type/-move/index.md) and [Stone](../-sgf-type/-stone/index.md) types.`abstract class CoordinateParser<T : Point>` |

### Constructors

| [&lt;init&gt;](-init-.md) | initializes the parser with the given [SgfParser.CoordinateParser](-coordinate-parser/index.md)`SgfParser(coordinateParser: CoordinateParser<*> = GoCoordinateParser)` |

### Properties

| [coordinateParser](coordinate-parser.md) | the [SgfParser.CoordinateParser](-coordinate-parser/index.md) to be used for game-specific parsing; default is [GoCoordinateParser](../-go-coordinate-parser/index.md)`var coordinateParser: CoordinateParser<*>` |

### Functions

| [parseSgfCollection](parse-sgf-collection.md) | Parses the [sgfString](parse-sgf-collection.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfParser$parseSgfCollection(kotlin.String)/sgfString) representing an `sgf GameTree` into an [SgfCollection](../-sgf-collection.md) object.`fun parseSgfCollection(sgfString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SgfCollection`](../-sgf-collection.md) |

