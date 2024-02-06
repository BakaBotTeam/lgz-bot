package ltd.guimc.lgzbot.utils

import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent

object RequestUtils {
    private val groups: MutableMap<Long, BotInvitedJoinGroupRequestEvent> = mutableMapOf()

    object Group {
        fun add(event: BotInvitedJoinGroupRequestEvent) = groups.put(event.eventId, event)
        fun remove(id: Long) = groups.remove(id)

        fun find(id: Long): BotInvitedJoinGroupRequestEvent {
            try {
                return groups[id]!!
            } catch (e: NullPointerException) {
                throw NoSuchElementException("Can't find the event")
            } catch (e: Throwable) {
                throw e
            }
        }
    }
}