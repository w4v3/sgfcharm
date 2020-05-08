package onion.w4v3xrmknycexlsd.lib.sgfview.data

/**
 * The parser object for reading strings into [SgfTree]s.
 *
 * It is very lenient, in that it will not throw errors for incorrect input, but it will try to
 * parse it as good as it can. Of course, that means that for incorrect input, the results might be
 * completely wrong (it will not try to correct anything).
 *
 * Note that only one SGF `GameTree` per input is considered, following ones will be ignored.
 */
object SgfParser {
    /** Parses the [sgfString] representing an SGF `GameTree` into an [SgfTree] object. */
    fun parseSgfTree(sgfString: String): SgfTree {
        // first, we get rid of the property values between [ ] as they might contain literal chars
        // that would complicate the parsing using RegEx's
        // also, watching out for \ characters appearing before the ] for escaping
        // and we use the chance to get rid of whitespace
        val sgfStripped = sgfString.replace(Regex("\\[.*?[^\\\\]]"), "").replace(Regex("\\s"), "")
        // we should save the properties, though
        val propStringList = Regex("\\[(.*?)[^\\\\]]")
            .findAll(sgfString)
            .map { it.groupValues[1] }
            .toList()
        // note that [parseNodes] just contains a skeleton, we need to repopulate it with the properties
        val parseSgfTree = parseSgfTreeHelper(sgfStripped)

        return populateParseSgfTree(parseSgfTree, propStringList)
    }

    private fun parseSgfTreeHelper(strippedSgfString: String): ParseSgfTree {
        // now the string is either of the form "\([^()]*\)", which would be a Sequence without
        // further GameTrees, or "\([^()]*\(.*", in which case we need to go through the rest of the
        // string to determine the other GameTrees, balancing parentheses.
        val sequenceMatches = Regex("\\(([^()]*)(\\(.*\\))?\\)").find(strippedSgfString)
        // the Sequence would be the first captured group
        val sequenceString = sequenceMatches?.groupValues?.getOrNull(1)
        // the GameTrees start at the second group
        val gameTreeListStart = sequenceMatches?.groups?.get(2)?.range?.start
        val gameTreeStringList = mutableListOf<String>()

        gameTreeListStart?.let {
            var treeLevel = 0 // parentheses counter
            for (c in strippedSgfString.drop(gameTreeListStart)) {
                when (c) {
                    '(' -> {
                        treeLevel++
                        // if top level, make new string
                        if (treeLevel == 1) gameTreeStringList.add("")
                    }
                    ')' -> treeLevel--
                }
                gameTreeStringList.last().plus(c) // append character to current string
            }
        }

        // parse the individual components
        val parseNodes =  sequenceString?.let { parseSgfSequence(it) } ?: emptyList()
        val children = gameTreeStringList.map { parseSgfTreeHelper(it) }

        return ParseSgfTree(parseNodes, children)
    }

    /** Parses the [sequenceString] representing an SGF `Sequence` into a list of [SgfNode]s. */
    private fun parseSgfSequence(sequenceString: String): List<ParseSgfNode> {
        // the nodes in a sequence are separated by ';', easy enough
        return sequenceString.split(";").map { parseSgfNode(it) }
    }

    /** Parses the [nodeString] representing an SGF `Node` into a list of [SgfProperty]s. */
    private fun parseSgfNode(nodeString: String): List<ParseSgfProperty> {
        val parseProperties = mutableListOf<ParseSgfProperty>()

        return Regex("([A-Z]+)(\\[]+)").findAll(nodeString).map { node ->
            ParseSgfProperty(
                node.groupValues.getOrNull(1),
                node.groupValues.getOrNull(2)?.count {
                    it == ']'
                }
            )
        }.toList()

        //for (c in nodeString) {
        //    var newProperty = true
        //    when (c) {
        //        '[' -> newProperty = true
        //        ']' -> {
        //            parseProperties.lastOrNull()?.propValNum?.inc()
        //            newProperty = true
        //        }
        //        else -> if (newProperty) {
        //            parseProperties.add(ParseSgfProperty("$c"))
        //            newProperty = false
        //        } else parseProperties.lastOrNull()?.ident?.plus(c)
        //    }
        //}

        //return parseProperties
    }

    private fun populateParseSgfTree(
        parseSgfTree: ParseSgfTree,
        propValueStringList: List<String>
    ): SgfTree {
        val sequence = parseSgfTree.nodes.map { node ->
            node.map { property ->
                when (property.ident) {
                }
            }
        }
    }

}