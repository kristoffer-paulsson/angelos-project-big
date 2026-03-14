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
import kotlin.test.assertContentEquals

class HexTest {

    // ========== Normal Cases ==========

    @Test
    fun testFromHexSymbols_emptyString() {
        val result = "".fromHexSymbols()
        assertContentEquals(byteArrayOf(), result)
    }

    @Test
    fun testFromHexSymbols_singleByte_lowercaseHex() {
        val result = "ff".fromHexSymbols()
        assertContentEquals(byteArrayOf(0xFF.toByte()), result)
    }

    @Test
    fun testFromHexSymbols_singleByte_uppercaseHex() {
        val result = "FF".fromHexSymbols()
        assertContentEquals(byteArrayOf(0xFF.toByte()), result)
    }

    @Test
    fun testFromHexSymbols_singleByte_mixedCase() {
        val result = "aB".fromHexSymbols()
        assertContentEquals(byteArrayOf(0xAB.toByte()), result)
    }

    @Test
    fun testFromHexSymbols_multipleBytes_lowercase() {
        val result = "deadbeef".fromHexSymbols()
        assertContentEquals(byteArrayOf(0xDE.toByte(), 0xAD.toByte(), 0xBE.toByte(), 0xEF.toByte()), result)
    }

    @Test
    fun testFromHexSymbols_multipleBytes_uppercase() {
        val result = "DEADBEEF".fromHexSymbols()
        assertContentEquals(byteArrayOf(0xDE.toByte(), 0xAD.toByte(), 0xBE.toByte(), 0xEF.toByte()), result)
    }

    @Test
    fun testFromHexSymbols_multipleBytes_mixedCase() {
        val result = "DeAdBeEf".fromHexSymbols()
        assertContentEquals(byteArrayOf(0xDE.toByte(), 0xAD.toByte(), 0xBE.toByte(), 0xEF.toByte()), result)
    }

    @Test
    fun testFromHexSymbols_allDigits() {
        val result = "0123456789".fromHexSymbols()
        assertContentEquals(byteArrayOf(0x01.toByte(), 0x23.toByte(), 0x45.toByte(), 0x67.toByte(), 0x89.toByte()), result)
    }

    @Test
    fun testFromHexSymbols_allLowercaseLetters() {
        val result = "abcdef".fromHexSymbols()
        assertContentEquals(byteArrayOf(0xAB.toByte(), 0xCD.toByte(), 0xEF.toByte()), result)
    }

    @Test
    fun testFromHexSymbols_allUppercaseLetters() {
        val result = "ABCDEF".fromHexSymbols()
        assertContentEquals(byteArrayOf(0xAB.toByte(), 0xCD.toByte(), 0xEF.toByte()), result)
    }

    @Test
    fun testFromHexSymbols_zeros() {
        val result = "00000000".fromHexSymbols()
        assertContentEquals(byteArrayOf(0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte()), result)
    }

    // ========== Edge Cases: Odd Length Strings ==========

    @Test
    fun testFromHexSymbols_oddLength_singleDigit() {
        // Single digit "f" should be treated as "0f"
        val result = "f".fromHexSymbols()
        assertContentEquals(byteArrayOf(0x0F.toByte()), result)
    }

    @Test
    fun testFromHexSymbols_oddLength_singleDigit_uppercase() {
        // Single digit "F" should be treated as "0F"
        val result = "F".fromHexSymbols()
        assertContentEquals(byteArrayOf(0x0F.toByte()), result)
    }

    @Test
    fun testFromHexSymbols_oddLength_singleDigit_zero() {
        // Single digit "0" should be treated as "00"
        val result = "0".fromHexSymbols()
        assertContentEquals(byteArrayOf(0x00.toByte()), result)
    }

    @Test
    fun testFromHexSymbols_oddLength_threeCharacters() {
        // Three chars "abc" should be treated as "0abc"
        val result = "abc".fromHexSymbols()
        assertContentEquals(byteArrayOf(0x0A.toByte(), 0xBC.toByte()), result)
    }

    @Test
    fun testFromHexSymbols_oddLength_fiveCharacters() {
        // Five chars should be treated with leading 0
        val result = "12345".fromHexSymbols()
        assertContentEquals(byteArrayOf(0x01.toByte(), 0x23.toByte(), 0x45.toByte()), result)
    }

