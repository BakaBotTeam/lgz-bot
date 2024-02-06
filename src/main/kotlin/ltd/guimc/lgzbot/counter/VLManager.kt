package ltd.guimc.lgzbot.counter

import net.mamoe.mirai.contact.Member

object VLManager {
    private val counters = HashMap<Member, Counter>()
    fun getCounter(p: Member): Counter {
        var c = counters[p]
        if (c == null) {
            c = Counter(p)
            counters[p] = c
        }
        return c
    }
}
