package onion.w4v3xrmknycexlsd.app.sgfview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import onion.w4v3xrmknycexlsd.lib.sgfcharmer.SgfController

class MainActivity : AppCompatActivity() {
    val ex1 = "(;FF[4]AP[Primiview:3.1]GM[1]SZ[19]GN[Gametree 1: properties]US[Arno Hollosi]C[日本語]\n" +
            "\n" +
            "(;B[pd]N[Moves, comments, annotations]\n" +
            "C[Nodename set to: \"Moves, comments, annotations\"];W[dp]GW[1]\n" +
            "C[Marked as \"Good for White\"];B[pp]GB[2]\n" +
            "C[Marked as \"Very good for Black\"];W[dc]GW[2]\n" +
            "C[Marked as \"Very good for White\"];B[pj]DM[1]\n" +
            "C[Marked as \"Even position\"];W[ci]UC[1]\n" +
            "C[Marked as \"Unclear position\"];B[jd]TE[1]\n" +
            "C[Marked as \"Tesuji\" or \"Good move\"];W[jp]BM[2]\n" +
            "C[Marked as \"Very bad move\"];B[gd]DO[]\n" +
            "C[Marked as \"Doubtful move\"];W[de]IT[]\n" +
            "C[Marked as \"Interesting move\"];B[jj];\n" +
            "C[White \"Pass\" move]W[];\n" +
            "C[Black \"Pass\" move]B[tt])\n" +
            "\n" +
            "(;AB[dd][de][df][dg][do:gq]\n" +
            "  AW[jd][je][jf][jg][kn:lq][pn:pq]\n" +
            "N[Setup]C[Black & white stones at the top are added as single stones.\n" +
            "\n" +
            "Black & white stones at the bottom are added using compressed point lists.]\n" +
            ";AE[ep][fp][kn][lo][lq][pn:pq]\n" +
            "C[AddEmpty\n" +
            "\n" +
            "Black stones & stones of left white group are erased in FF[3\\] way.\n" +
            "\n" +
            "White stones at bottom right were erased using compressed point list.]\n" +
            ";AB[pd]AW[pp]PL[B]C[Added two stones.\n" +
            "\n" +
            "Node marked with \"Black to play\".];PL[W]\n" +
            "C[Node marked with \"White to play\"])\n" +
            "\n" +
            "(;AB[dd][de][df][dg][dh][di][dj][nj][ni][nh][nf][ne][nd][ij][ii][ih][hq]\n" +
            "[gq][fq][eq][dr][ds][dq][dp][cp][bp][ap][iq][ir][is][bo][bn][an][ms][mr]\n" +
            "AW[pd][pe][pf][pg][ph][pi][pj][fd][fe][ff][fh][fi][fj][kh][ki][kj][os][or]\n" +
            "[oq][op][pp][qp][rp][sp][ro][rn][sn][nq][mq][lq][kq][kr][ks][fs][gs][gr]\n" +
            "[er]N[Markup]C[Position set up without compressed point lists.]\n" +
            "\n" +
            ";TR[dd][de][df][ed][ee][ef][fd:ff]\n" +
            " MA[dh][di][dj][ej][ei][eh][fh:fj]\n" +
            " CR[nd][ne][nf][od][oe][of][pd:pf]\n" +
            " SQ[nh][ni][nj][oh][oi][oj][ph:pj]\n" +
            " SL[ih][ii][ij][jj][ji][jh][kh:kj]\n" +
            " TW[pq:ss][so][lr:ns]\n" +
            " TB[aq:cs][er:hs][ao]\n" +
            "C[Markup at top partially using compressed point lists (for markup on white stones); listed clockwise, starting at upper left:\n" +
            "- TR (triangle)\n" +
            "- CR (circle)\n" +
            "- SQ (square)\n" +
            "- SL (selected points)\n" +
            "- MA ('X')\n" +
            "\n" +
            "Markup at bottom: black & white territory (using compressed point lists)]\n" +
            ";LB[dc:1][fc:2][nc:3][pc:4][dj:a][fj:b][nj:c]\n" +
            "[pj:d][gs:ABCDEFGH][gr:ABCDEFG][gq:ABCDEF][gp:ABCDE][go:ABCD][gn:ABC][gm:AB]\n" +
            "[mm:12][mn:123][mo:1234][mp:12345][mq:123456][mr:1234567][ms:12345678]\n" +
            "C[Label (LB property)\n" +
            "\n" +
            "Top: 8 single char labels (1-4, a-d)\n" +
            "\n" +
            "Bottom: Labels up to 8 char length.]\n" +
            "\n" +
            ";DD[kq:os][dq:hs]\n" +
            "AR[aa:sc][sa:ac][aa:sa][aa:ac][cd:cj]\n" +
            "  [gd:md][fh:ij][kj:nh]\n" +
            "LN[pj:pd][nf:ff][ih:fj][kh:nj]\n" +
            "C[Arrows, lines and dimmed points.])\n" +
            "\n" +
            "(;B[qd]N[Style & text type]\n" +
            "C[There are hard linebreaks & soft linebreaks.\n" +
            "Soft linebreaks are linebreaks preceeded by '\\\\' like this one >o\\\n" +
            "k<. Hard line breaks are all other linebreaks.\n" +
            "Soft linebreaks are converted to >nothing<, i.e. removed.\n" +
            "\n" +
            "Note that linebreaks are coded differently on different systems.\n" +
            "\n" +
            "Examples (>ok< shouldn't be split):\n" +
            "\n" +
            "linebreak 1 \"\\\\n\": >o\\\n" +
            "k<\n" +
            "linebreak 2 \"\\\\n\\\\r\": >o\\\n" +
            "\r" +
            "k<\n" +
            "linebreak 3 \"\\\\r\\\\n\": >o\\\r\n" +
            "k<\n" +
            "linebreak 4 \"\\\\r\": >o\\\r" +
            "k<]\n" +
            "\n" +
            "(;W[dd]N[W d16]C[Variation C is better.](;B[pp]N[B q4])\n" +
            "(;B[dp]N[B d4])\n" +
            "(;B[pq]N[B q3])\n" +
            "(;B[oq]N[B p3])\n" +
            ")\n" +
            "(;W[dp]N[W d4])\n" +
            "(;W[pp]N[W q4])\n" +
            "(;W[cc]N[W c17])\n" +
            "(;W[cq]N[W c3])\n" +
            "(;W[qq]N[W r3])\n" +
            ")\n" +
            "\n" +
            "(;B[qr]N[Time limits, captures & move numbers]\n" +
            "BL[120.0]C[Black time left: 120 sec];W[rr]\n" +
            "WL[300]C[White time left: 300 sec];B[rq]\n" +
            "BL[105.6]OB[10]C[Black time left: 105.6 sec\n" +
            "Black stones left (in this byo-yomi period): 10];W[qq]\n" +
            "WL[200]OW[2]C[White time left: 200 sec\n" +
            "White stones left: 2];B[sr]\n" +
            "BL[87.00]OB[9]C[Black time left: 87 sec\n" +
            "Black stones left: 9];W[qs]\n" +
            "WL[13.20]OW[1]C[White time left: 13.2 sec\n" +
            "White stones left: 1];B[rs]\n" +
            "C[One white stone at s2 captured];W[ps];B[pr];W[or]\n" +
            "MN[2]C[Set move number to 2];B[os]\n" +
            "C[Two white stones captured\n" +
            "(at q1 & r1)]\n" +
            ";MN[112]W[pq]C[Set move number to 112];B[sq];W[rp];B[ps]\n" +
            ";W[ns];B[ss];W[nr]\n" +
            ";B[rr];W[sp];B[qs]C[Suicide move\n" +
            "(all B stones get captured)])\n" +
            ")" +
            "\n"
    val ex2 = "\n" +
            "(;FF[4]AP[Primiview:3.1]GM[1]SZ[19]C[Gametree 2: game-info\n" +
            "\n" +
            "Game-info properties are usually stored in the root node.\n" +
            "If games are merged into a single game-tree, they are stored in the node\\\n" +
            " where the game first becomes distinguishable from all other games in\\\n" +
            " the tree.]\n" +
            ";B[pd]\n" +
            "(;PW[W. Hite]WR[6d]RO[2]RE[W+3.5]\n" +
            "PB[B. Lack]BR[5d]PC[London]EV[Go Congress]W[dp]\n" +
            "C[Game-info:\n" +
            "Black: B. Lack, 5d\n" +
            "White: W. Hite, 6d\n" +
            "Place: London\n" +
            "Event: Go Congress\n" +
            "Round: 2\n" +
            "Result: White wins by 3.5])\n" +
            "(;PW[T. Suji]WR[7d]RO[1]RE[W+Resign]\n" +
            "PB[B. Lack]BR[5d]PC[London]EV[Go Congress]W[cp]\n" +
            "C[Game-info:\n" +
            "Black: B. Lack, 5d\n" +
            "White: T. Suji, 7d\n" +
            "Place: London\n" +
            "Event: Go Congress\n" +
            "Round: 1\n" +
            "Result: White wins by resignation])\n" +
            "(;W[ep];B[pp]\n" +
            "(;PW[S. Abaki]WR[1d]RO[3]RE[B+63.5]\n" +
            "PB[B. Lack]BR[5d]PC[London]EV[Go Congress]W[ed]\n" +
            "C[Game-info:\n" +
            "Black: B. Lack, 5d\n" +
            "White: S. Abaki, 1d\n" +
            "Place: London\n" +
            "Event: Go Congress\n" +
            "Round: 3\n" +
            "Result: Balck wins by 63.5])\n" +
            "(;PW[A. Tari]WR[12k]KM[-59.5]RO[4]RE[B+R]\n" +
            "PB[B. Lack]BR[5d]PC[London]EV[Go Congress]W[cd]\n" +
            "C[Game-info:\n" +
            "Black: B. Lack, 5d\n" +
            "White: A. Tari, 12k\n" +
            "Place: London\n" +
            "Event: Go Congress\n" +
            "Round: 4\n" +
            "Komi: -59.5 points\n" +
            "Result: Black wins by resignation])\n" +
            "))"
    val ex3 = "(;GM[1]SZ[9]FF[4]\n" +
            "  AB[ac:ic]AW[ae:ie]\n" +
            " ;DD[aa:bi][ca:ce]\n" +
            "  VW[aa:bi][ca:ee][ge:ie][ga:ia][gc:ic][gb][ib]\n" +
            ")"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SgfController().load(ex1).into(sgfview)
    }
}
