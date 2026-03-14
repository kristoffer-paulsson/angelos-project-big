package org.angproj.big

import com.code_intelligence.jazzer.Jazzer
import com.code_intelligence.jazzer.api.FuzzedDataProvider
import org.angproj.sec.util.Octet.asHexSymbols
import java.util.Locale
import kotlin.test.assertContentEquals


public object FuzzerHexKt : FuzzPrefs() {
    @JvmStatic
    public fun fuzzerTestOneInput(data: FuzzedDataProvider) {
        val f1 = data.consumeBytes(128)

        assertContentEquals(f1.asHexSymbols().toUpperCase(Locale.ROOT).fromHexSymbols(), f1)
        assertContentEquals(f1.asHexSymbols().toLowerCase(Locale.ROOT).fromHexSymbols(), f1)
    }

    @JvmStatic
    public fun main(args: Array<String>) {
        Jazzer.main(arrayOf(
            "--target_class=${javaClass.name}",
            "-max_total_time=${maxTotalTime}"
        ))
    }
}