    @Test
    fun testFromHexSymbols_oddLength_sevenCharacters() {
        val result = "1234567".fromHexSymbols()
        assertContentEquals(byteArrayOf(0x01.toByte(), 0x23.toByte(), 0x45.toByte(), 0x67.toByte()), result)
    }

    // ========== Edge Cases: Boundary Values ==========

    @Test
    fun testFromHexSymbols_minByte() {
        val result = "00".fromHexSymbols()
        assertContentEquals(byteArrayOf(0x00.toByte()), result)
    }

    @Test
    fun testFromHexSymbols_maxByte() {
        val result = "ff".fromHexSymbols()
        assertContentEquals(byteArrayOf(0xFF.toByte()), result)
    }

    @Test
    fun testFromHexSymbols_maxByte_uppercase() {
        val result = "FF".fromHexSymbols()
        assertContentEquals(byteArrayOf(0xFF.toByte()), result)
    }

    @Test
    fun testFromHexSymbols_largeHexString() {
        // Test with a reasonably large hex string
        val input = "0123456789abcdefABCDEF0123456789abcdefABCDEF"
        val result = input.fromHexSymbols()
        assertEquals(22, result.size)
    }

    // ========== Error Cases ==========

    @Test
    fun testFromHexSymbols_invalidCharacter_lowercase_g() {
        assertFailsWith<IllegalStateException> {
            "gg".fromHexSymbols()
        }
    }

    @Test
    fun testFromHexSymbols_invalidCharacter_uppercase_G() {
        assertFailsWith<IllegalStateException> {
            "GG".fromHexSymbols()
        }
    }

    @Test
    fun testFromHexSymbols_invalidCharacter_space() {
        assertFailsWith<IllegalStateException> {
            "aa bb".fromHexSymbols()
        }
    }

    @Test
    fun testFromHexSymbols_invalidCharacter_hyphen() {
        assertFailsWith<IllegalStateException> {
            "aa-bb".fromHexSymbols()
        }
    }

    @Test
    fun testFromHexSymbols_invalidCharacter_specialCharacter() {
        assertFailsWith<IllegalStateException> {
            "aa@bb".fromHexSymbols()
        }
    }

    @Test
    fun testFromHexSymbols_invalidCharacter_lowercaseLetters_outsideRange() {
        assertFailsWith<IllegalStateException> {
            "z".fromHexSymbols()
        }
    }

    @Test
    fun testFromHexSymbols_invalidCharacter_highCharacterCode() {
        assertFailsWith<IllegalStateException> {
            "\u0080\u0081".fromHexSymbols()
        }
    }

    @Test
    fun testFromHexSymbols_invalidCharacter_oddLength_invalidFirstChar() {
        assertFailsWith<IllegalStateException> {
            "g5".fromHexSymbols()
        }
    }

    @Test
    fun testFromHexSymbols_invalidCharacter_oddLength_invalidSecondChar() {
        assertFailsWith<IllegalStateException> {
            "5g".fromHexSymbols()
        }
    }

    // ========== Additional Normal Cases ==========

    @Test
    fun testFromHexSymbols_allBoundaryDigits() {
        // Test 0 and 9 boundaries
        val result = "0909".fromHexSymbols()
        assertContentEquals(byteArrayOf(0x09.toByte(), 0x09.toByte()), result)
    }

    @Test
    fun testFromHexSymbols_allBoundaryLetters_lowercase() {
        // Test a and f boundaries
        val result = "afaf".fromHexSymbols()
        assertContentEquals(byteArrayOf(0xAF.toByte(), 0xAF.toByte()), result)
    }

    @Test
    fun testFromHexSymbols_alternatingPattern() {
        val result = "a0b1c2d3".fromHexSymbols()
        assertContentEquals(byteArrayOf(0xA0.toByte(), 0xB1.toByte(), 0xC2.toByte(), 0xD3.toByte()), result)
    }

    @Test
    fun testFromHexSymbols_repeatingPattern() {
        val result = "aaaa".fromHexSymbols()
        assertContentEquals(byteArrayOf(0xAA.toByte(), 0xAA.toByte()), result)
    }
}