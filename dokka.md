# Module sgfview

This is the SgfView library for rendering `sgf` files for the Go board game. It consists of:

* the [SgfView] class, which is told how to draw the current configuration of the board
* the [SgfController] class, which tells the `SgfView` what to draw and requests data from the `SgfHandler`
* the [SgfHandler] class, which finds the requested part of the `sgf` tree and hands the result over to the `SgfController`
* the [data] module, containing the data model for `sgf` as well as the [SgfParser] class, which does the actual parsing into the tree

# Package onion.w4v3xrmknycexlsd.lib.sgfview.data

# Module app

Just a quick example app demonstrating the usage of SgfView.
