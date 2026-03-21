/**
 * Copyright (c) 2026 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
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
import kotlin.test.assertFailsWith

import java.math.BigInteger as JavaBigInteger

class ToString {
    @Test
    fun testToString() {
        assertEquals(BigInt.one.toString(), JavaBigInteger.valueOf(1).toString(10))
        assertEquals(BigInt.minusOne.toString(), JavaBigInteger.valueOf(-1).toString(10))
        assertEquals(BigInt.zero.toString(), JavaBigInteger.valueOf(0).toString(10))
        assertEquals(bigIntOf(15).toString(16), JavaBigInteger.valueOf(15).toString(16))
    }

    @Test
    fun testWrongRadix() {
        assertFailsWith<IllegalArgumentException> {
            BigInt.two.toString(1)
        }
        assertFailsWith<IllegalArgumentException> {
            BigInt.two.toString(17)
        }
    }
}