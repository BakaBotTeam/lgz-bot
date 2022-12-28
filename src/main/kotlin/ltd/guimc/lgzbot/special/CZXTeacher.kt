/*
 * THIS FILE IS PART OF lgz-bot PROJECT
 *
 * You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.
 * Your modified application must also be licensed under the AGPLv3.
 *
 * Copyright (c) 2022 - now Guimc Team.
 */

package ltd.guimc.lgzbot.special

import ltd.guimc.lgzbot.utils.AsciiUtil
import ltd.guimc.lgzbot.utils.PinyinUtils
import ltd.guimc.lgzbot.utils.TextUtils.removeInterference
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.MessageSource.Key.recall
import net.mamoe.mirai.message.data.PlainText

class CZXTeacher {
    fun isFDPGroup(group: Group): Boolean {
        return group.id == 413868243L || group.id == 735874917L
    }

    fun checkczx(str: String): Boolean {
        val unPeekText = AsciiUtil.sbc2dbcCase(str)
            .lowercase()
            .removeInterference()
        if (Regex("czx|(ch.*n?z).*[x×]|(陈.*梓)|(梓.*希)").containsMatchIn(unPeekText)) {
            return true
        }
        return false
    }

    suspend fun specialCheck(e: GroupMessageEvent, textMessage: String) {
        val name = PinyinUtils.convertToPinyin(textMessage).lowercase().replace(" ", "")
        when {
            (((name.contains("chen") || name.contains("cheng") || name.contains("cen")) &&
                (name.contains("zi") || name.contains("zhi")) &&
                (name.contains("xi")) && (name.contains("zixi") || name.contains("zhixi"))) || checkczx(textMessage)) -> {
                if (name.contains("chenzixi"))
                    e.sender.mute(10)
                else {
                    e.sender.mute(65)
                }
                e.group.sendMessage(PlainText("你先别急，天天陈梓希陈梓希，陈梓希是你爹是吧"))
                e.sender.mute(10)
                e.message.recall()
            }

            textMessage.contains("远控") -> {
                e.group.sendMessage(PlainText("别急，下一个远控你"))
                e.sender.mute(3)
            }

            textMessage.contains("余志文") -> {
                e.group.sendMessage(PlainText("Hacked by Dimples#1337"))
                e.sender.mute(3)
            }

            textMessage.contains("李佳乐") -> {
                e.message.recall()
                e.group.sendMessage(PlainText("天天直呼其名，恶俗狗"))
                e.sender.mute(3)
            }

            textMessage.contains("崩端") || textMessage.contains("崩溃") || (textMessage.contains("fdp") && textMessage.contains(
                "崩"
            )) -> {
                e.message.recall()
                e.group.sendMessage(
                    PlainText(
                        "fdpcn崩端请看群公告解决，如果不行: \n\nTroubleshooting any problem without the error log is like driving with your eyes closed.\n" +
                            "在没有错误日志的情况下诊断任何问题,无异于闭眼开车\n" +
                            "   - Apache 官方文档Getting started篇章\n\n日志在 .minecraft/logs"
                    )
                )
                e.sender.mute(3)
            }

            textMessage.lowercase().contains("skiddermc") -> {
                e.group.sendMessage(
                    PlainText(
                        "关于SkidderMC: 之前有一个外国人加入了UnlegitMC的团队并踢出了所有的原始dev，封锁了仓库并创建了SkidderMC。所以现在的SkidderMC是由外国人谋权篡位所建，其中没有任何一个中国人或是UnlegitMC原始开发人员。特此创建名为UnlegitMinecraft的github组织，并创建FDPCN Client(别名FDP+)Dev均为中国人，原汁原味。\n" +
                            "我们的FDPCN拥有更高的帧率，更好的绕过，更美的UI，感谢大家的选择\n" +
                            "并且SkidderMC辱华，我们也不能保证有没有远控在他们的版本中。\n" +
                            "唯一Github:http://github.com/UnlegitMinecraft/FDPClientChina\n官方网站: https://fdpclient.club/\n" +
                            "QQ群: 912687006"
                    )
                )
            }
        }
    }
}
