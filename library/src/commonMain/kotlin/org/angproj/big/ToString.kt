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
 * Acknowledgement of algorithm:
 *      Per Bothner
 *
 * Contributors:
 *      Kristoffer Paulsson - initial implementation
 */
package org.angproj.big


/**
 * Returns a string representation of this BigInt in the given radix.
 *
 * @param radix the radix (base) to use; must be between 2 and 16
 * @return the string in the specified base (lowercase letters for a–f)
 * @throws IllegalArgumentException if the radix is outside 2..16
 */
public fun BigInt.toString(radix: Int = 10): String {
    if (radix !in 2..16) {
        throw IllegalArgumentException("Radix must be between 2 and 16")
    }

    if (sigNum.isZero()) {
        return "0"
    }

    val radixBig = bigIntOf(radix.toLong())
    var current = this.abs()
    val digits = mutableListOf<Char>()

    while (current > BigInt.zero) {
        val rem = current % radixBig
        digits.add(toDigit<Unit>(rem.toInt()))
        current /= radixBig
    }

    val sign = if (sigNum.isNegative()) "-" else ""
    val digitStr = digits.asReversed().joinToString("")
    return sign + digitStr
}

/** Converts a digit value (0–15) to its character representation. */
private inline fun<reified E: Any> toDigit(digit: Int): Char =
    if (digit < 10) '0' + digit else 'a' + (digit - 10)