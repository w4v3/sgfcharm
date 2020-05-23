/*
 *    Copyright 2020 w4v3
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package onion.w4v3xrmknycexlsd.lib.sgfcharm

import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType
import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfProperty
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.NodeInfo
import onion.w4v3xrmknycexlsd.lib.sgfcharm.handle.SgfNodeHandler
import onion.w4v3xrmknycexlsd.lib.sgfcharm.view.DefaultInfoTextMaker

/**
 * This object holds string literals to be used as [NodeInfo.key]s.
 *
 * They are used by the [SgfNodeHandler] and understood by the [DefaultInfoTextMaker]. You might of course
 * change their values to display node information differently in the view.
 *
 * The naming convention used here is as follows: Every property turned into a NodeInfo has a corresponding
 * key here with the name of that property as identifier. As an exception, the properties taking
 * a [SgfType.Color] or [SgfType.Double] value as argument correspond to maps with an
 * [SgfType.Color.Value.BLACK] and an [SgfType.Color.Value.WHITE] entry or [SgfType.Double.Value.MUCH]
 * and [SgfType.Double.Value.VERY_MUCH] entry, respectively. This allows testing for a `PL` (player) property
 * using `if (key in PL.values)`, for example.
 *
 * Another exception is the [SgfProperty.AP] property, which is included with two different strings
 * for the application name ([SgfInfoKeys.APN]) and version ([SgfInfoKeys.APV]).
 */
@Status.Beta
object SgfInfoKeys {
    private val fPL: (SgfType.Color.Value) -> String = { "${it.string()}to play" }
    private val fDM: (SgfType.Double.Value) -> String = { "${it.string()}even position" }
    private val fGB: (SgfType.Double.Value) -> String = { "${it.string()}good for black" }
    private val fGW: (SgfType.Double.Value) -> String = { "${it.string()}good for white" }
    private val fHO: (SgfType.Double.Value) -> String = { "${it.string()}interesting position" }
    private val fUC: (SgfType.Double.Value) -> String = { "${it.string()}unclear position" }
    private val fBM: (SgfType.Double.Value) -> String = { "${it.string()}bad move" }
    private val fTE: (SgfType.Double.Value) -> String = { "${it.string()}good move" }
    private val getMap: ((SgfType.Double.Value) -> String) -> Map<SgfType.Double.Value, String> = {
        mapOf(
            SgfType.Double.Value.MUCH to it(
                SgfType.Double.Value.MUCH
            ),
            SgfType.Double.Value.VERY_MUCH to it(
                SgfType.Double.Value.VERY_MUCH
            )
        )
    }

    private fun SgfType.Double.Value.string() =
        if (this == SgfType.Double.Value.VERY_MUCH) "very " else ""

    private fun SgfType.Color.Value.string() =
        if (this == SgfType.Color.Value.BLACK) "black " else "white "

    /** Strings used by [DefaultInfoTextMaker] to display the [SgfProperty.PL] property */
    @Status.Beta
    public var PL: Map<SgfType.Color.Value, String> = mapOf(
        SgfType.Color.Value.BLACK to fPL(
            SgfType.Color.Value.BLACK
        ),
        SgfType.Color.Value.WHITE to fPL(
            SgfType.Color.Value.WHITE
        )
    )

    /** Strings used by [DefaultInfoTextMaker] to display the [SgfProperty.DM] property */
    @Status.Beta
    public var DM: Map<SgfType.Double.Value, String> = getMap(fDM)

    /** Strings used by [DefaultInfoTextMaker] to display the [SgfProperty.GB] property */
    @Status.Beta
    public var GB: Map<SgfType.Double.Value, String> = getMap(fGB)

    /** Strings used by [DefaultInfoTextMaker] to display the [SgfProperty.GW] property */
    @Status.Beta
    public var GW: Map<SgfType.Double.Value, String> = getMap(fGW)

    /** Strings used by [DefaultInfoTextMaker] to display the [SgfProperty.HO] property */
    @Status.Beta
    public var HO: Map<SgfType.Double.Value, String> = getMap(fHO)

    /** Strings used by [DefaultInfoTextMaker] to display the [SgfProperty.UC] property */
    @Status.Beta
    public var UC: Map<SgfType.Double.Value, String> = getMap(fUC)

    /** Strings used by [DefaultInfoTextMaker] to display the [SgfProperty.BM] property */
    @Status.Beta
    public var BM: Map<SgfType.Double.Value, String> = getMap(fBM)

