/**
 * Copyright (c) 2025 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
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

/**
 * Finds the greatest common divisor between this [BigInt] and [b].
 *
 * This function uses Euclidean algorithm for efficient computation.
 * The result is always non-negative. The GCD of 0 and any number is that number.
 *
 * @param b The second number to compute the GCD against.
 * @return A new [BigInt] representing the greatest common divisor of this value and [b].
 */
public fun BigInt.gcd(b: BigInt): BigInt = BigInt.innerGcd(this.abs(), b.abs())

internal tailrec fun BigInt.Companion.innerGcd(a: BigInt, b: BigInt): BigInt {
    if (b.compareSpecial(BigInt.zero).isEqual())
        return a
    return innerGcd(b, a.mod(b))
}