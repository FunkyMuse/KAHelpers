package com.crazylegend.datastructuresandalgorithms.mathematics

import java.math.BigInteger


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */

/**
 * Recursively raises a number to a certain power.
 * This method returns the original number is power is 1, otherwise multiplies the number on the result of calling itself
 * with decremented power.
 * This means that the number will be multiplied on itself (the original) n amount of times, where n is the power.
 */
fun raiseToPower(num: Long, pow: Int): Long {
    return if (pow == 1) num
    else raiseToPower(num, pow - 1) * num
}

/**
 * Iteratively raises a number to a certain power.
 * For description refer to the method above.
 */
fun raiseToPowerBig(num: Long, pow: Int): BigInteger {
    var result = BigInteger.valueOf(num)
    for (i in pow downTo 2) result *= BigInteger.valueOf(num)
    return result
}
