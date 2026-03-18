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

import org.angproj.sec.util.TypeSize
import org.angproj.sec.util.ensure


/**
 * Converts an [Int] to a [Long] by treating it as unsigned.
 *
 * Masks the Int with 0xFFFFFFFF to produce the unsigned representation.
 *
 * @return the unsigned [Long] representation of this Int.
 */
internal fun Int.longMask(): Long = this.toLong() and 0xFFFFFFFFL

/**
 * Sets a [Long] value in this [IntArray] at the specified index using big-endian ordering.
 *
 * Stores the low 32 bits of the Long at position (lastIndex - index).
 * Uses reverse indexing where index 0 refers to the rightmost position.
 *
 * @param index the reverse index to set (0-based from the right).
 * @param value the [Long] value to store.
 */
internal fun IntArray.longSet(index: Int, value: Long) {
    this[lastIndex - index] = value.toInt()
}

/**
 * Sets an [Int] value in this [IntArray] at the specified index using big-endian ordering.
 *
 * Stores the value at position (lastIndex - index).
 * Uses reverse indexing where index 0 refers to the rightmost position.
 *
 * @param index the reverse index to set (0-based from the right).
 * @param value the [Int] value to store.
 */
internal fun IntArray.intSet(index: Int, value: Int) {
    this[lastIndex - index] = value
}

/**
 * Retrieves an [Int] value from this [IntArray] at the specified index using big-endian ordering.
 *
 * Reads from position (lastIndex - index).
 * Uses reverse indexing where index 0 refers to the rightmost position.
 *
 * @param index the reverse index to get (0-based from the right).
 * @return the [Int] value at the specified position.
 */
internal fun IntArray.intGet(index: Int): Int = this[lastIndex - index]

/**
 * Finds the position of the first non-zero [Int] element from the right.
 *
 * Returns the distance from the right to the first non-zero element,
 * or the array size if all elements are zero.
 *
 * @return the index of the first non-zero element from the right.
 */
internal fun IntArray.firstNonzero(): Int = LoadAndSaveBigInt.firstNonZeroIntNum(this)

/**
 * Calculates the number of [Int] units needed to represent a [BigInt] with the given sign.
 *
 * Returns (bitLength / 32) + 1, accounting for the sign when calculating bit length.
 *
 * @param sigNum the sign of the [BigInt].
 * @return the number of Int units needed.
 */
internal fun IntArray.intLength(sigNum: BigSigned): Int = LoadAndSaveBigInt.intLength(this, sigNum)

/**
 * Gets an [Int] value at the specified position with sign extension for two's complement.
 *
 * For negative numbers, applies two's complement sign extension.
 * For positive numbers, returns the magnitude value as-is.
 *
 * @param index the position (0-based from the right).
 * @param sigNum the sign of the [BigInt].
 * @param firstNonZero the cached position of first non-zero element.
 * @return the value with sign extension applied.
 */
internal fun IntArray.intGetComp(
    index: Int, sigNum: BigSigned, firstNonZero: Int
): Int = LoadAndSaveBigInt.getInt(index, this, sigNum, firstNonZero)

/**
 * Gets an [Int] value at the specified direct array index with sign extension.
 *
 * Similar to [intGetComp] but uses direct array indexing instead of reverse.
 * For negative numbers, applies two's complement sign extension.
 *
 * @param index the direct array index.
 * @param sigNum the sign of the [BigInt].
 * @param firstNonZero the cached position of first non-zero element.
 * @return the value with sign extension applied.
 */
internal fun IntArray.intGetCompUnrev(
    index: Int, sigNum: BigSigned, firstNonZero: Int
): Int = LoadAndSaveBigInt.getIntUnrev(index, this, sigNum, firstNonZero)

/**
 * Converts a reverse index to a forward array index.
 *
 * Calculates: lastIndex - index
 *
 * @param index the reverse index (0 = rightmost element).
 * @return the forward index in the array.
 */
internal fun IntArray.rev(index: Int): Int = this.lastIndex - index


/**
 * Internal utility object for loading and saving BigInt data.
 *
 * This object provides methods to:
 * - Convert BigInt magnitude arrays to/from ByteArray representations
 * - Calculate bit length and bit count for BigInt values
 * - Access and manipulate individual integers in a BigInt's magnitude array
 * - Handle signed/unsigned integer conversions
 * - Compute bit positions and lengths
 *
 * These operations are internal implementation details and should not be used directly
 * outside of the BigInt implementation.
 */
public object LoadAndSaveBigInt {

    /**
     * Calculates the number of bits required to represent an [Int].
     *
     * Returns the position of the highest set bit plus one.
     *
     * @param n the integer value.
     * @return the number of bits required (0 for zero, up to 32 for Int).
     */
    public fun bitLengthForInt(n: Int): Int = TypeSize.intBits - n.countLeadingZeroBits()

