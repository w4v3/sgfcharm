

This is the SgfView library for rendering `sgf` files.

### All Types

| Name | Summary |
|---|---|
|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.BoardConfig](../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-board-config/index.md)

Communicates the current board dimensions.


| (extensions in package onion.w4v3xrmknycexlsd.lib.sgfcharm)

##### [android.os.Bundle](../onion.w4v3xrmknycexlsd.lib.sgfcharm/android.os.-bundle/index.md)


| (extensions in package onion.w4v3xrmknycexlsd.lib.sgfcharm.view)

##### [android.graphics.Canvas](../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/android.graphics.-canvas/index.md)


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.view.DefaultSgfDrawer](../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-default-sgf-drawer/index.md)

Default implementation of the [SgfDrawer](../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-sgf-drawer/index.md) interface, used by [GoSgfView](../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-go-sgf-view/index.md).


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.GoCoordinateParser](../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-go-coordinate-parser/index.md)

A coordinate parser for the Go game.


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.GoNodeHandler](../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-go-node-handler/index.md)

An [SgfNodeHandler](../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-sgf-node-handler/index.md) for Go.


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.view.GoSgfView](../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-go-sgf-view/index.md)

Draws the Board together with undo/redo buttons and additional informational text. It's a
`TextView`, so the text it displays is styled according to the `TextView` attributes.


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.Markup](../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-markup/index.md)

Markup of nodes on the board.


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.MarkupType](../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-markup-type/index.md)

The possible values for board Markup.


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.MoveInfo](../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-move-info/index.md)

Information about the last move played.


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.NodeInfo](../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-node-info/index.md)

A piece of information relating to the current node to be displayed in the text window.


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.Piece](../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-piece/index.md)

A gaming piece.


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfCollection](../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-collection.md)

Representation of the `sgf Collection`, which is a list of `GameTree`s.


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.SgfController](../onion.w4v3xrmknycexlsd.lib.sgfcharm/-sgf-controller/index.md)

Controls interaction between the [GoSgfView](../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-go-sgf-view/index.md) and [SgfTree](../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-tree/index.md) objects.


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.SgfCustomPropertyHandler](../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-sgf-custom-property-handler.md)

See [SgfNodeHandler.customPropertyHandler](../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-sgf-node-handler/custom-property-handler.md).


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.SgfData](../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-sgf-data.md)

Base class for all instructions understandable by [GoSgfView](../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-go-sgf-view/index.md).


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.view.SgfDrawer](../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-sgf-drawer/index.md)

If there are some specific components for which you would like to change the way they are drawn,
most prominently the board markup, the pieces and the info text, you can override the corresponding
functions in this interface.


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.SgfInfoKeys](../onion.w4v3xrmknycexlsd.lib.sgfcharm/-sgf-info-keys/index.md)

This object holds string literals to be used as [NodeInfo.key](../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-node-info/key.md)s.


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.view.SgfInputListener](../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-sgf-input-listener/index.md)

Interface for communicating touch events from the view to the controller.


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.SgfMoveHandler](../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-sgf-move-handler.md)

See [SgfNodeHandler.moveHandler](../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-sgf-node-handler/move-handler.md).


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfNavigator](../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-navigator/index.md)

This class offers the facilities to navigate through an `sgf GameTree` given by its constructor parameter.
It calls the [SgfParser](../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-parser/index.md) to get the first [SgfTree](../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-tree/index.md) arising from the string, and provides
functions to obtain the next and preceding nodes as well as to list and descend into possible
variations.


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfNode](../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-node.md)

Representation of the `sgf Node`, which is a list of `Property`s.


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.SgfNodeHandler](../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-sgf-node-handler/index.md)

This class processes [SgfNode](../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-node.md)s given the current [SgfState](../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-sgf-state/index.md) and modifies that state to include
the processed data.


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfParser](../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-parser/index.md)

Parser for reading `sgf` strings into [SgfCollection](../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-collection.md)s.


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfProperty](../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-property/index.md)

Base class for all `sgf Property`s, with a subclass for each particular property.


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfSequence](../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-sequence.md)

Representation of the `sgf Sequence`, which is a list of `Node`s.


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.SgfState](../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-sgf-state/index.md)

This class holds state relevant to the [GoSgfView](../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-go-sgf-view/index.md).


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfTree](../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-tree/index.md)

Representation of the `sgf GameTree`, which consists of an `Sequence`, and zero or more children `GameTree`s.


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType](../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-type/index.md)

This is the base class for all `sgf` data types, for strong typing.


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.SgfVariationsMarker](../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-sgf-variations-marker.md)

See [SgfNodeHandler.variationsMarker](../onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-sgf-node-handler/variations-marker.md).


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.view.SgfView](../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/-sgf-view/index.md)

If you want to use the `sgf` parsing and handling architecture provided by this library but make
your own view with fundamental changes, you can make it implement this interface so that it can be
controlled by the [SgfController](../onion.w4v3xrmknycexlsd.lib.sgfcharm/-sgf-controller/index.md).


|

##### [onion.w4v3xrmknycexlsd.lib.sgfcharm.Status](../onion.w4v3xrmknycexlsd.lib.sgfcharm/-status/index.md)

Indicates the status of the implementation of the respective target.


| (extensions in package onion.w4v3xrmknycexlsd.lib.sgfcharm.parse)

##### [kotlin.String](../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/kotlin.-string/index.md)


| (extensions in package onion.w4v3xrmknycexlsd.lib.sgfcharm.view)

##### [kotlin.String](../onion.w4v3xrmknycexlsd.lib.sgfcharm.view/kotlin.-string/index.md)


