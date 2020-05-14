# SgfCharm

This is an [`SGF`](https://www.red-bean.com/sgf/index.html) parser and viewer library for Android, written in Kotlin.

It provides a `GoSgfView` view that can display Go games encoded in an `SGF` file and that allows
user interaction with the game. It can also be used as just a parsing library that converts each
property from the `SGF` to a proper type, allowing type-safe and idiomatic handling in Kotlin.

Only the `FF[4]` standard of the `SGF` specification (i.e., the current one) is implemented so far,
and the `GoSgfView` widget can only handle `SGF` files for the Go game. However, the library was
developed to be as widely applicable to the `SGF` format as possible, so that it should be easy
to use it to define views for other games, as well. See [Customization](#customization) for details.

## Installation

## Basic Usage

First, add the view to your layout:
```xml
<sgfcharm.view.GoSgfView
    android:id="@+id/sgfview"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```
Then, in your code, call
```kotlin
SgfController().load(mySgfString).into(sgfview)
```
where `mySgfString` is a string containing `SGF` data. The result looks like this:

You get a view of the board and possibly markup from the file, with undo/redo buttons and additional
text displayed underneath. When there is nothing to undo, the redo button shows the next node or
first variation, if any. You can also interact freely with the board by placing stones on it. It
checks for captures, but it does not check for illegal moves. For example, placing a stone onto an
existing one and suicide are both allowed. This is in accordance with the `SGF` specification of a
`Move`, but I might change this behavior in the future (or at least provide an option to turn it off).

Have a look at the [app module](app) for a slightly more sophisticated example, or at the next section
for an in-depth explanation of the available features.

## Customization

Depending on what you have in mind, a different amount of work is required and different levels of
stability are guaranteed. If you …

* want to use it to display Go `SGF` files and only want to make changes to the default theme,
change the [`GoSgfView` attributes](#gosgfview-attributes);
* want to use it for `Go` but make more fundamental changes to the appearance, you could implement
the [`SgfDrawer` interface](#sgfdrawer-interface) or even create your own `View` and have it implement
the [`SgfView` interface](#sgfview-interface);
* want to use it for a different game with different rules or handle custom properties outside of the
`SGF` standard, look at [Custom `SGF` node handling](#custom-sgf-node-handling);
* need custom parsing of point/move/stone types, look at [Custom `SGF` parsing](#custom-sgf-parsing);
* just want to use the parsing and/or navigation facilities, read the [documentation](doc).

From the `SGF` point of view, Go is a simple game: Stones are indistinguishable from each other,
and every move is completely described by the point where a stone is placed. Therefore, `Move`,
`Stone` and `Point` types from `SGF` are all the same for Go and specified by two coordinates.
This means that if you want to implement a `ChessSgfView`, you need to do most of the above:

* a `Stone` now also has a type and a `Move` consists of two points, so custom `SGF` parsing is required;
* a stones are not removed if they are encircled like in Go, but instead, a stone is captured if a move
was executed landing on it, requiring custom node handling;
* stones should be drawn differently and the grid should have a checkerboard pattern, so a new `View`
is required.

Nevertheless, it is not a lot of additional work that is required. In fact, I will use the implementation
of a `ChessSgfView` throughout the section as an example.

### `GoSgfView` attributes

First of all, let me note that the `GoSgfView` is actually a `TextView`, so all `TextView`
attributes can be used on it and have an effect on the info text displayed below the board.

In addition, the `GoSgfView` has the following `XML` attributes:

    attribute     |                 description                  |   default  |   type
------------------|----------------------------------------------|------------|-----------
`app:blackColor`  | the color used to draw the black stones      | black      | `color`
`app:whiteColor`  | the color used to draw the white stones      | white      | `color`
`app:gridColor`   | the color used to draw the grid on the board | light gray | `color`
`app:markupColor` | the color used to draw board markup          | blue       | `color`
`app:showText`    | whether or not to show the information text  | true       | `boolean`

For a little more control, the class also exposes two `Paint` members:

* `piecePaint` used for drawing the stones (but the color will be set according to `blackColor` and `whiteColor`)
* `gridPaint` used for drawing the grid

Finally, `SgfController` offers the possibility to switch the display of variations on the board on
or off, via its `showVariations` property which can also be used in the constructor. Note that the `SGF`
file might also switch this on or off, but the `showVariations` property overrides this (except if
it is set to `null`, which is the default).

### `SgfDrawer` interface

### `SgfView` interface

### Custom `SGF` node handling

### Custom `SGF` parsing

## Release notes

## License