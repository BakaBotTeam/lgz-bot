package ltd.guimc.lgzbot.plugin

import ltd.guimc.lgzbot.plugin.PluginMain.adRegex
import ltd.guimc.lgzbot.plugin.PluginMain.isSuperUser
import ltd.guimc.lgzbot.plugin.PluginMain.logger
import ltd.guimc.lgzbot.plugin.files.Config
import ltd.guimc.lgzbot.plugin.utils.AsciiUtil
import ltd.guimc.lgzbot.plugin.utils.MessageUtils.getForwardMessage
import ltd.guimc.lgzbot.plugin.utils.MessageUtils.getPlainText
import ltd.guimc.lgzbot.plugin.utils.MessageUtils.getFullText
import ltd.guimc.lgzbot.plugin.utils.PinyinUtils
import ltd.guimc.lgzbot.plugin.utils.RegexUtils
import ltd.guimc.lgzbot.plugin.utils.TextUtils.findSimilarity
import ltd.guimc.lgzbot.plugin.utils.TextUtils.removeNonVisible
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import net.mamoe.mirai.console.permission.PermitteeId.Companion.permitteeId
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.isOperator
import net.mamoe.mirai.contact.isOwner
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.MessageSource.Key.recall
import java.lang.Thread.sleep
import java.time.Instant

object MessageFilter {
    private var spammerFucker = mutableMapOf<Long, Int>()
    private var spammerFucker2 = mutableMapOf<Long, Long>()
    private var repeaterFucker = mutableMapOf<Long, String>()
    private var memberVl = mutableMapOf<Long, Double>()
    private var historyMessage = mutableMapOf<Long, MutableList<MessageChain>>()

