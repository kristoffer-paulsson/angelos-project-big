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
 * Converts an [IntArray] to a [BigInt].
 *
 * @return a [BigInt] instance created from the integer array.
 */
internal fun IntArray.valueOf(): BigInt = ExportImportBigInt.valueOf(this)


/**
 * Internal utility object for converting between BigInt and its internal integer array representation.
 *
 * This object provides methods to:
 * - Convert between BigInt and signed/unsigned integer arrays
 * - Handle leading zero stripping and two's complement conversion
 * - Export BigInt values to primitive types (Int, Long)
 * - Manage signage for positive, negative, and zero values
 *
 * These operations are internal implementation details and should not be used directly
 * outside of the BigInt implementation.
 */
public object ExportImportBigInt {

    private inline fun <reified R : Any> stripKeep(data: IntArray): Int {
        val vlen = data.size
        var keep = 0
        while (keep < vlen && data[keep] == 0) {
            keep++
        }
        return keep
    }

    private fun stripLeadingZeroInts(data: IntArray): IntArray {
        return data.copyOfRange(stripKeep<Unit>(data), data.size)
    }

    /**
     * Removes leading zero integers from an array without validation.
     *
     * Strips leading zero elements from the input array and returns the remaining portion.
     * If the array contains no leading zeros, returns the original array unmodified.
     *
     * @param data the integer array to process.
     * @return a new array with leading zeros removed, or the original array if no leading zeros exist.
     */
    public fun trustedStripLeadingZeroInts(data: IntArray): IntArray {
        val keep = stripKeep<Unit>(data)
        return if (keep == 0) data else data.copyOfRange(keep, data.size)
    }

    private inline fun <reified R : Any> positiveKeep(data: IntArray): Int {
        val vlen = data.size
        var keep = 0
        while (keep < vlen && data[keep] == -1) {
            keep++
        }
        return keep
    }

    /**
     * Converts a negative integer array (two's complement) to positive magnitude representation.
     *
     * Converts the given array representing a negative value in two's complement form
     * into a positive magnitude array suitable for use with [BigSigned.NEGATIVE].
     *
     * @param data the integer array in two's complement negative form.
     * @return a new array representing the positive magnitude.
     */
    public fun makePositive(data: IntArray): IntArray {
        val keep = positiveKeep<Unit>(data)
        var j = keep
        while (j < data.size && data[j] == 0) {
            j++
        }

        val extraInt = (if (j == data.size) 1 else 0)
        val result = IntArray(data.size - keep + extraInt)

        for (i in keep..<data.size) result[i - keep + extraInt] = data[i].inv()
        var i = result.size - 1
        while (++result[i] == 0) {
            i--
        }

        return result
    }

    /**
     * Creates a [BigInt] from an integer array, determining sign from the first element.
     *
     * Interprets the array as a signed big-endian integer. If the first element is negative,
     * the array is treated as two's complement and converted appropriately.
     *
     * @param data the integer array to convert.
     * @return a [BigInt] instance with appropriate magnitude and sign.
     * @throws BigMathException if the array is empty.
     */
    public fun internalOf(data: IntArray): BigInt {
        ensure(data.isNotEmpty()) { BigMathException("Zero length magnitude") }
        return when (data[0] < 0) {
            true -> BigInt(makePositive(data), BigSigned.NEGATIVE)
            else -> {
                val mag = trustedStripLeadingZeroInts(data)
                BigInt(mag, if (mag.isEmpty()) BigSigned.ZERO else BigSigned.POSITIVE)
            }
        }
    }

    /**
     * Creates a [BigInt] from a magnitude array and explicit sign without validation.
     *
     * Directly creates a BigInt from the provided magnitude and sign.
     * If the magnitude is empty, the sign is overridden to [BigSigned.ZERO].
     * This method trusts that the magnitude is already properly normalized.
     *
     * @param magnitude the magnitude array (absolute value).
     * @param sigNum the sign ([BigSigned.POSITIVE], [BigSigned.NEGATIVE], or [BigSigned.ZERO]).
     * @return a [BigInt] instance with the specified magnitude and sign.
     */
    public fun internalOf(magnitude: IntArray, sigNum: BigSigned): BigInt {
        return BigInt(magnitude, if (magnitude.isEmpty()) BigSigned.ZERO else sigNum)
    }

