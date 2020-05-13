package onion.w4v3xrmknycexlsd.lib.sgfcharmer

import onion.w4v3xrmknycexlsd.lib.sgfcharmer.parse.SgfType

/** @suppress */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class Api

/** @suppress */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.PROPERTY
)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class Impl

/** @suppress */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class Util

// info property value keys
object SgfInfoKeys {
    private val _PL: (SgfType.Color.Value) -> String = { "${it.string()}to play" }
    private val _DM: (SgfType.Double.Value) -> String = { "${it.string()}even position" }
    private val _GB: (SgfType.Double.Value) -> String = { "${it.string()}good for black" }
    private val _GW: (SgfType.Double.Value) -> String = { "${it.string()}good for white" }
    private val _HO: (SgfType.Double.Value) -> String = { "${it.string()}interesting position" }
    private val _UC: (SgfType.Double.Value) -> String = { "${it.string()}unclear position" }
    private val _BM: (SgfType.Double.Value) -> String = { "${it.string()}bad move" }
    private val _TE: (SgfType.Double.Value) -> String = { "${it.string()}good move" }
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

    val PL = mapOf(
        SgfType.Color.Value.BLACK to _PL(
            SgfType.Color.Value.BLACK
        ),
        SgfType.Color.Value.WHITE to _PL(
            SgfType.Color.Value.WHITE
        )
    )
    val DM = getMap(_DM)
    val GB = getMap(_GB)
    val GW = getMap(_GW)
    val HO = getMap(_HO)
    val UC = getMap(_UC)
    val BM = getMap(_BM)
    val TE = getMap(_TE)

    const val C = ""
    const val GC = ""
    const val N = ""
    const val V = "Score"
    const val DO = "doubtful move"
    const val IT = "interesting move"
    const val AN = "Annotations by"
    const val BR = "Black player rank"
    const val BT = "Black team"
    const val CP = "Copyright"
    const val DT = "Date of game"
    const val EV = "Event"
    const val GN = "Game"
    const val ON = "Opening"
    const val OT = "Byo-yomi"
    const val PB = "Black player"
    const val PC = "Place"
    const val PW = "White player"
    const val RE = "Result"
    const val RO = "Round"
    const val RU = "Rule set"
    const val SO = "Source"
    const val TM = "Time limits"
    const val US = "Game entered by"
    const val WR = "White player rank"
    const val WT = "White team"
    const val BL = "Time left for black/s"
    const val OB = "Moves left for black"
    const val OW = "Moves left for white"
    const val WL = "Time left for white/s"
    const val HA = "Handicap"
    const val KM = "Komi"
    const val APN = "Application name"
    const val APV = "Application version"
}