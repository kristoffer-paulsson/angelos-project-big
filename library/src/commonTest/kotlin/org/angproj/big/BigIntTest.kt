/**
 * Copyright (c) 2024 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
 *
 * This software is available under the terms of the MIT license. Parts are licensed
 * under different terms if stated. The legal terms are attached to the LICENSE file
 * and are made available on:
 *
 *      https://opensource.org/licenses/MIT
 *
 * SPDX-License-Identifier: MIT
 *
 * Contributors:
 *      Kristoffer Paulsson - initial implementation
 */
package org.angproj.big

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BigIntTest {

    @Test
    fun testNull() {
        assertTrue(BigInt.nullObject.isNull())
        assertFalse(BigInt.zero.isNull())
    }

    /**
     * This test recognizes that BigInt can predict its export size properly.
     * */
    @Test
    fun testToSize() {
        val randomBytes = ByteArray(33) { it.toByte() }
        val bigInt = bigIntOf(randomBytes)

        assertEquals(bigInt.getByteSize(), bigInt.toByteArray().size)
     }

    /**
     * This test recognizes whether large zero unsigned big integer
     * translates into zero magnitude and sigNum properly.
     * */
    @Test
    fun testUnsignedBigIntOf() {
    }

    @Test
    fun testArbitraryBitCount() {
        val fuzz = listOf(
            Pair("a20100000000000000", 68)
        )

        fuzz.forEach {
            val bitCount = bigIntOf(it.first.fromHexSymbols()).bitCount
            assertEquals(bitCount, it.second)
        }
    }
}