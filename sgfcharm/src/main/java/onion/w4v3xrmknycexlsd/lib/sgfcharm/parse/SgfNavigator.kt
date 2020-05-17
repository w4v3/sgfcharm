/*
 *    Copyright [2020] [w4v3]
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

package onion.w4v3xrmknycexlsd.lib.sgfcharm.parse

import onion.w4v3xrmknycexlsd.lib.sgfcharm.Status

/**
 * This class offers the facilities to navigate through an `sgf GameTree` given by its constructor parameter.
 * It calls the [SgfParser] to get the first [SgfTree] arising from the string, and provides
 * functions to obtain the next and preceding nodes as well as to list and descend into possible
 * variations.
 *
 * The user can also navigate away from the tree by entering their own moves, in which case a new
 * variation is created in which the user can move back and forth. Once the last user move is undone,
 * the variation is deleted and [nextNode] will return the primary variation of the actual tree.
 *
 * @param[sgfString] the string containing the `sgf GameTree` associated with this navigator
 * @constructor creates the [SgfTree] from the given string and initializes the navigator instance on it
 */
@Status.Api
class SgfNavigator(internal val sgfString: String) {
    private var currentTree: SgfTree? = SgfParser().parseSgfCollection(sgfString).getOrNull(0)
    private var currentNodeIndex: Int = -1

    private var userBranch: Boolean = false // is the user currently branching off on their own?

    /**
     * Returns the next node of the current tree, or descends into the first variation if no next
     * node exists in the current sequence, or returns null if already at the last node of the tree.
     */
    @Status.Api
    public fun nextNode(): SgfNode? {
        // as long as we have a tree, try to get its next node
        // if such a node does not exist, descent into its children
        (currentTree?.nodes?.getOrNull(currentNodeIndex + 1))?.let { // advance one node if possible
            return it.also { currentNodeIndex++ }
        } ?: currentTree?.children?.getOrNull(0)?.let { // else descend, always into first variation
            currentTree = it
            currentNodeIndex = 0 // only reset if we actually descent
            return currentTree?.nodes?.getOrNull(currentNodeIndex)
        }

        return null // we are at the end of the tree already
    }

    /**
     * Returns the previous node of the current tree, or ascends to the parent tree if no previous
     * node exists in the current sequence, or returns null if already at the root node of the tree.
     */
    @Status.Api
    public fun previousNode(): SgfNode? {
        // analogous to [nextNode]
        (currentTree?.nodes?.getOrNull(currentNodeIndex - 1))?.let { // go back one node if possible
            return it.also { currentNodeIndex-- }
        } ?: currentTree?.parent?.let { // else go up
            currentTree = it
            currentNodeIndex = it.nodes.lastIndex // only reset if we actually ascent
            // if we ascended from a user branch, it gets deleted
            if (userBranch) deleteUserBranch()
            return currentTree?.nodes?.getOrNull(currentNodeIndex)
        }

        return null // we are at the root node already
    }

    /**
     * Carries out the [move] in the tree.
     *
     * There are three possible situations:
     * - the next node in the tree has the [move]: move to that node
     * - we are at the last node of the current sequence and there are variation trees, one of which
     * contains the [move]: move to the tree containing it
     * - neither of the above: the user is branching off, a new variation is created
     *
     * If [variationNumber] is not `null`, the navigator is forced to move to the variation with
     * that index if it exists. This is useful for letting the user navigate to variations without
     * moves in their root nodes, for example by displaying markup at custom places on the board.
     */
    @Status.Api
    public fun makeMove(
        move: SgfProperty<SgfType.Move>,
        variationNumber: Int? = null
    ): SgfNode? {
        // we have to distinguish between the case where the user is playing on their own
        // (userBranch == true) and the one where a variation from the tree might have been selected
        if (!userBranch) {
            // if the next node, or the first nodes in the children if we are at the last node,
            // contains the move carried out by the user, we move to that node or tree
            // otherwise, the user goes branching on their own
            if (currentTree?.nodes?.getOrNull(currentNodeIndex + 1)?.contains(move) == true) {
                currentNodeIndex++
            } else {
                // if currently showing variations, the user might have tapped onto one, so we should check
                if (variationNumber != null) {
                    currentTree?.children?.getOrNull(variationNumber)?.let {
                        currentTree = it
                        currentNodeIndex = 0
                    }
                } else { // no shown variation, but if not showing variations the user might have found one anyway
                    if (currentNodeIndex == currentTree?.nodes?.lastIndex) { // only makes sense if this is the last node
                        val containingChildTree = currentTree?.children
                            ?.find { it.nodes.getOrNull(0)?.contains(move) == true }
                        if (containingChildTree != null) { // if successful, descent
                            currentTree = containingChildTree
                            currentNodeIndex = 0
                        } else createUserBranch(move)
                    } else createUserBranch(move)
                }
            }
        } else addToUserBranch(move) // already branching

        return currentTree?.nodes?.getOrNull(currentNodeIndex)
    }

