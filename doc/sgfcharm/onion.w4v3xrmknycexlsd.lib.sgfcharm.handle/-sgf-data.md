[sgfcharm](../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle](index.md) / [SgfData](./-sgf-data.md)

# SgfData

`sealed class SgfData`

Base class for all instructions understandable by [GoSgfView](../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-go-sgf-view/index.md).

This is to cast the semantics of the `sgf` format into the semantics of what actually
needs to be drawn in the end, which might be different. For example, the [GoSgfView](../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-go-sgf-view/index.md) always
needs a complete representation of the current board configuration, but each `sgf` node
contains only incremental changes to the board.

### Inheritors

| Name | Summary |
|---|---|
| [Markup](-markup/index.md) | Markup of nodes on the board.`data class Markup : `[`SgfData`](./-sgf-data.md) |
| [MoveInfo](-move-info/index.md) | Information about the last move played.`data class MoveInfo : `[`SgfData`](./-sgf-data.md) |
| [NodeInfo](-node-info/index.md) | A piece of information relating to the current node to be displayed in the text window.`data class NodeInfo : `[`SgfData`](./-sgf-data.md) |
| [Piece](-piece/index.md) | A gaming piece.`data class Piece : `[`SgfData`](./-sgf-data.md) |
