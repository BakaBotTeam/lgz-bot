package ltd.guimc.lgzbot.listener.message

import ltd.guimc.lgzbot.files.ModuleStateConfig
import ltd.guimc.lgzbot.utils.HttpUtils
import ltd.guimc.lgzbot.utils.MessageUtils.getPlainText
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.ForwardMessage
import net.mamoe.mirai.message.data.ForwardMessageBuilder
import net.mamoe.mirai.message.data.PlainText

object FunListener {
    suspend fun onMessage(event: GroupMessageEvent) {
        when(event.message.getPlainText()) {
            "摸鱼", "摸鱼!", "摸鱼.",
            "摸鱼！", "摸鱼。" -> {
                if (ModuleStateConfig.moyu) {
                    try {
                        event.subject.sendMessage(At(event.sender) + getMoyu())
                    } catch (e: Throwable) {
                        event.subject.sendMessage("处理时发生异常... \nStackTrace:")
                        event.subject.sendMessage(build2forwardMessage(e.stackTraceToString(), event.subject))
                    }
                }
            }

            "历史上的今天" -> {
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

            "今日运势" -> {
                if (ModuleStateConfig.fortune) {
                    try {
                        event.subject.sendMessage(At(event.sender) + getEveryLuck(event.sender.id))
                    } catch (e: Throwable) {
                        event.subject.sendMessage("处理时发生异常... \nStackTrace:")
                        event.subject.sendMessage(build2forwardMessage(e.stackTraceToString(), event.subject))
                    }
                }
            }
        }
    }

    private fun getMoyu(): String {
        return HttpUtils.getJsonObject("http://bjb.yunwj.top/php/mo-yu/php.php")
            .getString("wb")
            .replace("【换行】", "\n")
    }

    private fun getHistoryToday(contact: Contact): ForwardMessage {
        val msg = ForwardMessageBuilder(contact)
        msg.add(contact.bot, PlainText(HttpUtils.getResponse("https://www.ipip5.com/today/api.php?type=txt")))
        return msg.build()
    }

    private fun getEveryLuck(qq: Long): String {
        val data = HttpUtils.getJsonObject("https://api.fanlisky.cn/api/qr-fortune/get/$qq")
            .getJSONObject("data")

        return "今日运势: ${data.getString("fortuneSummary")}\n" +
            "运势星级: ${data.getString("luckyStar")}\n" +
            "签文: ${data.getString("signText")}\n" +
            "解签: ${data.getString("unSignText")}"
    }

    private fun build2forwardMessage(string: String, contact: Contact): ForwardMessage {
        return ForwardMessageBuilder(contact)
            .add(contact.bot, PlainText(string))
            .build()
    }
}