package org.angproj.big

class LoadBitcoinTest {
    abstract class SecpP256Koblitz1 {
        val p: String = "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" +
                "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFE" + "FFFFFC2F"
        val a: String = "00000000" + "00000000" + "00000000" + "00000000" +
                "00000000" + "00000000" + "00000000" + "00000000"
        val b: String = "00000000" + "00000000" + "00000000" + "00000000" +
                "00000000" + "00000000" + "00000000" + "00000007"
        val Gc: String = "02" +
                "79BE667E" + "F9DCBBAC" + "55A06295" + "CE870B07" +
                "029BFCDB" + "2DCE28D9" + "59F2815B" + "16F81798"
        val G: String = "04" +
                "79BE667E" + "F9DCBBAC" + "55A06295" + "CE870B07" +
                "029BFCDB" + "2DCE28D9" + "59F2815B" + "16F81798" +
                "483ADA77" + "26A3C465" + "5DA4FBFC" + "0E1108A8" +
                "FD17B448" + "A6855419" + "9C47D08F" + "FB10D4B8"
        val n: String = "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFE" +
                "BAAEDCE6" + "AF48A03B" + "BFD25E8C" + "D0364141"
        val h: String = "01"
    }
}