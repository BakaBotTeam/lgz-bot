package ltd.guimc.lgzbot.hypixel.games

object ExpCalculator {
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