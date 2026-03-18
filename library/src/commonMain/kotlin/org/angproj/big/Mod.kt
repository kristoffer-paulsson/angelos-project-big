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
 * Computes the modulus of this BigInt with respect to the specified modulus.
 *
 * This function returns the unique result in the range [0, modulus) such that:
 * `(this == (result + k * modulus))` for some integer k.
 *
 * Unlike the remainder operation ([rem]), the modulo operation always returns
 * a non-negative result, even if this BigInt is negative.
 *
 * @param value The modulus to apply. Must be positive.
 * @return A new BigInt in the range [0, value) representing this BigInt modulo value.
 * @throws BigMathException if the modulus is not positive (zero or negative).
 */
public fun BigInt.mod(value: BigInt): BigInt {
    ensure(value.sigNum.isPositive()) { BigMathException("Modulus must be positive.") }
    val result = remainder(value)
    return if (result.sigNum.isNonNegative()) result else result.add(value)
}