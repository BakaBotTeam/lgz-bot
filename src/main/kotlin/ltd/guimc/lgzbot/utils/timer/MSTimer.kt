package ltd.guimc.lgzbot.utils.timer

import kotlin.properties.Delegates

class MSTimer() {
    private var initTime by Delegates.notNull<Long>()

    init {
        this.initTime = System.currentTimeMillis()
    }

    fun isTimePressed(ms: Long): Boolean {
        return System.currentTimeMillis() - ms >= this.initTime
    }

    fun reset() {
        this.initTime = System.currentTimeMillis()
    }

    fun hasTimePassed(): Long {
        return System.currentTimeMillis() - this.initTime
    }
}