    private fun internalOf(value: Long): BigInt {
        var newValue = value
        val newSig = when (newValue < 0) {
            true -> {
                newValue = -newValue
                BigSigned.NEGATIVE
            }

            else -> BigSigned.POSITIVE
        }

        val newMag = when (val highWord = (newValue ushr TypeSize.intBits).toInt()) {
            0 -> IntArray(1).also { it[0] = newValue.toInt() }
            else -> IntArray(2).also {
                it[0] = highWord
                it[1] = newValue.toInt()
            }
        }
        return BigInt(newMag, newSig)
    }

    /**
     * Creates a [BigInt] from a sign and magnitude array with validation.
     *
     * Strips leading zeros from the magnitude and validates that the sign is consistent
     * with the magnitude (non-zero magnitude must have non-zero sign).
     *
     * @param sigNum the sign to apply.
     * @param magnitude the magnitude array (may contain leading zeros).
     * @return a normalized [BigInt] instance.
     * @throws BigMathException if the sign is [BigSigned.ZERO] but magnitude is non-empty.
     */
    public fun externalOf(sigNum: BigSigned, magnitude: IntArray): BigInt {
        val mag: IntArray = stripLeadingZeroInts(magnitude)
        val newSigNum = when {
            mag.isEmpty() -> BigSigned.ZERO
            else -> {
                ensure(!sigNum.isZero()) { BigMathException("Signum magnitude mismatch") }
                sigNum
            }
        }
        return BigInt(mag, newSigNum)
    }

    /**
     * Creates a [BigInt] from an integer array, determining sign from the first element.
     *
     * If the first element is positive or zero, creates a [BigInt] with [BigSigned.POSITIVE].
     * If the first element is negative, converts from two's complement.
     *
     * @param value the integer array (where the first element's sign determines the BigInt's sign).
     * @return a [BigInt] instance.
     */
    public fun valueOf(value: IntArray): BigInt = when {
        value[0] > 0 -> internalOf(value, BigSigned.POSITIVE)
        else -> internalOf(value)
    }

    /**
     * Creates a [BigInt] from a [Long] value.
     *
     * Converts a primitive Long to a BigInt. If the value is 0, returns the cached [BigInt.zero].
     *
     * @param value the Long value to convert.
     * @return a [BigInt] instance, or [BigInt.zero] if value is 0.
     */
    public fun valueOf(value: Long): BigInt = when {
        value == 0L -> BigInt.zero
        else -> internalOf(value)
    }

    /**
     * Extracts the [Int] value from a [BigInt]'s magnitude, applying sign if negative.
     *
     * Gets the least significant 32 bits from the magnitude and applies the sign.
     * The result is truncated if the value exceeds the [Int] range.
     *
     * @param x the magnitude array.
     * @param xSig the sign of the BigInt.
     * @return the [Int] value with sign applied.
     */
    public fun intValue(x: IntArray, xSig: BigSigned): Int = x.intGetComp(
        0, xSig, x.firstNonzero()
    )

    /**
     * Extracts the [Long] value from a [BigInt]'s magnitude, applying sign if negative.
     *
     * Combines the least significant two 32-bit integers into a 64-bit value and applies the sign.
     * The result is truncated if the value exceeds the [Long] range.
     *
     * @param x the magnitude array.
     * @param xSig the sign of the BigInt.
     * @return the [Long] value with sign applied.
     */
    public fun longValue(x: IntArray, xSig: BigSigned): Long {
        val firstNonZero = x.firstNonzero()
        var result: Long = 0
        for (i in 1 downTo 0) result = (result shl TypeSize.intBits) + (x.intGetComp(i, xSig, firstNonZero).longMask())
        return result
    }
}