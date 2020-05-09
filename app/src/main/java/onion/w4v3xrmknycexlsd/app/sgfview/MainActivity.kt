package onion.w4v3xrmknycexlsd.app.sgfview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import onion.w4v3xrmknycexlsd.lib.sgfview.SgfController

class MainActivity : AppCompatActivity() {
    val ex1 = "(;FF[4]GM[1]SZ[19];B[aa];W[bb];B[cc];W[dd];B[ad];W[bd])"
    val ex2 = "(;FF[4]GM[1]SZ[19];B[aa];W[bb](;B[cc]N[Var A];W[dd];B[ad];W[bd])\n" +
            "(;B[hh]N[Var B];W[hg])\n" +
            "(;B[gg]N[Var C];W[gh];B[hh]  (;W[hg]N[Var A];B[kk])  (;W[kl]N[Var B])))"
    val ex3 = "(;FF[5]GM[1]SZ[6]\n" +
            "  AB[bb:ee]\n" +
            " ;AW[bb][ee][dc][cd]\n" +
            " ;AW[cb][bc][be][eb][ed][de]\n" +
            " ;AE[dc][cd]\n" +
            ")"
    val ex4 = "(;FF[5]GM[1]SZ[6]\n" +
            "  AB[bb:ee]AW[bb][ee][dc][cd][cb][bc][be][eb][ed][de]\n" +
            " ;B[dd]\n" +
            ")"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SgfController().load(ex4).into(sgfview)
    }
}