    private var messagesHandled = 0
    var riskList = ArrayList<Member>()
    fun checkczx(str :String):Boolean{
        val unPeekText = AsciiUtil.sbc2dbcCase(str).lowercase()
            .replace(" ", "")
            .replace(",", "")
            .replace(".", "")
            .replace("!", "")
            .replace("?", "")
            .replace(";", "")
            .replace(":", "")
            .replace("\"", "")
            .replace("'", "")
            .replace("“", "")
            .replace("”", "")
            .replace("‘", "")
            .replace("’", "")
            .replace("<", "")
            .replace(">", "")
            .replace("(", "")
            .replace(")", "")
            .replace("內", "内")
        var regex=Regex("[Cc][Zz][Xx]|[阝东木辛希]|[ch.*n][z.*][x.*]");
        if (regex.containsMatchIn(unPeekText)) {
            logger.info("匹配成功")
            return true
        }
        return false
    }
    suspend fun filter(e: GroupMessageEvent) {
        // 检查权限

        val textMessage = e.message.getPlainText()
                                .removeNonVisible()
        val forwardMessage = e.message.getFullText()
        val stringLength = 0/*if (e.sender in riskList) 10 else 35*/

        if (forwardMessage.length == 0 && textMessage.length == 0) return
        val name=PinyinUtils.convertToPinyin(textMessage).lowercase().replace(" ","");
        if(e.group.id==912687006L) when {
            (((name.contains("chen") || name.contains("cheng") || name.contains("cen")) &&
                (name.contains("zi") || name.contains("zhi")) &&
                (name.contains("xi")) && (name.contains("zixi") || name.contains("zhixi"))) || checkczx(textMessage)) -> {
                    if(name.contains("chenzixi"))
                        e.sender.mute(10);
                    else{
                        e.sender.mute(65);
                    }
                    e.group.sendMessage(PlainText("你先别急，天天陈梓希陈梓希，陈梓希是你爹是吧"))
                    e.sender.mute(10)
                    e.message.recall()
                }
                else -> {
                    when {
                        textMessage.contains("远控") -> {
                            e.group.sendMessage(PlainText("别急，下一个远控你"))
                            e.sender.mute(3)
                        }
                        else -> {
                            if (textMessage.contains("余志文")) {
                                e.group.sendMessage(PlainText("Hacked by Dimples#1337"))
                                e.sender.mute(3)
                            }else{
                                if (textMessage.contains("李佳乐")) {
                                    e.message.recall()
                                    e.group.sendMessage(PlainText("天天直呼其名，恶俗狗"))
                                    e.sender.mute(3)
                                }else{
                                    if (textMessage.contains("崩端") || textMessage.contains("崩溃") || (textMessage.contains("fdp") && textMessage.contains("崩"))) {
                                        e.message.recall()
                                        e.group.sendMessage(PlainText("fdpcn崩端请看群公告解决，如果不行: \n\nTroubleshooting any problem without the error log is like driving with your eyes closed.\n" +
                                            "在没有错误日志的情况下诊断任何问题,无异于闭眼开车\n" +
                                            "   - Apache 官方文档Getting started篇章\n\n日志在 .minecraft/logs"))
                                        e.sender.mute(3)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        if(textMessage.lowercase().contains("skiddermc")){
            e.group.sendMessage(PlainText("关于SkidderMC: 之前有一个外国人加入了UnlegitMC的团队并踢出了所有的原始dev，封锁了仓库并创建了SkidderMC。所以现在的SkidderMC是由外国人谋权篡位所建，其中没有任何一个中国人或是UnlegitMC原始开发人员。特此创建名为UnlegitMinecraft的github组织，并创建FDPCN Client(别名FDP+)Dev均为中国人，原汁原味。\n" +
                "我们的FDPCN拥有更高的帧率，更好的绕过，更美的UI，感谢大家的选择\n" +
                "并且SkidderMC辱华，我们也不能保证有没有远控在他们的版本中。\n" +
                "唯一Github:http://github.com/UnlegitMinecraft/FDPClientChina\n官方网站: https://fdpclient.club/\n" +
                "QQ群: 912687006"))
        }

        if (e.sender.permission.level >= e.group.botPermission.level) return
        if ((RegexUtils.matchRegex(adRegex, textMessage) && textMessage.length >= stringLength) ||
            textMessage.length == 0 && RegexUtils.matchRegex(adRegex, forwardMessage)) {
            try {
                e.group.sendMessage(PlainText("嘘~"))
                e.message.recall()
                if (!e.sender.permitteeId.hasPermission(isSuperUser)) {
                    e.sender.mute(30)
                }
            }
            catch (_: Exception) {}
            riskList.add(e.sender)
            setVl(e.sender.id, 99.0)
            messagesHandled++
            e.intercept()
        }

        if (memberVl[e.sender.id] == null) {
            clearVl(e.sender.id)
            riskList.remove(e.sender)
        }

        if (memberVl[e.sender.id]!! <= 25 && e.sender in riskList) {
            riskList.remove(e.sender)
            //e.group.sendMessage(PlainText("你已经被移出了风险管控，请不要再发送广告了~")
        }

        if (historyMessage[e.sender.id] == null) {
            historyMessage[e.sender.id] = mutableListOf()
        } else if (historyMessage[e.sender.id]!!.size >= Config.historyMessageLimit) {
            historyMessage[e.sender.id]?.removeAt(0)
        }

        historyMessage[e.sender.id]?.add(e.message)

        // Anti Spammers
        val timestamp = Instant.now()

        if (spammerFucker[e.sender.id] == null) {
            spammerFucker[e.sender.id] = 1
            spammerFucker2[e.sender.id] = timestamp.epochSecond
        } else if (timestamp.epochSecond - spammerFucker2[e.sender.id]!! >= 1) {
            spammerFucker[e.sender.id] = 1
            spammerFucker2[e.sender.id] = timestamp.epochSecond
        } else {
            spammerFucker[e.sender.id] = spammerFucker[e.sender.id]!! + 1
        }

        if (spammerFucker[e.sender.id]!! >= 3) {
            addVl(e.sender.id, 30.0)
        }

        // 重复内容过滤
        if (repeaterFucker[e.sender.id] == null) {
            repeaterFucker[e.sender.id] = e.message.content
        } else {
            val slimer = findSimilarity(repeaterFucker[e.sender.id]!!, e.message.content)
            if (slimer >= 0.75) {
                addVl(e.sender.id, slimer * 25)
            } else {
                addVl(e.sender.id, -20.0)
            }
            repeaterFucker[e.sender.id] = e.message.content
        }

        // VL处罚
        if (memberVl[e.sender.id]!! >= Config.vlPunish) {
            e.group.sendMessage(PlainText("你的VL已经超过了${Config.vlPunish}了!! 你的嘴现在被我黏上了~~"))
            e.sender.mute(60)
            e.message.recall()
            try {
                historyMessage[e.sender.id]?.forEach {
                    it.recall()
                    sleep(50)
                }
            } catch (_: Exception) {}
            historyMessage[e.sender.id]?.clear()
            memberVl[e.sender.id] = .0
            messagesHandled++
            e.intercept()
        }

        // VL小于0时, 将其置为0
        if (memberVl[e.sender.id]!! < 0) {
            memberVl[e.sender.id] = .0
        }
    }

    private fun addVl(id: Long, vl: Double) {
        if (memberVl[id] == null) {
            memberVl[id] = .0
        }
        val tempValue = memberVl[id]!!
        memberVl[id] = memberVl[id]!! + vl
        if (memberVl[id]!! >= 0.0 || tempValue > 0.0) {
            logger.info("$id 的VL增加了 $vl, 现在是 ${memberVl[id]}")
        }
    }

    private fun clearVl(id: Long) {
        memberVl[id] = .0
        logger.info("$id 的VL清零了")
    }

    private fun setVl(id: Long, vl: Double) {
        memberVl[id] = vl
        logger.info("$id 的VL设置为 $vl")
    }
}
