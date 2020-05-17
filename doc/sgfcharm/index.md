[sgfcharm](./index.md)

This is the SgfView library for rendering `sgf` files.

It consists of:

* the [onion.w4v3xrmknycexlsd.lib.sgfcharm.view](onion.w4v3xrmknycexlsd.lib.sgfcharm.view/index.md) package containing the UI logic
* the [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse](onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/index.md) package containing the parser for `SGF` files and the data type definitions
* the [SgfController](onion.w4v3xrmknycexlsd.lib.sgfcharm/-sgf-controller/index.md) class managing the interaction of the two components
* the [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle](onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/index.md) package containing several helper classes supporting the [SgfController](onion.w4v3xrmknycexlsd.lib.sgfcharm/-sgf-controller/index.md)

Please take note of the [Status](onion.w4v3xrmknycexlsd.lib.sgfcharm/-status/index.md) annotations provided throughout the documentation which should guide
your reliance on the different components of the API.

### Packages

| Name | Summary |
|---|---|
| [onion.w4v3xrmknycexlsd.lib.sgfcharm](onion.w4v3xrmknycexlsd.lib.sgfcharm/index.md) |  |
| [onion.w4v3xrmknycexlsd.lib.sgfcharm.handle](onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/index.md) | This package may be seen as an extension to the [SgfController](onion.w4v3xrmknycexlsd.lib.sgfcharm/-sgf-controller/index.md), which makes use of these components: |
| [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse](onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/index.md) | This package contains the `sgf` parser and the library data models: |
| [onion.w4v3xrmknycexlsd.lib.sgfcharm.view](onion.w4v3xrmknycexlsd.lib.sgfcharm.view/index.md) | This package contains the logic for rendering [SgfData](onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-sgf-data.md) received from the [SgfController](onion.w4v3xrmknycexlsd.lib.sgfcharm/-sgf-controller/index.md): |

### Index

[All Types](alltypes/index.md)