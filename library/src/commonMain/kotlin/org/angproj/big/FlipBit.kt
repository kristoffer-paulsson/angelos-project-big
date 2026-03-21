/**
 * Copyright (c) 2023-2025 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
 *
 * This software is available under the terms of the MIT license. Parts are licensed
 * under different terms if stated. The legal terms are attached to the LICENSE file
 * and are made available on:
 *
 *      https://opensource.org/licenses/MIT
 *
 * SPDX-License-Identifier: MIT
 *
 * Acknowledgement of algorithm:
 *      Josh Bloch
 *      Michael McCloskey
 *      Alan Eliasen
 *      Timothy Buktu
 *
 * Contributors:
 *      Kristoffer Paulsson - Port to Kotlin and adaption to Angelos Project
 */
package org.angproj.big

import org.angproj.sec.util.TypeSize
import org.angproj.sec.util.ensure
import kotlin.math.max

/**
 * Flips the bit at the specified position in this [BigInt].
 *
 * This operation toggles the bit at the specified position (0 becomes 1, 1 becomes 0).
 * This operation does not modify this BigInt, but returns a new BigInt with the bit flipped.
 * The position 0 refers to the least significant bit.
 *
 * @param pos The position of the bit to flip (0-indexed, where 0 is the least significant bit).
 * @return A new [BigInt] with the specified bit flipped.
 * @throws BigMathException if the position is negative.
 */
public fun BigInt.flipBit(pos: Int): BigInt = BigInt.innerFlipBit(this.mag, this.sigNum, pos).valueOf()


internal fun BigInt.Companion.innerFlipBit(x: IntArray, xSig: BigSigned, pos: Int): IntArray {
    ensure(pos >= 0) { BigMathException("Can not flip an imaginary bit at a negative position.") }

    val bigCnt = pos.floorDiv(TypeSize.intBits)
    val result = IntArray(max(x.intLength(xSig), bigCnt + 2))
    val xnz = x.firstNonzero()

    result.indices.forEach { result.intSet(it, x.intGetComp(it, xSig, xnz)) }
    result.intSet(bigCnt, result.intGet(bigCnt) xor (1 shl (pos and 31)))

    return result
}