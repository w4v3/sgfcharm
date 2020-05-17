[sgfcharm](../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse](./index.md)

## Package onion.w4v3xrmknycexlsd.lib.sgfcharm.parse

This package contains the `sgf` parser and the library data models:

* the [SgfTree](-sgf-tree/index.md) is the internal representation of the SGF `GameTree`, with a strong type system
* the [SgfParser](-sgf-parser/index.md) exposes the [SgfParser.parseSgfCollection](-sgf-parser/parse-sgf-collection.md) function, which reads a string into a list of [SgfTree](-sgf-tree/index.md)s
* the [SgfNavigator](-sgf-navigator/index.md) exposes functions to navigate through the tree node by node

### Types

| Name | Summary |
|---|---|
| [GoCoordinateParser](-go-coordinate-parser/index.md) | A coordinate parser for the Go game.`object GoCoordinateParser : CoordinateParser<XYPoint>` |
| [SgfCollection](-sgf-collection.md) | Representation of the `sgf Collection`, which is a list of `GameTree`s.`typealias SgfCollection = `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SgfTree`](-sgf-tree/index.md)`>` |
| [SgfNavigator](-sgf-navigator/index.md) | This class offers the facilities to navigate through an `sgf GameTree` given by its constructor parameter. It calls the [SgfParser](-sgf-parser/index.md) to get the first [SgfTree](-sgf-tree/index.md) arising from the string, and provides functions to obtain the next and preceding nodes as well as to list and descend into possible variations.`class SgfNavigator` |
| [SgfNode](-sgf-node.md) | Representation of the `sgf Node`, which is a list of `Property`s.`typealias SgfNode = `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`SgfProperty`](-sgf-property/index.md)`<`[`SgfType`](-sgf-type/index.md)`>>` |
| [SgfParser](-sgf-parser/index.md) | Parser for reading `sgf` strings into [SgfCollection](-sgf-collection.md)s.`class SgfParser` |
| [SgfProperty](-sgf-property/index.md) | Base class for all `sgf Property`s, with a subclass for each particular property.`sealed class SgfProperty<out T : `[`SgfType`](-sgf-type/index.md)`>` |
| [SgfSequence](-sgf-sequence.md) | Representation of the `sgf Sequence`, which is a list of `Node`s.`typealias SgfSequence = `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`SgfNode`](-sgf-node.md)`>` |
| [SgfTree](-sgf-tree/index.md) | Representation of the `sgf GameTree`, which consists of an `Sequence`, and zero or more children `GameTree`s.`data class SgfTree` |
| [SgfType](-sgf-type/index.md) | This is the base class for all `sgf` data types, for strong typing.`sealed class SgfType` |

### Extensions for External Classes

| Name | Summary |
|---|---|
| [kotlin.String](kotlin.-string/index.md) |  |

### Functions

| Name | Summary |
|---|---|
| [not](not.md) | Switches this [Color.Value](-sgf-type/-color/-value/index.md) from black to white and vice versa.`operator fun Value?.not(): Value?` |
