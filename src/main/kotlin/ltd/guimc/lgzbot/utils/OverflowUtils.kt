/*
 * THIS FILE IS PART OF lgz-bot PROJECT
 *
 * You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.
 * Your modified application must also be licensed under the AGPLv3.
 *
 * Copyright (c) 2022 - now Guimc Team.
 */

package ltd.guimc.lgzbot.utils

import net.mamoe.mirai.Bot
import top.mrxiaom.overflow.contact.RemoteBot.Companion.asRemoteBot

object OverflowUtils {
    fun checkOverflowCore(): Boolean {
        try {
            Class.forName("top.mrxiaom.overflow.internal.Overflow")
            return true
        } catch (ignored: ClassNotFoundException) {
            return false
        }
    }

    fun getOnebotServiceProviderName(bot: Bot): String {
        if (!checkOverflowCore()) return "null"
        return bot.asRemoteBot.appName
    }

    fun getOnebotServiceProviderVersion(bot: Bot): String {
        if (!checkOverflowCore()) return "null"
        return bot.asRemoteBot.appVersion
    }

    fun getOnebotConnection(): String {
        if (!checkOverflowCore()) return "null"
        val oclazz = Class.forName("top.mrxiaom.overflow.internal.Overflow")
        val instance = oclazz.getDeclaredMethod("getInstance").invoke(null)
        val method = oclazz.getDeclaredMethod("getConfig")
        method.trySetAccessible()
        val configInstance = method.invoke(instance)
        val cclazz = Class.forName("top.mrxiaom.overflow.internal.Config")
        val rPortMethod = cclazz.getDeclaredMethod("getReversedWSPort")
        rPortMethod.trySetAccessible()
        val rPort = rPortMethod.invoke(configInstance) as Int
        if (rPort in 1..65536) {
            return "rws: $rPort"
        }
        val wsHostMethod = cclazz.getDeclaredMethod("getWsHost")
        wsHostMethod.trySetAccessible()
        val wsHost = wsHostMethod.invoke(configInstance) as String
        return "ws: $wsHost"
    }
}