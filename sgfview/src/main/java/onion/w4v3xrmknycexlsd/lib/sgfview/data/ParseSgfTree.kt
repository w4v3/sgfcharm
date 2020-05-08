package onion.w4v3xrmknycexlsd.lib.sgfview.data

/** "Fake" version of [SgfTree] for parsing without actual properties. */
internal data class ParseSgfTree(val nodes: List<ParseSgfNode>, val children: List<ParseSgfTree>)
internal typealias ParseSgfNode = List<ParseSgfProperty>
internal data class ParseSgfProperty(var ident: String?, var propValNum: Int? = 0)

