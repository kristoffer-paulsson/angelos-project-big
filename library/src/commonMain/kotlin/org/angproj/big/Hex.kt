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


/**
 * Converts a hexadecimal string to a [ByteArray].
 *
 * This function accepts hexadecimal strings with either uppercase or lowercase letters (0-9, A-F, a-f).
 * If the string has an odd length, it is treated as if a leading '0' were prepended to make the length even.
 *
 * For example:
 * - "FF" -> [0xFF]
 * - "F" -> [0x0F]
 * - "ABCD" -> [0xAB, 0xCD]
 * - "" -> []
 *
 * @return A [ByteArray] containing the decoded bytes.
 * @throws IllegalArgumentException if an invalid hexadecimal character is found.
 */
public fun String.fromHexSymbols(): ByteArray {
    if (isEmpty()) return byteArrayOf()

    val odd = length % 2
    val byteLen = length.ceilDiv(2)
    val bytes = ByteArray(byteLen)

    if (odd == 1) bytes[0] = fromHexChar<Unit>(this[0].code).toByte()

    var strIdx = odd
    repeat(byteLen - odd) {
        val high = fromHexChar<Unit>(this[strIdx].code)
        val low = fromHexChar<Unit>(this[strIdx + 1].code)
        bytes[it + odd] = ((high shl 4) or low).toByte()
        strIdx += 2
    }
    return bytes
}

/**
 * Converts a single hexadecimal character to its integer value.
 *
 * @param c The character code (from [Char.code]) of a hexadecimal digit.
 * @return The integer value (0-15) of the hexadecimal digit.
 * @throws IllegalArgumentException if the character is not a valid hexadecimal digit (0-9, A-F, a-f).
 */
private inline fun <reified E : Any> fromHexChar(c: Int): Int = when (c) {
    in 0x30..0x39 -> c - 0x30 // 0-9
    in 0x61..0x66 -> 10 + c - 0x61 // a-f
    in 0x41..0x46 -> 10 + c - 0x41 // A-F
    else -> error("Invalid hexadecimal character: $c")
}