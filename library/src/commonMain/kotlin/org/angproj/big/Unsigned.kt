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
 *      Kristoffer Paulsson - adaption to Angelos Project
 */
package org.angproj.big

import org.angproj.sec.util.ensure

/**
 * This object provides methods to create `BigInt` instances from unsigned byte arrays.
 * It is used internally to handle the conversion of byte arrays into `BigInt` objects
 * without leading zero bytes.
 * */
public object Unsigned {

    /**
     * Strips leading zero bytes from the data and constructs an IntArray with the remaining bytes.
     *
     * This is a helper function that processes unsigned byte data and converts it to the internal
     * integer array representation used by BigInt, removing any unnecessary leading zeros.
     *
     * @param firstOctet The first octet to start processing from
     * @param data The data source from which to read octets
     * @param size The total size of the data
     * @param readOctet A lambda function that reads a single octet at the given index
     * @return An IntArray representing the significant bytes, or an empty IntArray if the result is zero
     *
     * @see internalOf
     */
    private fun <E> stripLeadingZeroBytes(
        firstOctet: Int, data: E, size: Int, readOctet: E.(i: Int) -> Byte
    ): IntArray {
        var octet = firstOctet
        var index = 1

        while (octet == BigSigned.POSITIVE.signed && index < size) {
            octet = data.readOctet(index++).toInt()
        }
        if (octet == 0) {
            return intArrayOf()
        }

        val result = IntArray((size - index).div(4) + 1)

        var first = octet and 0xFF
        repeat((size - index).mod(4)) {
            first = first shl 8 or (data.readOctet(index++).toInt() and 0xff)
        }
        result[0] = first

        repeat((size - index).div(4)) {
            result[it + 1] = (data.readOctet(index++).toInt() shl 24) or
                    ((data.readOctet(index++).toInt() and 0xff) shl 16) or
                    ((data.readOctet(index++).toInt() and 0xff) shl 8) or
                    (data.readOctet(index++).toInt() and 0xff)
        }
        return result
    }

    /**
     * Creates a `[BigInt]` instance from an unsigned byte array.
     *
     * Interprets the byte array as an unsigned big-endian integer. The result is always
     * non-negative, and any leading zero bytes are stripped.
     *
     * @param bytes the byte array representing the magnitude of the `BigInt`.
     * @return a `BigInt` instance with the specified magnitude and positive sign, or zero sign if all bytes are zero.
     * @throws BigMathException if the byte array is empty or has zero length.
     */
    public fun internalOf(bytes: ByteArray): BigInt = internalOf(bytes, bytes.size) { this[it] }

    /**
     * Creates a `[BigInt]` from an unsigned byte-like data source.
     *
     * Generic function that interprets bytes as an unsigned big-endian integer.
     * The result is always non-negative. Leading zero bytes are stripped before creating the BigInt.
     *
     * @param data the data source (ByteArray, List, etc.).
     * @param size the number of bytes to read.
     * @param readOctet lambda to read a byte at the specified index.
     * @return a `BigInt` instance with [BigSigned.POSITIVE] or [BigSigned.ZERO] sign.
     * @throws BigMathException if size is zero.
     */
    public fun <E> internalOf(data: E, size: Int, readOctet: E.(i: Int) -> Byte): BigInt {
        ensure(size > 0) { BigMathException("Zero length magnitude") }

        val firstOctet = data.readOctet(0).toInt()
        val mag = stripLeadingZeroBytes(firstOctet, data, size, readOctet)
        return BigInt(mag, if (mag.isEmpty()) BigSigned.ZERO else BigSigned.POSITIVE)
    }
}