package ltd.guimc.lgzbot.listener.message

import ltd.guimc.lgzbot.files.ModuleStateConfig
import ltd.guimc.lgzbot.utils.HttpUtils
import ltd.guimc.lgzbot.utils.ImageUtils
import ltd.guimc.lgzbot.utils.MessageUtils.getPlainText
import ltd.guimc.lgzbot.utils.OverflowUtils
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.ForwardMessage
import net.mamoe.mirai.message.data.ForwardMessageBuilder
import net.mamoe.mirai.message.data.PlainText
import org.json.JSONObject
import org.jsoup.Jsoup
import top.mrxiaom.overflow.OverflowAPI

object FunListener {
    val COMMAND_PREFIX = "!"

    suspend fun onMessage(event: GroupMessageEvent) {
        when(event.message.getPlainText()) {
            "${COMMAND_PREFIX}摸鱼", "${COMMAND_PREFIX}摸鱼!", "${COMMAND_PREFIX}摸鱼.",
            "${COMMAND_PREFIX}摸鱼！", "${COMMAND_PREFIX}摸鱼。" -> {
                if (ModuleStateConfig.moyu) {
                    try {
                        event.subject.sendMessage(
                            At(event.sender) + if (!OverflowUtils.checkOverflowCore()) {
                                ImageUtils.url2imageMessage(
                                    "https://api.j4u.ink/v1/store/redirect/moyu/calendar/today.png",
                                    event.bot,
                                    event.subject
                                )
                            } else {
                                OverflowAPI.get()
                                    .imageFromFile("https://api.j4u.ink/v1/store/redirect/moyu/calendar/today.png")
                            }
                        )
                    } catch (e: Throwable) {
                        event.subject.sendMessage("处理时发生异常... \nStackTrace:")
                        event.subject.sendMessage(build2forwardMessage(e.stackTraceToString(), event.subject))
                    }
                }
            }

            "${COMMAND_PREFIX}历史上的今天" -> {
                if (ModuleStateConfig.historytoday) {
                    try {
                        event.subject.sendMessage(getHistoryToday(event.subject))
                        event.subject.sendMessage(At(event.sender))
                    } catch (e: Throwable) {
                        event.subject.sendMessage("处理时发生异常... \nStackTrace:")
                        event.subject.sendMessage(build2forwardMessage(e.stackTraceToString(), event.subject))
                    }
                }
            }
        }
    }

    private fun getHistoryToday(contact: Contact): ForwardMessage {
        val msg = ForwardMessageBuilder(contact)
        val jsonObject = HttpUtils.getJsonObject("https://api.oioweb.cn/api/common/history")
        jsonObject.getJSONArray("result").forEach {
            if (it is JSONObject) {
                msg.add(
                    contact.bot, PlainText(
                        "${it.getString("year")}: ${it.getString("title").replace("\n", "")}\n" +
                            "${Jsoup.parse(it.getString("desc")).text()}\n来源: ${it.getString("link")}"
                    )
                )
            }
        }
        return msg.build()
    }

    private fun build2forwardMessage(string: String, contact: Contact): ForwardMessage {
        return ForwardMessageBuilder(contact)
            .add(contact.bot, PlainText(string))
            .build()
    }
}