# SgfCharm

This is an [`SGF`](https://www.red-bean.com/sgf/index.html) parser and viewer library for Android, written in Kotlin.

It provides a `SgfView` view that can display games encoded in an `SGF` file (by default Go) and that allows
user interaction with the game. It can also be used as just a parsing library that converts each
property from the `SGF` to a proper type, allowing type-safe and idiomatic handling in Kotlin.

Only the `FF[4]` standard of the `SGF` specification (i.e., the current one) is implemented so far,
and the default configuration of the `SgfView` widget can only handle `SGF` files for the Go game.
However, the library was developed to be as widely applicable to the `SGF` specification as possible,
so that it should be easy to use it to define views for other games, as well. See [Customization](#customization)
for details.

[Installation](#installation)  
[Basic Usage](#basic-usage)  
[Customization](#customization)  
[Release notes](#release-notes)  
[License](#license)

[Documentation](https://w4v3.github.io/sgfcharm/sgfcharm/index.html)

## Installation

**project `build.gradle`**

	allprojects {
	    repositories {
	        ...
	        maven { url 'https://jitpack.io' }
	    }
	}
	
**module `build.gradle`**

	dependencies {
        implementation 'com.github.w4v3:sgfcharm:v0.2.0'
        // if you want to use it for chess, too, add:
        // implementation 'com.github.w4v3:sgfcharm-chess:v0.2.0'
	}


## Basic Usage

First, add the view to your layout:
```xml
<sgfcharm.view.SgfView
    android:id="@+id/sgfview"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```
Then, in your code, call
```kotlin
SgfController().load(mySgfString).into(sgfview)
```
where `mySgfString` is a string containing `SGF` data. The result looks like this:

<img src="gosgfviewex1.png" width="300"/> <img src="gosgfviewex2.png" width="300"/> 

You get a view of the board and possibly markup from the file, with undo/redo buttons and additional
text displayed underneath. When there is nothing to undo, the redo button shows the next node or
first variation, if any. You can also interact freely with the board by placing stones on it. It
checks for captures, but it does not check for illegal moves. For example, placing a stone onto an
existing one and suicide are both allowed. This is in accordance with the `SGF` specification of a
`Move`, but I might change this behavior in the future (or at least provide an option to turn it off).

If you would like to make use of the (experimental) chess `SGF` functionalities of the library, in 
addition to including the above library dependencies, you need to call `sgfview.setupChess()` on the view.

The view does not retain its state upon configuration changes. Instead, you can put and get the state
of the controller into the respective bundles using `Bundle.putSgfController` and `Bundle.getSgfController`,
and load it into the view after retrieving it, like so:
```kotlin
class MainActivity : AppCompatActivity() {

    lateinit var controller: SgfController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        controller =
            savedInstanceState?.getSgfController("sgf") ?: SgfController().load(mySgfString)
        controller.into(sgfview)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSgfController("sgf", controller)
    }
}
```
Note that this does not save stones placed by the user if they were not part of the `SGF` file.

Have a look at the [app module](app) for a slightly more sophisticated example, or at the next section
for an in-depth explanation of the available features.

## Customization

Depending on what you have in mind, a different amount of work is required and different levels of
stability are guaranteed. If you …

* want to use it to display Go `SGF` files and only want to make changes to the default theme,
change the [`SgfView` attributes](#styling-the-sgfview);
* want to use it for `Go` but make more fundamental changes to the appearance, you could change
the `SgfView` components or even create your own `View` and have it implement the `SgfViewAdapter` interface,
see [here](#custom-drawing) for details;
* want to use it for a different game with different rules and possibly different parsing of point/move/
stone types, or need to handle custom `SGF` properties outside of the standard, find out how to
[implement a different game](#implementing-a-different-game);
* just want to use the parsing and/or navigation facilities, read their [documentation](https://w4v3.github.io/sgfcharm/sgfcharm/index.html).

In the following sections are a few "tutorials" showing examples of how to do these things. For more
details, please always refer to the [documentation](https://w4v3.github.io/sgfcharm/sgfcharm/index.html).

### Styling the `SgfView`

First of all, let me note that the `SgfView` is actually a `TextView`, so all `TextView`
attributes can be used on it and have an effect on the info text displayed below the board.

In addition, the `SgfView` has the following `XML` attributes:

   attribute     |                 description                  |   default  |   type
-----------------|----------------------------------------------|------------|-----------
`app:blackColor` | the color used to draw the black stones      | black      | `color`
`app:whiteColor` | the color used to draw the white stones      | white      | `color`
`app:gridColor`  | the color used to draw the grid on the board | light gray | `color`
`app:markupColor`| the color used to draw board markup          | blue       | `color`
`app:showText`   | whether or not to show the information text  | true       | `boolean`
`app:showButtons`| whether or not to show the undo/redo buttons | true       | `boolean`

For a little more control, the class also exposes two `Paint` members:

* `piecePaint` used for drawing the stones (but the color will be set according to `blackColor` and `whiteColor`)
* `gridPaint` used for drawing the grid

Finally, `SgfController` offers the possibility to customize interaction with the view via two of its
properties (which can also be used as constructor parameters):

* `showVariations` can be used to switch the display of variations on the board on or off; note that
the `SGF` file might also switch this on or off, but the `showVariations` property overrides this
(except if it is set to `null`, which is the default).
* `interactionMode` is by default `InteractionMode.FREE_PLAY`, meaning that the user can place stones
on the board freely (with the `SGF` display advancing if they have hit the next node or a variation);
it can be `InteractionMode.COUNTERMOVE` to make the view "play back" with the next move in the `SGF`
file if the user has played a move from the file; or `InteractionMode.DISABLE` to disallow placing stones

The functionality of the buttons is independent of this setting. Thus, if you just want to display
a figure for a single board position, you might use `SgfController(interactionMode = SgfController.InteractionMode.DISABLE`
together with `app:showButtons="false"` in the `XML` file.

### Custom drawing

The easiest way to change the appearance in a bit more fundamental ways than described above is to
change some customizable `SgfView` components, namely

* the `SgfPieceDrawer` set by `SgfView.usePieceDrawer` used to draw the pieces on the board
* the `SgfMarkupDrawer` set by `SgfView.useMarkupDrawer` used to draw the board markup
* the `SgfInfoTextMaker` set by `SgfView.useInfoTextMaker` used to turn additional information on the
current node into the info text displayed underneath the view

Here is an example:
```kotlin
SgfView.usePieceDrawer(GameId.GO) { canvas, piece, x, y, size, paint, blackColor, whiteColor ->
    val colorValue = piece.color

    paint.apply {
        style = Paint.Style.STROKE
        color = when (colorValue) {
            SgfType.Color.Value.BLACK -> blackColor
            SgfType.Color.Value.WHITE -> whiteColor
        }
    }

    canvas.drawRect(x - size / 2, y - size / 2, x + size / 2, y + size / 2, paint)
}
```
This `SgfPieceDrawer` implementation draws an outlined rectangle, which will be used to draw
the Go pieces. Note the `Piece` object; this is the type of data the `SgfView` receives which encodes
the current situation of the board. The `drawPiece` method has to choose the correct color accordingly.
The supplied drawer will only be used to draw pieces for the given `GameId`, i.e., Go pieces in this
case. If no piece drawer is known for a particular type of game, the default `GoPieceDrawer` will be used.
```kotlin
SgfView.useMarkupDrawer { canvas, markup, x, y, xTo, yTo, size, paint, blackColor, whiteColor, markupColor ->
    if (markup.type == MarkupType.SELECT) {
        val myPaint = Paint().apply {
            style = Paint.Style.FILL
            color = markupColor
        }
        canvas.drawTriangle(x.toInt(), y.toInt(), size.toInt() / 2, myPaint)
    } else DefaultMarkupDrawer(canvas, markup, x, y, xTo, yTo, size, paint, blackColor, whiteColor, markupColor)
}
```
This `SgfMarkupDrawer` implementation only does something special for *selection* (`SL`) markups. Instead
of the default (encircling the selected piece), it draws a filled triangle. Note the convenience extension
function `drawTriangle` provided by the library.
```kotlin
SgfView.useInfoTextMaker { nodeInfos, lastMoveInfo ->
    nodeInfos.filter { it.key == SgfInfoKeys.C }.joinToString("\n")
}
```
Finally, `SgfInfoTextMaker` can be used to specify how the textual information from the `SGF` file should
be displayed. Here, it ignores the `MoveInfo` information on the last move and uses only those
`NodeInfo` objects of the current node that have a key `SgfInfoKeys.C`, meaning *comment*, and displays
them interspersed with newlines.

If you merely want to change the string displayed for each informational property, have a look at
`SgfInfoKeys`, which contains the displayed strings.

The above approach is not applicable if you …

* want to draw a custom grid or a different kind of board altogether;
* want to include additional buttons or other features;
* want to change the order of the components.

The implementation of custom boards or games is discussed in the [next section](#implementing-a-different-game).
Otherwise, you will have to make your own `View` and have it implement the `SgfViewAdapter` interface.
A minimal implementation looks like this:
```kotlin
class MySgfView(context: Context) : View(context), SgfViewAdapter {
    private var inputListener: SgfInputListener? = null
    
    override fun registerInputListener(listener: InputListener) { inputListener = listener }

    private var pieces: List<Piece> = listOf()
    private var gridColumns: Int = 0
    private var gridRows: Int = 0
   
    override fun onReceiveSgfData(data: List<SgfData>) {
        pieces = data.filterIsInstance<Piece>()
        data.find { it is BoardConfig }?.let { col, row ->
            gridColumns = col
            gridRows = row
        }
        invalidate()
    }
}
```
You should call the `SgfInputListener` when observing a touch event which is relevant to the navigation
of the `SGF` file. In `onReceiveSgfData`, you will probably want to separate the data by its type
and save them to variables; in this case, only pieces and board dimensions were read, but more types
are available; see [the documentation](https://w4v3.github.io/sgfcharm/sgfcharm/onion.w4v3xrmknycexlsd.lib.sgfcharm.handle/-sgf-data.md).

### Implementing a different game

From the `SGF` point of view, Go is a simple game: Stones are indistinguishable from each other,
and every move is completely described by the point where a stone is placed. Therefore, `Move`,
`Stone` and `Point` types from `SGF` are all the same for Go and specified by two coordinates.
While this library has abstract data types for `Move`, `Stone` and `Point` that do not make specific
assumptions, the default implementations all assume that they are based on an `XYPoint` type, which
consists simply of two coordinates, given by two letters in the `SGF`.

This means that if you want to implement a `ChessSgfView`, for example, you need to tweak the library
quite a bit:

* a `Stone` now also has a type (Knight, Queen, etc.) and a `Move` consists of two points, so custom `SGF` parsing is required;
* a stones are not removed if they are encircled like in Go, but instead, a stone is captured if a move
was executed landing on it, requiring custom move handling;
* your version of the format might include custom properties that are not part of the `SGF` specification,
in which case you need to specify how to handle these properties;
* stones should be drawn differently and the grid should have a checkerboard pattern, so a new `View`
is required.

Nevertheless, the required changes are relatively straightforward. In fact, I will use the implementation
of the [sgfcharm-chess module](https://w4v3.github.io/sgfcharm/sgfcharm-chess/index.html) throughout the section as an example.

Let's start at the bottom of the whole process. First of all, we need to define our own `SgfType.Stone`
and `SgfType.Move` types. The `SgfType.Point` can be reused from the `XYPoint` implementation from Go,
as a point in chess can still be defined using two coordinates:
```kotlin
data class ChessStone(val type: ChessStoneType, override val point: XYPoint) : SgfType.Stone(point)

enum class ChessStoneType {
    KING,
    QUEEN,
    ROOK,
    BISHOP,
    KNIGHT,
    PAWN
}

abstract class ChessMove(override val point: XYPoint?) : SgfType.Move(point)

data class StandardChessMove(val stone: ChessStone, override val point: XYPoint) : ChessMove(point)
data class CastlingMove(val long: Boolean) : ChessMove(null)
object ChessPass : ChessMove(null)
```
As you can see, we define a `ChessStone` type that extens `SgfType.Stone` and overrides its `point`
property. A `ChessMove` is taken to be an abstract class as there are many different subtypes of moves
in chess. Here, two examples are given: The `StandardChessMove` consists of a stone (with its current position)
and a point where to move the stone to. A `CastlingMove` represents either short or long castling.
Finally, `ChessPass` indicates a pass move. Now we can tell the `SgfParser` how to parse a property
value that represents each of these types. This is done by extending the `CoordinateParser` class:
```kotlin
class ChessCoordinateParser : SgfParser.CoordinateParser<SgfType.XYPoint>() {
    public override fun parsePoint(from: String): XYPoint? = from.getOrNull(0)?.let { column ->
        from.drop(0).toIntOrNull()?.let { row ->
            XYPoint(('a'..'z').indexOf(column) + 1, row)
        }
    }

    public override fun parseStone(from: String): ChessStone? = with(from) {
        firstOrNull()?.toChessStoneType()?.let { stone ->
            parsePoint(drop(1))?.let { point ->
                ChessStone(stone, point)
            }
        }
    }

    private fun Char.toChessStoneType(): ChessStoneType? =
        when (this) {
            'K' -> ChessStoneType.KING
            'Q' -> ChessStoneType.QUEEN
            'R' -> ChessStoneType.ROOK
            'B' -> ChessStoneType.BISHOP
            'N' -> ChessStoneType.KNIGHT
            'P' -> ChessStoneType.PAWN
            else -> null
        }

    public override fun parseMove(from: String): ChessMove = when (from) {
        "0-0" -> CastlingMove(false)
        "0-0-0" -> CastlingMove(true)
        else -> from.parseStandardMove()
    } ?: ChessPass

    @OptIn(Status.Util::class)
    private fun String.parseStandardMove(): StandardChessMove? =
        parseCompose(::parseStone, ::parsePoint)?.let { (stone, point) ->
            StandardChessMove(stone, point)
        }

    public override fun XYPoint.rangeTo(other: XYPoint): List<XYPoint> =
        (x..other.x).flatMap { x -> (other.y downTo y).map { y -> XYPoint(x, y) } }
}
```
To be honest, I did not find any information on how these Chess types are represented in the `SGF`,
but I assumed here that each point is given like in standard algebraic notation, from `a1` to `h8`,
and that each stone has a capital identifier and a point, like `Pe7` for the pawn at `e7`
(with the coordinate system starting at the bottom left). A `StandardChessMove` is assumed to be a compose type of
`ChessStone` and `Stone`, like `Peb:ed`for `e7-e5`, while castling is indicated by `0-0` or `0-0-0`.

Note that `parseMove` opts in to the `Status.Util` annotation because it uses `parseCompose`, which
is a utility function provided for these custom purposes but is not regarded part of the official API
of this library. See the [documentation](https://w4v3.github.io/sgfcharm/sgfcharm/onion.w4v3xrmknycexlsd.lib.sgfcharm/-status/index.html)
of the annotations for more information.

Now we only need to set up the `SgfParser` to use this `CoordinateParser` for chess, by calling
`SgfParser.useCoordinateParser(GameId.CHESS, ChessCoordinateParser)` and the parser will give
us a properly parsed `SgfTree`.

To illustrate how a custom property would be handled, we introduce a two new properties:
* `CHK`, indicating that a move in the same node has caused a check, taking an `SGF Double` type where
`1` means check and `2` means checkmate
* `DEF`, setting up the board with the default starting position for chess

The `SgfParser` will turn any property identifier it does not know into a property of type `CUSTOM`
and it is up to the `SgfNodeHandler` to deal with it. This handler takes a node and processes it,
using and modifying the current (abstract) state of the view via an `SgfState` object.

The handler has two components that you may replace to influence its functionality: the `SgfMoveHandler`
set by `SgfNodeHandler.useMoveHandler` for carrying out moves, and the `SgfCustomPropertyHandler` set
by `SgfNodeHandler.useCustomPropertyHandler` for handling `CUSTOM` properties.

The `SgfMoveHandler` should mainly call `SgfState.addPiece` and `SgfState.removePiece` functions to
add or remove pieces. It can find out about the pieces currently on board by inquiring into
`SgfState.currentPieces`. It also has to return a `MoveInfo` object with information about what has
changed by the executed move (move numbers and captures). Here is an implementation
for chess, which looks if there is a piece at the target in which case it removes it, and carries out the move
or executes the correct castling move sequence if the move was a `CastlingMove`:
```kotlin
@OptIn(Status.Impl::class)
SgfNodeHandler.useMoveHandler {
    state: SgfState, colorValue: SgfType.Color.Value, move: SgfType.Move ->
        with(state) {
            var prisoners = 0
            var prisonerColor = SgfType.Color.Value.BLACK

            when (move) {
                is StandardChessMove -> {
                    removePiece(Piece(colorValue, move.stone))
                    currentPieces.find { it.stone?.point == point }?.let {
                        prisonerColor = it.color
                        removePiece(it)
                        prisoners = 1
                    }
                    addPiece(Piece(colorValue, ChessStone(move.stone.type, move.point)))
                }
                is CastlingMove -> {
                    val king = currentPieces.find {
                        (it.stone as? ChessStone)?.type == ChessStoneType.KING &&
                                it.color == colorValue
                    }

                    val rook = currentPieces.find {
                        (it.stone as? ChessStone)?.type == ChessStoneType.ROOK &&
                                it.color == colorValue &&
                                (it.stone?.point as? XYPoint)?.x?.minus(
                                    (king?.stone as? ChessStone)?.point?.x ?: 0
                                )?.absoluteValue == if (move.long) 3 else 2
                    }

                    king?.let { removePiece(it) }
                    rook?.let { removePiece(it) }

                    val row = when (colorValue) {
                        SgfType.Color.Value.BLACK -> 8
                        SgfType.Color.Value.WHITE -> 1
                    }

                    val kingcol = if (move.long) 3 else 7
                    val rookcol = if (move.long) 4 else 6

                    if (move.long) {
                        addPiece(
                            Piece(
                                colorValue,
                                ChessStone(ChessStoneType.KING, XYPoint(kingcol, row))
                            )
                        )
                        addPiece(
                            Piece(
                                colorValue,
                                ChessStone(ChessStoneType.ROOK, XYPoint(rookcol, row))
                            )
                        )
                    }
                }
                is ChessPass -> MoveInfo(1, colorValue, move, (0 to 0))
                else -> return@lambda null
            }

            MoveInfo(
                1,
                colorValue,
                move,
                if (prisonerColor == SgfType.Color.Value.BLACK) (prisoners to 0) else (0 to prisoners)
            )
        }
}
```
As you can see, opting it to use the `Status.Impl` annotated features is required. Next, the handler
for our custom castling property:
```kotlin
@OptIn(Status.Impl::class, Status.Util::class)
SgfHandler.useCustomPropertyHandler {
    { state: SgfState, propIdent: String, propValue: String ->
        with(state) {
            when (propIdent) {
                "CHK" -> propValue.parseDouble()?.value?.let { addNodeInfo(NodeInfo(SgfInfoKeys.CHK[it])) }
                "DEF" -> {
                    colorJustSet = SgfType.Color.Value.WHITE
                    addPiece(
                        Piece(
                            SgfType.Color.Value.WHITE,
                            ChessStone(ChessStoneType.PAWN, XYPoint(1, 7))
                        )
                    )
                    // add all other default pieces …
                }
            }
        }
    }
}

public val SgfInfoKeys.CHK: Map<SgfType.Double.Value, String>
    get() = mapOf(SgfType.Double.Value.MUCH to "Check", SgfType.Double.Value.MUCH to "Checkmate")
```
As you can see, it first checks if the passed property identifier is `CHK` or `DEF`. For `CHK`, the
value is parsed as a double type, then `addNodeInfo` is called to turn it into a `NodeInfo` object
transmitted to the view. A key map is used for convenience to convert the double value to a message.
For `DEF`, the next color to move is set to white and the pieces are added using `addPiece`.

Now, in order to draw the pieces, an `SgfPieceDrawer` was implemented. For drawing the grid and handling
the different properties of the board (such as the response to touch events in order to enter a `ChessMove`),
we can implement the `SgfBoard` interface and use `SgfView.useBoard` to register the implementation for
chess with the view. See the [sgfcharm-chess source](sgfcharm-chess/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm_chess/ChessView.kt) 
for an example on how to implement this interface.

### Limitations

What you cannot do with this library:
* automatically handle `SGF` files from versions earlier than `FF[4]` (you can handle them as custom
properties, though)
* automatically handle text encodings other than `utf-8` (but you can of course simply reencode your string beforehand)
* things that don't have anything to do with `SGF` (I think)

## Release notes

### 2020-05-18 version 0.2.0

Added new functionality:

* changing the way of interaction with the view using `SgfController.interactionMode`, including the
possibility of instant countermoves from the view upon user input
* deactivate the display of the undo/redo buttons
* board is now expanded to fill the screen if parts of it are specified to be invisible via the `VW` property
from the `SGF` specification

Fixed inherited property bug.

### 2020-05-31 version 0.2.0

Made API more easily extendable:

* added the `sgfcharm-chess` module for (basic) chess `SGF` file support
* refactored `SgfDrawer` to lambda components
* segregated `SgfBoard` interface for custom boards

### 2020-05-17 version 0.1.0

Improved performance and UI—definitely usable by now. Changes:

* implemented fully functional siblings variation mode
* current board state is cached alongside the history, allowing both fast forward and backward navigation
* save and restore possibility implemented for `SgfController`
* board configuration changes within one `SGF` file are possible (although illegal in `SGF`)
* scroll motions not intercepted any more
* `GoSgfView` now tries to fill the requested width instead of the smaller dimension

### 2020-05-14 version 0.0.1

Initial release! Not yet thoroughly tested, but working so far. Features:

* a `GoSgfView` to display `SGF` files for the Go game
* a highly customizable parsing library for `SGF` files
* an `SgfView` interface, allowing custom drawing while taking advantage of the existing parsing and navigation facilities

## License

    Copyright 2020 w4v3

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