    /**
     * Calculates the number of [Int] units needed to represent a [BigInt].
     *
     * Returns (bitLength / 32) + 1.
     *
     * @param mag the magnitude array.
     * @param sigNum the sign of the [BigInt].
     * @return the number of Int units required.
     */
    public fun intLength(mag: IntArray, sigNum: BigSigned): Int = (bitLength(mag, sigNum) ushr 5) + 1

    /**
     * Calculates the number of bits required to represent a [BigInt].
     *
     * For empty magnitude, returns 0.
     * For positive numbers, returns the position of the highest set bit plus one.
     * For negative numbers, special handling for powers of two:
     * - If magnitude is a power of 2: returns bitLength - 1
     * - Otherwise: returns normal bitLength
     *
     * @param mag the magnitude array.
     * @param sigNum the sign of the [BigInt].
     * @return the minimum number of bits needed.
     */
    public fun bitLength(mag: IntArray, sigNum: BigSigned): Int = when (mag.isEmpty()) {
        true -> 0
        else -> {
            val magBitLength: Int = (mag.lastIndex shl 5) + bitLengthForInt(mag[0])
            when (sigNum.isNegative()) {
                true -> {
                    // Check if magnitude is a power of two
                    var pow2 = mag[0].countOneBits() == 1
                    var i = 1
                    while (i < mag.size && pow2) {
                        pow2 = mag[i] == 0
                        i++
                    }

                    when (pow2) {
                        true -> magBitLength - 1
                        else -> magBitLength
                    }
                }

                else -> magBitLength
            }
        }
    }

    /**
     * Counts the number of 1-bits in the two's complement representation of a [BigInt].
     *
     * For positive numbers, counts 1-bits directly.
     * For negative numbers, applies two's complement logic.
     *
     * @param mag the magnitude array.
     * @param sigNum the sign of the [BigInt].
     * @return the count of set bits.
     */
    public fun bitCount(mag: IntArray, sigNum: BigSigned): Int {
        var count = mag.sumOf { it.countOneBits() }
        if (sigNum.isNegative()) {
            var magTrailingZeroCount = 0
            var j: Int = mag.lastIndex
            while (mag[j] == 0) {
                magTrailingZeroCount += TypeSize.intBits
                j--
            }
            magTrailingZeroCount += mag[j].countTrailingZeroBits()
            count += magTrailingZeroCount - 1
        }
        return count
    }

    private fun signInt(sigNum: BigSigned): Int = if (sigNum.isNegative()) -1 else 0

    /**
     * Finds the index of the first non-zero [Int] element from the right.
     *
     * @param mag the magnitude array.
     * @return the distance from the right to the first non-zero element, or array size if all zero.
     */
    public fun firstNonZeroIntNum(mag: IntArray): Int {
        val mlen: Int = mag.size
        var i: Int = mlen - 1
        while (i >= 0 && mag[i] == 0) {
            i--
        }
        return mlen - i - 1
    }

    /**
     * Retrieves an [Int] value at position n with sign extension for negative numbers.
     *
     * For negative n, returns 0.
     * For position >= array size, returns sign extension (0 for positive, -1 for negative).
     * For negative BigInt: applies two's complement sign extension.
     *
     * @param n the position (0-based from the right).
     * @param mag the magnitude array.
     * @param sigNum the sign of the [BigInt].
     * @param firstNonZero the cached position of first non-zero element.
     * @return the value with sign extension applied.
     */
    public fun getInt(n: Int, mag: IntArray, sigNum: BigSigned, firstNonZero: Int): Int {
        if (n < 0) return 0
        if (n >= mag.size) return signInt(sigNum)

        val magInt: Int = mag[mag.lastIndex - n]

        return if (sigNum.isNonNegative()) magInt else if (n <= firstNonZero) -magInt else magInt.inv()
    }

    /**
     * Retrieves an [Int] value at direct array index n with sign extension for negative numbers.
     *
     * Similar to [getInt] but uses direct indexing instead of reverse.
     *
     * @param n the direct array index.
     * @param mag the magnitude array.
     * @param sigNum the sign of the [BigInt].
     * @param firstNonZero the cached position of first non-zero element.
     * @return the value with sign extension applied.
     */
    public fun getIntUnrev(n: Int, mag: IntArray, sigNum: BigSigned, firstNonZero: Int): Int {
        if (n < 0) return 0
        if (n >= mag.size) return signInt(sigNum)

        val magInt: Int = mag[n]

        return if (sigNum.isNonNegative()) magInt else if (n <= firstNonZero) -magInt else magInt.inv()
    }

    /**
     * Converts a [BigInt] magnitude to a [ByteArray] representation.
     *
     * Produces a two's complement representation in big-endian byte order.
     *
     * @param mag the magnitude array.
     * @param sigNum the sign of the [BigInt].
     * @return a [ByteArray] in big-endian format.
     */
    public fun toByteArray(mag: IntArray, sigNum: BigSigned): ByteArray = toArbitraryByte(
        mag, sigNum,
        { idx, data -> this[idx] = data }
    ) { ByteArray(it) }

