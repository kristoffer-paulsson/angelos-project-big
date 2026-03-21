package org.angproj.big

import com.code_intelligence.jazzer.Jazzer
import com.code_intelligence.jazzer.api.FuzzedDataProvider
import java.math.BigInteger
import kotlin.test.assertEquals


public object FuzzerToStringKt : FuzzPrefs() {
    @JvmStatic
    public fun fuzzerTestOneInput(data: FuzzedDataProvider) {
        val f1 = data.consumeBytes(64)
        val f2 = data.consumeByte(2, 16).toInt()

        var e1 = false
        var e2 = false

        val r1 = try {
            bigIntOf(f1).toString(f2)
        } catch (_: BigMathException) {
            e1 = true
            byteArrayOf().toString()
        }
        val r2 = try {
            BigInteger(f1).toString(f2)
        } catch (_: NumberFormatException) {
            e2 = true
            byteArrayOf().toString()
        } catch (_: ArithmeticException) {
            e2 = true
            byteArrayOf().toString()
        }

        if(!e1 && !e2) {
            assertEquals(r1, r2)
        }
    }

    @JvmStatic
    public fun main(args: Array<String>) {
        Jazzer.main(arrayOf(
            "--target_class=${javaClass.name}",
            "-max_total_time=${maxTotalTime}"
        ))
    }
}