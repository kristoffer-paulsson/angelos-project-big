/**
 * Copyright (c) 2024-2026 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
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
import kotlin.test.assertContentEquals

import java.math.BigInteger as JavaBigInteger

class LoadBitcoinEccTest {
    abstract class Secpp256Koblitz1 {
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

    @Test
    fun testLoadP() {
        val curve = object : Secpp256Koblitz1() {}

        assertContentEquals(
            unsignedBigIntOf(curve.p.fromHexSymbols()).toByteArray(),
            JavaBigInteger(curve.p, 16).toByteArray()
        )
    }

    @Test
    fun testLoadA() {
        val curve = object : Secpp256Koblitz1() {}

        assertContentEquals(
            unsignedBigIntOf(curve.a.fromHexSymbols()).toByteArray(),
            JavaBigInteger(curve.a, 16).toByteArray()
        )
    }
    @Test
    fun testLoadB() {
        val curve = object : Secpp256Koblitz1() {}

        assertContentEquals(
            unsignedBigIntOf(curve.b.fromHexSymbols()).toByteArray(),
            JavaBigInteger(curve.b, 16).toByteArray()
        )
    }

    @Test
    fun testLoadGc() {
        val curve = object : Secpp256Koblitz1() {}

        assertContentEquals(
            unsignedBigIntOf(curve.Gc.fromHexSymbols()).toByteArray(),
            JavaBigInteger(curve.Gc, 16).toByteArray()
        )
    }

    @Test
    fun testLoadG() {
        val curve = object : Secpp256Koblitz1() {}

        assertContentEquals(
            unsignedBigIntOf(curve.G.fromHexSymbols()).toByteArray(),
            JavaBigInteger(curve.G, 16).toByteArray()
        )
    }

    @Test
    fun testLoadN() {
        val curve = object : Secpp256Koblitz1() {}

        assertContentEquals(
            unsignedBigIntOf(curve.n.fromHexSymbols()).toByteArray(),
            JavaBigInteger(curve.n, 16).toByteArray()
        )
    }

    @Test
    fun testLoadH() {
        val curve = object : Secpp256Koblitz1() {}

        assertContentEquals(
            unsignedBigIntOf(curve.h.fromHexSymbols()).toByteArray(),
            JavaBigInteger(curve.h, 16).toByteArray()
        )
    }
}