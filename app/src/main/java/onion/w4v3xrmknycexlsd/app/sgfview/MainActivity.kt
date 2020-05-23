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

package onion.w4v3xrmknycexlsd.app.sgfview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import onion.w4v3xrmknycexlsd.lib.sgfcharm.SgfController
import onion.w4v3xrmknycexlsd.lib.sgfcharm.getSgfController
import onion.w4v3xrmknycexlsd.lib.sgfcharm.putSgfController

const val SGFSAVE = "sgf"

class MainActivity : AppCompatActivity() {

    lateinit var controller: SgfController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        controller =
            savedInstanceState?.getSgfController(SGFSAVE) ?: SgfController(
                showVariations = true,
                interactionMode = SgfController.InteractionMode.FREE_PLAY
            ).load("(;SZ[3])")
        controller.into(sgfview)

        website_button.setOnClickListener { controller.load(resources.getString(R.string.sgf_website_example)) }
        alphago_button.setOnClickListener { controller.load(resources.getString(R.string.lee_sedol_vs_alpha_go)) }
        shuusaku_button.setOnClickListener { controller.load(resources.getString(R.string.ear_reddening_game)) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSgfController(SGFSAVE, controller)
    }
}