    /** Strings used by [DefaultInfoTextMaker] to display the [SgfProperty.TE] property */
    @Status.Beta
    public var TE: Map<SgfType.Double.Value, String> = getMap(fTE)

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.C] property */
    @Status.Beta
    public var C: String = ""

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.GC] property */
    @Status.Beta
    public var GC: String = "Game info"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.N] property */
    @Status.Beta
    public var N: String = "Node"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.V] property */
    @Status.Beta
    public var V: String = "Score"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.DO] property */
    @Status.Beta
    public var DO: String = "doubtful move"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.IT] property */
    @Status.Beta
    public var IT: String = "interesting move"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.AN] property */
    @Status.Beta
    public var AN: String = "Annotations by"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.BR] property */
    @Status.Beta
    public var BR: String = "Black player rank"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.BT] property */
    @Status.Beta
    public var BT: String = "Black team"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.CP] property */
    @Status.Beta
    public var CP: String = "Copyright"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.DT] property */
    @Status.Beta
    public var DT: String = "Date of game"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.EV] property */
    @Status.Beta
    public var EV: String = "Event"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.GN] property */
    @Status.Beta
    public var GN: String = "Game"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.ON] property */
    @Status.Beta
    public var ON: String = "Opening"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.OT] property */
    @Status.Beta
    public var OT: String = "Byo-yomi"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.PB] property */
    @Status.Beta
    public var PB: String = "Black player"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.PC] property */
    @Status.Beta
    public var PC: String = "Place"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.PW] property */
    @Status.Beta
    public var PW: String = "White player"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.RE] property */
    @Status.Beta
    public var RE: String = "Result"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.RO] property */
    @Status.Beta
    public var RO: String = "Round"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.RU] property */
    @Status.Beta
    public var RU: String = "Rule set"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.SO] property */
    @Status.Beta
    public var SO: String = "Source"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.TM] property */
    @Status.Beta
    public var TM: String = "Time limits"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.US] property */
    @Status.Beta
    public var US: String = "Game entered by"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.WR] property */
    @Status.Beta
    public var WR: String = "White player rank"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.WT] property */
    @Status.Beta
    public var WT: String = "White team"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.BL] property */
    @Status.Beta
    public var BL: String = "Time left for black/s"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.OB] property */
    @Status.Beta
    public var OB: String = "Moves left for black"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.OW] property */
    @Status.Beta
    public var OW: String = "Moves left for white"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.WL] property */
    @Status.Beta
    public var WL: String = "Time left for white/s"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.HA] property */
    @Status.Beta
    public var HA: String = "Handicap"

    /** String used by [DefaultInfoTextMaker] to display the [SgfProperty.KM] property */
    @Status.Beta
    public var KM: String = "Komi"

    /** String used by [DefaultInfoTextMaker] to display the application name part of the [SgfProperty.AP] property */
    @Status.Beta
    public var APN: String = "Application name"

    /** String used by [DefaultInfoTextMaker] to display the application version part of the [SgfProperty.AP] property */
    @Status.Beta
    public var APV: String = "Application version"
}

/** Some possible values of the `GM` property in FF[4] for identifying the current game type. */
object GameId {

    /** The Go game. */
    const val GO: Int = 1

    /** The Othello game. */
    const val OTHELLO: Int = 2

    /** The Chess game. */
    const val CHESS: Int = 3

    /** The Gomoku game. */
    const val GOMOKU: Int = 4

    /** The Nine Men's Morris game. */
    const val NINE_MENS_MORRIS: Int = 5

    /** The Backgammon game. */
    const val BACKGAMMON: Int = 6

    /** The Chinese Chess game. */
    const val CHINESE_CHESS: Int = 7

    /** The Shogi game. */
    const val SHOGI: Int = 8

    /** The Lines of Action game. */
    const val LINES_OF_ACTION: Int = 9

    /** The Ataxx game. */
    const val ATAXX: Int = 10

    /** The Hex game. */
    const val HEX: Int = 11

    /** The Jungle game. */
    const val JUNGLE: Int = 12

    /** The Neutron game. */
    const val NEUTRON: Int = 13

    /** The Philosopher's Football game. */
    const val PHILOSOPHERS_FOOTBALL: Int = 14

    /** The Quadrature game. */
    const val QUADRATURE: Int = 15

    /** The Trax game. */
    const val TRAX: Int = 16
}