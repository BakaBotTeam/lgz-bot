package ltd.guimc.lgzbot.utils

import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent

object RequestUtils {
    private val groups: MutableMap<Long, BotInvitedJoinGroupRequestEvent> = mutableMapOf()

    object Group {
        fun add(event: BotInvitedJoinGroupRequestEvent) = groups.put(event.eventId, event)
        fun remove(id: Long) = groups.remove(id)

        fun find(id: Long): BotInvitedJoinGroupRequestEvent {
            val events = groups.filter { it.key == id }

            if (events.isEmpty())
                throw NoSuchElementException("Can't find request")

            return events[0]!!
        }
    }
}