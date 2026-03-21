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
 *      Per Bothner
 *
 * Contributors:
 *      Kristoffer Paulsson - initial implementation
 */
package org.angproj.big

/**
 * Arbitrary precision integer.
 *
 * The magnitude is represented as an array of integers in big-endian order, where each integer
 * represents 32 bits of the number. The most significant integer is at index 0.
 *
 * The sign is represented by the [sigNum] property, which can be positive, negative, or zero.
 *
 * @property mag The magnitude of the BigInt as an array of integers.
 * @property sigNum The sign of the BigInt.
 */
public data class BigInt(
    public val mag: IntArray,
    public val sigNum: BigSigned
) : Comparable<BigInt> {

    /**
     * Indicates whether some other object is "equal to" this BigInt.
     *
     * @param other the reference object with which to compare.
     * @return `true` if this object is the same as the `other` argument; `false` otherwise.
     */
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        return equalsCompare(other)
    }

    /**
     * Internal equality comparison helper.
     *
     * @param x the object to compare with.
     * @return `true` if `x` represents exactly the same numeric value (sign + magnitude),
     *         `false` otherwise.
     */
    public fun equalsCompare(x: Any): Boolean {
        if (x === this) return true
        if (x !is BigInt) return false
        if (sigNum != x.sigNum) return false
        if (mag.size != x.mag.size) return false
        return mag.indices.indexOfFirst { mag[it] != x.mag[it] } == -1
    }

    /**
     * Compares this BigInt with another BigInt.
     *
     * @param other the BigInt to compare with.
     * @return a negative integer, zero, or a positive integer as this BigInt is less than, equal to, or greater than the specified BigInt.
     */
    override fun compareTo(other: BigInt): Int = compareSpecial(other).state

    /**
     * Returns a hash code value for this BigInt.
     *
     * Note: Different corporate implementations of arbitrary-precision integers may use
     * different hashing algorithms; this one follows the common pattern used by Java's
     * `BigInteger`.
     */
    override fun hashCode(): Int {
        // Truth is, different corporate implementations may have different hash algos.
        var hash = 0
        for (element in mag) {
            hash = 31 * hash + (element.toLong() and 0xffffffffL).toInt()
        }
        return hash * sigNum.state
    }

    override fun toString(): String = toString(10)

    /**
     * Checks if this BigInt is the null object representation.
     *
     * @return true if this BigInt represents the null object, false otherwise.
     */
    public fun isNull(): Boolean = nullObject === this

    public companion object {
        /**
         * The constant zero value of type `BigInt`.
         *
         * This instance is lazily initialized on first access and is guaranteed to be
         * the unique canonical representation of the number 0.
         */
        public val zero: BigInt by lazy { bigIntOf(byteArrayOf(0)) }

        /**
         * The constant minus-one value of type `BigInt`.
         *
         * This instance is lazily initialized on first access and is guaranteed to be
         * the unique canonical representation of the number -1.
         */
        public val minusOne: BigInt by lazy { bigIntOf(byteArrayOf(-1)) }

        /**
         * The constant one value of type `BigInt`.
         *
         * This instance is lazily initialized on first access and is guaranteed to be
         * the unique canonical representation of the number 1.
         */
        public val one: BigInt by lazy { bigIntOf(byteArrayOf(1)) }

        /**
         * The constant two value of type `BigInt`.
         *
         * This instance is lazily initialized on first access and is guaranteed to be
         * the unique canonical representation of the number 2.
         */
        public val two: BigInt by lazy { bigIntOf(byteArrayOf(2)) }

        /**
         * Sentinel object representing a null `BigInt`.
         */
        public val nullObject: BigInt by lazy { BigInt(intArrayOf(), BigSigned.ZERO) }
    }
}

/**
 * Converts the BigInt to an Int representation.
 *
 * If the BigInt value is outside the range of Int, the result is truncated to fit.
 *
 * @return An Int representing the value of the BigInt.
 */
public fun BigInt.toInt(): Int = ExportImportBigInt.intValue(mag, sigNum)

/**
 * Converts the BigInt to a Long representation.
 *
 * If the BigInt value is outside the range of Long, the result is truncated to fit.
 *
 * @return A Long representing the value of the BigInt.
 */
public fun BigInt.toLong(): Long = ExportImportBigInt.longValue(mag, sigNum)

/**
 * Returns the number of bits required to represent this BigInt (excluding the sign bit).
 *
 * For positive numbers, this is the position of the highest set bit plus one.
 * For negative numbers in two's complement, this accounts for the sign representation.
 * For zero, this returns 0.
 */
public val BigInt.bitLength: Int
    get() = LoadAndSaveBigInt.bitLength(mag, sigNum)

/**
 * Returns the number of bits set to 1 in the two's complement representation of this BigInt.
 *
 * For positive numbers, this counts the 1-bits in the binary representation.
 * For negative numbers, this counts the 1-bits in the two's complement representation.
 */
public val BigInt.bitCount: Int
    get() = LoadAndSaveBigInt.bitCount(mag, sigNum)

/**
 * Converts the BigInt to a ByteArray representation.
 *
 * The byte array is in two's complement big-endian format. For positive numbers,
 * this is simply the big-endian binary representation.
 *
 * @return A ByteArray representing the value of the BigInt.
 */
public fun BigInt.toByteArray(): ByteArray = LoadAndSaveBigInt.toByteArray(mag, sigNum)

/**
 * Returns the number of bytes required to represent the BigInt.
 *
 * @return The number of bytes required to represent the BigInt.
 */
public fun BigInt.getByteSize(): Int = bitLength / 8 + 1

/**
 * Creates a BigInt from a ByteArray.
 *
 * The byte array is interpreted as a signed big-endian integer in two's complement format.
 *
 * @param value The ByteArray to convert to BigInt.
 * @return A BigInt representing the given ByteArray.
 * @throws BigMathException if the byte array is empty.
 */
public fun bigIntOf(value: ByteArray): BigInt = LoadAndSaveBigInt.internalOf(value)

/**
 * Creates a BigInt from a Long value.
 *
 * @param value The Long value to convert to BigInt.
 * @return A BigInt representing the given Long value.
 */
public fun bigIntOf(value: Long): BigInt = ExportImportBigInt.valueOf(value)

/**
 * Creates a BigInt from a ByteArray, treating the bytes as an unsigned value.
 *
 * The byte array is interpreted as an unsigned big-endian integer.
 *
 * @param value The ByteArray to convert to BigInt (interpreted as unsigned).
 * @return A BigInt representing the given unsigned ByteArray.
 * @throws BigMathException if the byte array is empty.
 */
public fun unsignedBigIntOf(value: ByteArray): BigInt = Unsigned.internalOf(value)