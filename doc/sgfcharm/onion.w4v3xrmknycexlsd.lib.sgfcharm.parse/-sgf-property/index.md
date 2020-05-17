[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse](../index.md) / [SgfProperty](./index.md)

# SgfProperty

`sealed class SgfProperty<out T : `[`SgfType`](../-sgf-type/index.md)`>` [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/parse/SgfTree.kt#L62)

Base class for all `sgf Property`s, with a subclass for each particular property.

The documentation for each class includes just the first sentence of the `sgf` documentation.
For more information, refer to the official [SGF documentation](https://www.red-bean.com/sgf/go.html).

### Types

| Name | Summary |
|---|---|
| [AB](-a-b/index.md) | Add black stones to the board.`data class AB : `[`SgfProperty`](./index.md)`<List<Stone>>` |
| [AE](-a-e/index.md) | Clear the given points on the board.`data class AE : `[`SgfProperty`](./index.md)`<List<Point>>` |
| [AN](-a-n/index.md) | Provides the name of the person who made the annotations.`data class AN : `[`SgfProperty`](./index.md)`<SimpleText>` |
| [AP](-a-p/index.md) | Provides the name and version number of the application used to create this gametree.`data class AP : `[`SgfProperty`](./index.md)`<Compose<SimpleText, SimpleText>>` |
| [AR](-a-r/index.md) | Draw an arrow pointing FROM the first point TO the second point.`data class AR : `[`SgfProperty`](./index.md)`<List<Compose<Point, Point>>>` |
| [AW](-a-w/index.md) | Add white stones to the board.`data class AW : `[`SgfProperty`](./index.md)`<List<Stone>>` |
| [B](-b/index.md) | Execute a black move.`data class B : `[`SgfProperty`](./index.md)`<Move>` |
| [BL](-b-l/index.md) | Time left for black, after the move was made.`data class BL : `[`SgfProperty`](./index.md)`<Real>` |
| [BM](-b-m/index.md) | The played move is bad`data class BM : `[`SgfProperty`](./index.md)`<Double>` |
| [BR](-b-r/index.md) | Provides the rank of the black player.`data class BR : `[`SgfProperty`](./index.md)`<SimpleText>` |
| [BT](-b-t/index.md) | Provides the name of the black team.`data class BT : `[`SgfProperty`](./index.md)`<SimpleText>` |
| [C](-c/index.md) | Provides a comment text for the given node.`data class C : `[`SgfProperty`](./index.md)`<Text>` |
| [CA](-c-a/index.md) | Provides the used charset for SimpleText and Text type.`data class CA : `[`SgfProperty`](./index.md)`<SimpleText>` |
| [CP](-c-p/index.md) | Any copyright information (e.g. for the annotations) should be included here.`data class CP : `[`SgfProperty`](./index.md)`<SimpleText>` |
| [CR](-c-r/index.md) | Marks the given points with a circle.`data class CR : `[`SgfProperty`](./index.md)`<List<Point>>` |
| [CUSTOM](-c-u-s-t-o-m/index.md) | This class is used for custom properties. It is composed of the identifier and the content of the value, both as strings inside [SimpleText](../-sgf-type/-simple-text/index.md) and [Text](../-sgf-type/-text/index.md) objects, respectively.`data class CUSTOM : `[`SgfProperty`](./index.md)`<Compose<SimpleText, Text>>` |
| [DD](-d-d/index.md) | Dim (grey out) the given points.`data class DD : `[`SgfProperty`](./index.md)`<List<Point>>` |
| [DM](-d-m/index.md) | The position is even.`data class DM : `[`SgfProperty`](./index.md)`<Double>` |
| [DO](-d-o.md) | The played move is doubtful`object DO : `[`SgfProperty`](./index.md)`<None>` |
| [DT](-d-t/index.md) | Provides the date when the game was played.`data class DT : `[`SgfProperty`](./index.md)`<SimpleText>` |
| [EV](-e-v/index.md) | Provides the name of the event (e.g. tournament).`data class EV : `[`SgfProperty`](./index.md)`<SimpleText>` |
| [FF](-f-f/index.md) | Defines the used file format.`data class FF : `[`SgfProperty`](./index.md)`<Number>` |
| [FG](-f-g/index.md) | The figure property is used to divide a game into different figures for printing.`data class FG : `[`SgfProperty`](./index.md)`<Compose<Number, SimpleText>>` |
| [GB](-g-b/index.md) | Something good for black.`data class GB : `[`SgfProperty`](./index.md)`<Double>` |
| [GC](-g-c/index.md) | Provides some extra information about the following game.`data class GC : `[`SgfProperty`](./index.md)`<Text>` |
| [GM](-g-m/index.md) | Defines the type of game.`data class GM : `[`SgfProperty`](./index.md)`<Number>` |
| [GN](-g-n/index.md) | Provides a name for the game. The name is used to`data class GN : `[`SgfProperty`](./index.md)`<SimpleText>` |
| [GW](-g-w/index.md) | Something good for white.`data class GW : `[`SgfProperty`](./index.md)`<Double>` |
| [HA](-h-a/index.md) | Defines the Number of handicap stones (&gt;=2).`data class HA : `[`SgfProperty`](./index.md)`<Number>` |
| [HO](-h-o/index.md) | Node is a 'hotspot', i.e. something interesting.`data class HO : `[`SgfProperty`](./index.md)`<Double>` |
| [IT](-i-t.md) | The played move is interesting`object IT : `[`SgfProperty`](./index.md)`<None>` |
| [KM](-k-m/index.md) | Defines the komi.`data class KM : `[`SgfProperty`](./index.md)`<Real>` |
| [KO](-k-o.md) | Execute a given move, even if it's illegal.`object KO : `[`SgfProperty`](./index.md)`<None>` |
| [LB](-l-b/index.md) | Writes the given text on the board at the point.`data class LB : `[`SgfProperty`](./index.md)`<List<Compose<Point, SimpleText>>>` |
| [LN](-l-n/index.md) | Applications should draw a simple line form one point to the other.`data class LN : `[`SgfProperty`](./index.md)`<List<Compose<Point, Point>>>` |
| [MA](-m-a/index.md) | Marks the given points with an 'X'.`data class MA : `[`SgfProperty`](./index.md)`<List<Point>>` |
| [MN](-m-n/index.md) | Sets move number to the given value.`data class MN : `[`SgfProperty`](./index.md)`<Number>` |
| [N](-n/index.md) | Provides a name for the node.`data class N : `[`SgfProperty`](./index.md)`<SimpleText>` |
| [OB](-o-b/index.md) | Number of black moves left to play in this byo-yomi period.`data class OB : `[`SgfProperty`](./index.md)`<Number>` |
| [ON](-o-n/index.md) | Provides some information about the opening played.`data class ON : `[`SgfProperty`](./index.md)`<SimpleText>` |
| [OT](-o-t/index.md) | Describes the method used for overtime (byo-yomi).`data class OT : `[`SgfProperty`](./index.md)`<SimpleText>` |
| [OW](-o-w/index.md) | Number of white moves left to play in this byo-yomi period.`data class OW : `[`SgfProperty`](./index.md)`<Number>` |
| [PB](-p-b/index.md) | Provides the name of the black player.`data class PB : `[`SgfProperty`](./index.md)`<SimpleText>` |
| [PC](-p-c/index.md) | Provides the place where the game was played.`data class PC : `[`SgfProperty`](./index.md)`<SimpleText>` |
| [PL](-p-l/index.md) | Tells whose turn it is to play.`data class PL : `[`SgfProperty`](./index.md)`<Color>` |
| [PM](-p-m/index.md) | This property is used for printing.`data class PM : `[`SgfProperty`](./index.md)`<Number>` |
| [PW](-p-w/index.md) | Provides the name of the white player.`data class PW : `[`SgfProperty`](./index.md)`<SimpleText>` |
| [RE](-r-e/index.md) | Provides the result of the game.`data class RE : `[`SgfProperty`](./index.md)`<SimpleText>` |
| [RO](-r-o/index.md) | Provides round-number and type of round.`data class RO : `[`SgfProperty`](./index.md)`<SimpleText>` |
| [RU](-r-u/index.md) | Provides the used rules for this game.`data class RU : `[`SgfProperty`](./index.md)`<SimpleText>` |
| [SL](-s-l/index.md) | Selected points. Type of markup unknown`data class SL : `[`SgfProperty`](./index.md)`<List<Point>>` |
| [SO](-s-o/index.md) | Provides the name of the source (e.g. book, journal, ...).`data class SO : `[`SgfProperty`](./index.md)`<SimpleText>` |
| [SQ](-s-q/index.md) | Marks the given points with a square.`data class SQ : `[`SgfProperty`](./index.md)`<List<Point>>` |
| [ST](-s-t/index.md) | Defines how variations should be shown.`data class ST : `[`SgfProperty`](./index.md)`<Number>` |
| [SZ](-s-z/index.md) | Defines the size of the board.`data class SZ : `[`SgfProperty`](./index.md)`<Compose<Number, Number>>` |
| [TB](-t-b/index.md) | Specifies the black territory or area.`data class TB : `[`SgfProperty`](./index.md)`<List<Point>>` |
| [TE](-t-e/index.md) | The played move is a tesuji (good move).`data class TE : `[`SgfProperty`](./index.md)`<Double>` |
| [TM](-t-m/index.md) | Provides the time limits of the game.`data class TM : `[`SgfProperty`](./index.md)`<Real>` |
| [TR](-t-r/index.md) | Marks the given points with a triangle.`data class TR : `[`SgfProperty`](./index.md)`<List<Point>>` |
| [TW](-t-w/index.md) | Specifies the white territory or area.`data class TW : `[`SgfProperty`](./index.md)`<List<Point>>` |
| [UC](-u-c/index.md) | The position is unclear.`data class UC : `[`SgfProperty`](./index.md)`<Double>` |
| [US](-u-s/index.md) | Provides the name of the user (or program) who entered the game.`data class US : `[`SgfProperty`](./index.md)`<SimpleText>` |
| [V](-v/index.md) | Define a value for the node.`data class V : `[`SgfProperty`](./index.md)`<Real>` |
| [VW](-v-w/index.md) | View only given points of the board.`data class VW : `[`SgfProperty`](./index.md)`<List<Point>>` |
| [W](-w/index.md) | Execute a white move.`data class W : `[`SgfProperty`](./index.md)`<Move>` |
| [WL](-w-l/index.md) | Time left for white after the move was made.`data class WL : `[`SgfProperty`](./index.md)`<Real>` |
| [WR](-w-r/index.md) | Provides the rank of the white player.`data class WR : `[`SgfProperty`](./index.md)`<SimpleText>` |
| [WT](-w-t/index.md) | Provide the name of the white team.`data class WT : `[`SgfProperty`](./index.md)`<SimpleText>` |

### Properties

| Name | Summary |
|---|---|
| [value](value.md) | the content of the property value, typed`open val value: T` |