    // branching off, we add a new tree child for the user as the primary variation
    // and put the remaining sequence of the current tree into another variation
    private fun createUserBranch(move: SgfProperty<SgfType.Move>) {
        val userTree = SgfTree(
            currentTree,
            mutableListOf(mutableListOf(move as SgfProperty<SgfType>))
        )
        currentTree?.apply {
            // splitting the nodes at the current index
            val oldNodes = nodes.take(currentNodeIndex + 1)
            val remainingNodes = nodes.drop(currentNodeIndex + 1).toMutableList()
            nodes.clear()
            nodes.addAll(oldNodes) // the ones before get added back
            children.add(0, userTree)
            // the others go into a separate tree at position 1
            children.add(1, SgfTree(currentTree, remainingNodes))
        }
        currentTree = userTree
        currentNodeIndex = 0
        userBranch = true
    }

    // add the move to an already existing branch and discard the rest of the branch
    // in case the user had gone back after the initial branching off
    private fun addToUserBranch(move: SgfProperty<SgfType.Move>) {
        val keep = currentTree?.nodes?.take(currentNodeIndex + 1)
        currentTree?.nodes?.clear()
        keep?.let { currentTree?.nodes?.addAll(it) }
        currentTree?.nodes?.add(mutableListOf(move))
        currentNodeIndex++
    }

    // deletes the user branch, supposing that we are currently at the last node before that branch
    private fun deleteUserBranch() {
        currentTree?.apply {
            nodes.addAll(children[1].nodes) // add the old nodes back in first
            children.removeAt(1) // remove old node branch
            children.removeAt(0) // remove user branch
        }
        userBranch = false
    }

    /**
     * Returns the currently possible variations.
     *
     * If [successors] is set to `true` (default), the variations of the successor node are returned,
     * otherwise, the variations of the current node. The returned value is a list of
     * `[SgfType.Move]?`s, each of which represents a variation in the order in which
     * they appear in the tree. If the variation does not contain a move, the corresponding entry is `null`.
     */
    @Status.Api
    public fun variations(successors: Boolean = true): List<SgfType.Move?> =
        when {
            successors -> {
                // only makes sense if this is the last node of the current sequence
                // and if there are children
                takeIf { currentTree?.nodes?.getOrNull(currentNodeIndex + 1) == null }?.let {
                    currentTree?.children
                        ?.map { child ->
                            (child.nodes.getOrNull(0)
                                ?.find { it is SgfProperty.B || it is SgfProperty.W }?.value as? SgfType.Move)
                        }
                } ?: emptyList()
            }
            userBranch -> emptyList() // if siblings mode and we have branched off, no variations should be shown
            else -> {
                // this must be the first node of the sequence
                // and there must be other children in the parent
                takeIf { currentNodeIndex == 0 }?.let {
                    currentTree?.parent?.children
                        ?.map { child ->
                            (child.nodes.getOrNull(0)
                                ?.find { it is SgfProperty.B || it is SgfProperty.W }?.value as? SgfType.Move)
                        }
                } ?: emptyList()
            }
        }

    // get current position as array where each element is the index of the subtree taken from the
    // beginning, and the last element is the index of the current node
    @Status.Impl
    internal val currentIndices: IntArray
        get() {
            // user branch is ignored
            var traverseTree = if (userBranch) currentTree?.parent else currentTree
            val index =
                mutableListOf(
                    if (userBranch) currentTree?.parent?.nodes?.lastIndex ?: 0 else currentNodeIndex
                )

            while (traverseTree != null) {
                traverseTree.parent?.children?.indexOf(traverseTree)?.let { index.add(it) }
                traverseTree = traverseTree.parent
            }

            return index.toIntArray()
        }

    // reverse operation of [currentIndices]
    @Status.Impl
    internal fun goToIndices(indices: IntArray): List<SgfNode> {
        val nodes = mutableListOf<SgfNode>()

        for (i in indices.lastIndex downTo 1) {
            currentTree?.nodes?.let { nodes.addAll(it) }
            currentTree = currentTree?.children?.getOrNull(indices[i])
        }

        currentTree?.nodes?.take(indices[0] + 1)?.let { nodes.addAll(it) }
        currentNodeIndex = indices[0]

        return nodes
    }
}