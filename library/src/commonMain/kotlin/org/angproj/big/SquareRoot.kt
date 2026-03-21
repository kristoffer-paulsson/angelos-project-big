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
 * Contributors:
 *      Kristoffer Paulsson - Port to Kotlin and adaption to Angelos Project
 */
package org.angproj.big

import org.angproj.sec.util.ensure

/**
 * Computes the integer square root of this BigInt.
 *
 * This function uses a binary search algorithm to find the largest integer whose
 * square is less than or equal to this BigInt.
 *
 * @return The integer square root of this BigInt.
 * @throws BigMathException if this BigInt is negative.
 */
public fun BigInt.sqrt(): BigInt = sqrtAndRemainder().first

/**
 * Computes the integer square root of this BigInt and the remainder.
 *
 * This function uses a binary search algorithm to find the square root and then
 * calculates the remainder as (this - (sqrt * sqrt)).
 *
 * @return A Pair where the first element is the integer square root of this BigInt,
 *         and the second element is the remainder (this - (sqrt * sqrt)).
 * @throws BigMathException if this BigInt is negative.
 */
public fun BigInt.sqrtAndRemainder(): Pair<BigInt, BigInt> {
    ensure<BigMathException>(this >= BigInt.zero) { BigMathException("Square root of negative BigInt is not supported.") }
    if (this == BigInt.zero || this == BigInt.one) return Pair(this, BigInt.zero)

    var low = BigInt.zero
    var high = this
    var mid: BigInt

    while (low < high) {
        mid = (low + high + BigInt.one) shr 1
        val midSq = mid * mid
        when {
            midSq > this -> high = mid - BigInt.one
            else -> low = mid
        }
    }
    val remainder = this - (low * low)
    return Pair(low, remainder)
}
