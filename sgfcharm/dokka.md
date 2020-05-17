# Module sgfview

This is the SgfView library for rendering `sgf` files.

It consists of:

* the [onion.w4v3xrmknycexlsd.lib.sgfcharm.view] package containing the UI logic
* the [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse] package containing the parser for `SGF` files and the data type definitions
* the [SgfController] class managing the interaction of the two components
* the [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle] package containing several helper classes supporting the [SgfController]

Please take note of the [Status] annotations provided throughout the documentation which should guide
your reliance on the different components of the API.

# Package onion.w4v3xrmknycexlsd.lib.sgfcharm.view

This package contains the logic for rendering [SgfData] received from the [SgfController]:

* the [GoSgfView] can render the data into an interactive view, and forwards touch events to the [SgfController]
* the [SgfView] interface can be implemented by any view to be controllable by the [SgfController]
* the [SgfDrawer] interface can be implemented if only relatively minor changes to the [GoSgfView] drawing are required

# Package onion.w4v3xrmknycexlsd.lib.sgfcharm.parse

This package contains the `sgf` parser and the library data models:

* the [SgfTree] is the internal representation of the SGF `GameTree`, with a strong type system
* the [SgfParser] exposes the [SgfParser.parseSgfCollection] function, which reads a string into a list of [SgfTree]s
* the [SgfNavigator] exposes functions to navigate through the tree node by node

# Package onion.w4v3xrmknycexlsd.lib.sgfcharm.handle

This package may be seen as an extension to the [SgfController], which makes use of these components:

* the [SgfData] class defines the types of instructions the [SgfView] actually understands
* the [SgfState] class holds [SgfData] representing the current state of the view
* the [SgfNodeHandler] defines extension functions to [SgfState], which are used to process
each node of the [SgfTree] by turning it into [SgfData] and modifying the [SgfState] with it
