# Module sgfview

This is the SgfView library for rendering `sgf` files for the Go board game. It consists of:

* the [SgfView] class, which is told how to draw the current configuration of the board, given in terms of [data.SgfData] objects
* the [SgfController] class, which links the data it receives from the [SgfHandler] to the [SgfView] and requests new data from the [SgfHandler] in response to touch events
* the [SgfHandler] class, which finds the requested part of the [SgfTree], converts it to [SgfData] and hands the result over to the [SgfController]
* the [data] module, containing the data model for `sgf` as well as the [SgfParser] class, which does the actual parsing into the [SgfTree]

# Package onion.w4v3xrmknycexlsd.lib.sgfview.data

This package contains the `sgf` parser:

* the [SgfParser] object exposes the [SgfParser.parseSgfTree] function, which reads a string into an [SgfTree]
* the [SgfTree] is the internal representation of the SGF `GameTree`, with a strong type system
* the [SgfData] class defines the types of instructions the [SgfView] actually understands

# Module app

Just a quick example app demonstrating the usage of SgfView.
