package ltd.guimc.lgzbot.utils.hypixel

import kotlin.math.floor
import kotlin.math.sqrt

object ExpCalculator {
    val BASE = 10000.0
    val GROWTH = 2500.0

    /* Constants to generate the total amount of XP to complete a level */
    val HALF_GROWTH = 0.5 * GROWTH

    /* Constants to look up the level from the total amount of XP */
    val REVERSE_PQ_PREFIX = -(BASE - 0.5 * GROWTH) / GROWTH
    val REVERSE_CONST = REVERSE_PQ_PREFIX * REVERSE_PQ_PREFIX
    val GROWTH_DIVIDES_2 = 2 / GROWTH

    fun getExactLevel(exp: Long): Double {
        return getLevel(exp) + getPercentageToNextLevel(exp)
    }

    private fun getTotalExpToFullLevel(level: Double): Double {
        return (HALF_GROWTH * (level - 2) + BASE) * (level - 1)
    }

    private fun getTotalExpToLevel(level: Double): Double {
        val lv = floor(level)
        val x0 = getTotalExpToFullLevel(lv)
        return if (level == lv) x0 else (getTotalExpToFullLevel(lv + 1) - x0) * (level % 1) + x0
    }

    private fun getPercentageToNextLevel(exp: Long): Double {
        val lv = getLevel(exp)
        val x0 = getTotalExpToLevel(lv)
        return (exp - x0) / (getTotalExpToLevel(lv + 1) - x0)
    }

    private fun getLevel(exp: Long): Double {
        return if (exp < 0) 1.0 else floor(1 + REVERSE_PQ_PREFIX + sqrt(REVERSE_CONST + GROWTH_DIVIDES_2 * exp))
    }

    fun getBedWarsLevel(exp: Int): Double {
        var exp = exp.toDouble()
        var level = 100 * (exp / 487000).toInt()
        exp %= 487000
        if (exp < 500) return level + exp / 500
        level++
        if (exp < 1500) return level + (exp - 500) / 1000
        level++
        if (exp < 3500) return level + (exp - 1500) / 2000
        level++
        if (exp < 7000) return level + (exp - 3500) / 3500
        level++
        exp -= 7000.0
        return level + exp / 5000
    }

    fun getSkyWarsLevel(xp: Int): Double {
        val xps = intArrayOf(0, 20, 70, 150, 250, 500, 1000, 2000, 3500, 6000, 10000, 15000)
        val exp = xp.toDouble()
        if (exp >= 15000) {
            return (exp - 15000) / 10000 + 12
        } else {
            for (i in xps.indices) {
                if (exp < xps[i]) {
                    return i + (exp - xps[i - 1]) / (xps[i] - xps[i - 1])
                }
            }
        }
        return 1.0
    }
}