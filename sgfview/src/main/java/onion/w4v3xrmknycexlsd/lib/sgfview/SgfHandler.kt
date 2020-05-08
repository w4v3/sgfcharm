package onion.w4v3xrmknycexlsd.lib.sgfview

import onion.w4v3xrmknycexlsd.lib.sgfview.data.Piece
import onion.w4v3xrmknycexlsd.lib.sgfview.data.SgfParser
import onion.w4v3xrmknycexlsd.lib.sgfview.data.SgfTree

class SgfHandler {
    private var sgfTree: SgfTree? = null

    fun parseString(sgfString: String) {
        sgfTree = SgfParser.parseSgfTree(sgfString)
    }

    fun getBoard(): List<Piece> {
        return listOf()
    }
}