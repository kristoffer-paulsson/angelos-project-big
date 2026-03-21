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

import org.angproj.sec.util.ceilDiv
import org.angproj.sec.util.TypeSize
import org.angproj.sec.rand.InitializationVector


object Sampler {
    fun abstractByteArray(size: Int, seed: Long = 0): ByteArray {
        var state = -(InitializationVector.entries[0].iv xor seed).inv() * 5
        return ByteArray(size) {
            state = -state.inv() * 5
            (state ushr 32).toByte()
        }
    }

    fun abstractBigInt(bitLength: Int, seed: Long = 0): BigInt {
        require(bitLength >= 0) { "Must be more than negative bits long" }
        val value = bigIntOf(abstractByteArray(bitLength.ceilDiv(TypeSize.byteBits)+4, seed)).abs()
        val valueBitLength = value.bitLength

        return when {
            valueBitLength == bitLength -> value
            valueBitLength > bitLength -> value.shiftRight(valueBitLength - bitLength)
            else -> error("Won't happen!")
        }
    }
}