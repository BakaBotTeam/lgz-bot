package ltd.guimc.lgzbot.utils

class CooldownUtils(val cooldown: Long) {
    var cooldownMap: MutableMap<Any, Long> = mutableMapOf()
    var cooldownNoticeMap: MutableMap<Any, Long> = mutableMapOf()

    fun flag(target: Any) {
        cooldownMap[target] = System.currentTimeMillis()
    }

    fun isTimePassed(target: Any): Boolean {
        return isTimePassed(target, cooldown)
    }

    fun isTimePassed(target: Any, time: Long): Boolean {
        if (cooldownMap.keys.indexOf(target) == -1) return true
        if (System.currentTimeMillis() - cooldownMap[target]!! >= time) return true
        return false
    }

    fun shouldSendCooldownNotice(target: Any): Boolean {
        if (cooldownNoticeMap.keys.indexOf(target) == -1) return true
        if (System.currentTimeMillis() - cooldownNoticeMap[target]!! >= 3000) {
            cooldownNoticeMap[target] = System.currentTimeMillis()
            return true
        }
        return false
    }

    fun getLeftTime(target: Any): Long {
        return getLeftTime(target, cooldown)
    }

    fun getLeftTime(target: Any, time: Long): Long {
        if (cooldownMap.keys.indexOf(target) == -1) return -1
        return time - (System.currentTimeMillis() - cooldownMap[target]!!)
    }

    fun addLeftTime(target: Any, time: Long) {
        if (cooldownMap.keys.indexOf(target) == -1) return
        cooldownMap[target] = cooldownMap[target]!! + time
    }
}