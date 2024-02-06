package ltd.guimc.lgzbot.counter

import net.mamoe.mirai.contact.Member

class Counter(private val member: Member) {
    var wordFrequency = HashMap<String, Int>()
    var messageVl = 0
}
