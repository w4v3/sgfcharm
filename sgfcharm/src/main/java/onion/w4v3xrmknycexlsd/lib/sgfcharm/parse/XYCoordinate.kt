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

package onion.w4v3xrmknycexlsd.lib.sgfcharm.parse

import onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType.Point

/**
 * A point at ([x], [y]), useful for Go and Chess, for example.
 *
 * @property[x] the column of the point, starting at 1 from the left
 * @property[y] the row of the point, starting at 1 from the top
 */
data class XYPoint(val x: Int, val y: Int) : SgfType.Point()

/**
 * This is analogous to [XYPoint].
 *
 * @property[point] the target [Point] of the move
 */
data class XYMove(override val point: XYPoint?) : SgfType.Move(point) {
    /** constructs the [XYMove] to the [XYPoint] with coordinates ([x], [y]). */
    constructor(x: Int, y: Int) : this(XYPoint(x, y))
}

/**
 * This is analogous to [XYPoint].
 *
 * @property[point] the [XYPoint] where to place the stone
 */
data class XYStone(override val point: XYPoint) : SgfType.Stone(point) {
    /** constructs the [XYStone] at the [XYPoint] with coordinates ([x], [y]). */
    constructor(x: Int, y: Int) : this(XYPoint(x, y))
}

