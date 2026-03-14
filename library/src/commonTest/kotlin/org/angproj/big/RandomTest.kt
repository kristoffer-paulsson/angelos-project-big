package org.angproj.big

import org.angproj.sec.SecureRandomException
import org.angproj.sec.util.Octet.asHexSymbols
import org.angproj.sec.util.securelyRandomize
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

class RandomTest {

    /**
     * For mocking the inner functions of random
     * */
    fun BigInt.Companion.createMockBigInt(bitLength: Int): BigInt {
        return innerCreateBigint(bitLength) {
            Random.nextBytes(it)
        }
    }

    /**
     * For mocking the inner functions of random
     * */
    fun BigInt.Companion.createMockInRange(min: BigInt, max: BigInt): BigInt {
        return innerCreateInRange(min, max) {
            Random.nextBytes(it)
        }
    }

    @Test
    fun testCreateBigIntOutside() {

        assertFailsWith<BigMathException>{
            BigInt.innerCreateBigint(4097) {
                Random.nextBytes(it)
            }
        }
        assertFailsWith<BigMathException>{
            BigInt.innerCreateBigint(-1) {
                Random.nextBytes(it)
            }
        }
    }

    @Test
    fun testCreateBigIntInside() {
        assertNotEquals(BigInt.createMockBigInt(4096), BigInt.createMockBigInt(0))
    }

    @Test
    fun testEnsureCatastrophicFailure() {
        assertFailsWith<SecureRandomException>{
            BigInt.innerCreateBigint(256) {
                it.fill(0)
            }
        }
    }

    @Test
    fun testRandomTrulyFailed() {
        assertFailsWith<BigMathException> {
            BigInt.innerCreateBigint(256) {
                it.fill(-1)
            }
        }
    }

    @Test
    fun testRangeMinMaxCheck() {
        assertFailsWith<BigMathException>{
            BigInt.innerCreateInRange(
                Sampler.abstractBigInt(192),
                Sampler.abstractBigInt(128)
            ) {
                it.fill(58)
            }
        }
    }

    @Test
    fun testRangeMinMax() {
        BigInt.innerCreateInRange(
            Sampler.abstractBigInt(128),
            Sampler.abstractBigInt(192)
        ) {
            it.fill(-37)
        }
    }

    @Test
    fun testCreateEntropyBigInt() {
        val rand = BigInt.createEntropyBigInt(192)

        assertEquals(rand.bitLength, 192)
    }

    @Test
    fun testCreateEntropyInRangePositive() {

        val min = Sampler.abstractBigInt(129)
        val max = min.subtract(BigInt.minusOne)
        val inBetween = BigInt.createEntropyInRange(min, max)

        assertTrue { min.compareSpecial(inBetween).isLesserOrEqual() }
        assertTrue { max.compareSpecial(inBetween).isGreaterOrEqual() }
    }

    @Test
    fun testCreateEntropyInRangeNegative() {
        val min = Sampler.abstractBigInt(129).negate()
        val max = min.add(BigInt.one)

        val inBetween = BigInt.createEntropyInRange(min, max)

        assertTrue { min.compareSpecial(inBetween).isLesserOrEqual() }
        assertTrue { max.compareSpecial(inBetween).isGreaterOrEqual() }
    }

    @Test
    fun testCreateRandomBigInt() {
        val rand = BigInt.createRandomBigInt(192)

        assertEquals(rand.bitLength, 192)
    }

    @Test
    fun testCreateRandomInRangePositive() {

        val min = Sampler.abstractBigInt(129)
        val max = min.subtract(BigInt.minusOne)
        val inBetween = BigInt.createRandomInRange(min, max)

        assertTrue { min.compareSpecial(inBetween).isLesserOrEqual() }
        assertTrue { max.compareSpecial(inBetween).isGreaterOrEqual() }
    }

    @Test
    fun testCreateRandomInRangeNegative() {
        val min = Sampler.abstractBigInt(129).negate()
        val max = min.add(BigInt.one)

        val inBetween = BigInt.createRandomInRange(min, max)

        assertTrue { min.compareSpecial(inBetween).isLesserOrEqual() }
        assertTrue { max.compareSpecial(inBetween).isGreaterOrEqual() }
    }
}