    /**
     * Generic function to convert [BigInt] to any byte-like format.
     *
     * Converts the BigInt magnitude to a custom byte container using the provided factory and writer.
     *
     * @param mag the magnitude array.
     * @param sigNum the sign of the [BigInt].
     * @param writeOctet lambda to write a byte at the specified index.
     * @param factory lambda to create the result container of the specified size.
     * @return the result in the specified format.
     */
    public fun <E> toArbitraryByte(
        mag: IntArray, sigNum: BigSigned,
        writeOctet: E.(Int, Byte) -> Unit,
        factory: (Int) -> E
    ): E {
        val firstNonZero = firstNonZeroIntNum(mag)
        val byteLen: Int = bitLength(mag, sigNum) / 8 + 1
        val byteData = factory(byteLen)

        var i = byteLen - 1
        var bytesCopied = 4
        var nextInt = 0
        var intIndex = 0
        while (i >= 0) {
            if (bytesCopied == 4) {
                nextInt = getInt(intIndex++, mag, sigNum, firstNonZero)
                bytesCopied = 1
            } else {
                nextInt = nextInt ushr 8
                bytesCopied++
            }
            byteData.writeOctet(i, nextInt.toByte())
            i--
        }
        return byteData
    }

    /**
     * Creates a [BigInt] from a [ByteArray].
     *
     * Interprets the bytes as a signed big-endian integer in two's complement format.
     *
     * @param bytes the byte array to convert.
     * @return a [BigInt] instance.
     * @throws BigMathException if the byte array is empty.
     */
    public fun internalOf(bytes: ByteArray): BigInt = internalOf(bytes, bytes.size) { this[it] }

    /**
     * Strips leading zero bytes from data with unsigned interpretation.
     *
     * Processes unsigned byte data and converts it to the internal integer array representation.
     * Removes leading zero bytes and constructs the magnitude array.
     *
     * @param firstOctet the first byte value (as Int).
     * @param data the data source.
     * @param size the total size of the data.
     * @param readOctet lambda to read a byte at the specified index.
     * @return an IntArray representing the significant bytes, or empty if zero.
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

    private fun <E> makePositive(
        firstOctet: Int, data: E, size: Int, readOctet: E.(i: Int) -> Byte
    ): IntArray {
        var octet: Int = firstOctet
        var index = 1

        while (octet == BigSigned.NEGATIVE.signed && index < size) {
            octet = data.readOctet(index++).toInt()
        }


        var first = -1 shl 8 or (octet and 0xFF)
        repeat((size - index).mod(4)) {
            octet = data.readOctet(index++).toInt()
            first = first shl 8 or (octet and 0xFF)
        }
        val realSize = index

        while (octet == BigSigned.POSITIVE.signed && index < size) {
            octet = data.readOctet(index++).toInt()
        }

        var second = octet and 0xFF
        repeat((size - index).mod(4)) {
            second = second shl 8 or (data.readOctet(index++).toInt() and 0xff)
        }

        val extra = if ((size - index or first or second) == 0) 1 else 0
        val result = IntArray(extra + 1 + (size - realSize).div(4))
        result[0] = if (extra == 0) first else -1
        var idx = result.size - (size - index).div(4)
        if (idx > 1) result[idx - 1] = second

        repeat((size - index).div(4)) {
            result[idx++] = (data.readOctet(index++).toInt() shl 24) or
                    ((data.readOctet(index++).toInt() and 0xff) shl 16) or
                    ((data.readOctet(index++).toInt() and 0xff) shl 8) or
                    (data.readOctet(index++).toInt() and 0xff)
        }

        while (--idx >= 0 && result[idx] == 0) {
        }
        result[idx] = -result[idx]
        while (--idx >= 0) {
            result[idx] = result[idx].inv()
        }
        return result
    }

    /**
     * Creates a [BigInt] from any byte-like data source.
     *
     * Generic function that interprets bytes as a signed big-endian integer in two's complement format.
     * Handles both positive and negative (two's complement) representations.
     *
     * @param data the data source (ByteArray, List, etc.).
     * @param size the number of bytes to read.
     * @param readOctet lambda to read a byte at the specified index.
     * @return a [BigInt] instance.
     * @throws BigMathException if size is zero.
     */
    public fun <E> internalOf(data: E, size: Int, readOctet: E.(i: Int) -> Byte): BigInt {
        ensure(size > 0) { BigMathException("Zero length magnitude") }

        val firstOctet = data.readOctet(0).toInt()
        return when (firstOctet < 0) {
            true -> BigInt(makePositive(firstOctet, data, size, readOctet), BigSigned.NEGATIVE)
            else -> {
                val mag = stripLeadingZeroBytes(firstOctet, data, size, readOctet)
                BigInt(mag, if (mag.isEmpty()) BigSigned.ZERO else BigSigned.POSITIVE)
            }
        }
    }
}