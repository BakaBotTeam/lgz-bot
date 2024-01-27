/*
 * THIS FILE IS PART OF lgz-bot PROJECT
 *
 * You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.
 * Your modified application must also be licensed under the AGPLv3.
 *
 * Copyright (c) 2022 - now Guimc Team.
 */

package ltd.guimc.lgzbot.utils

object OverflowUtils {
    fun checkOverflowCore(): Boolean {
        try {
            Class.forName("top.mrxiaom.overflow.internal.Overflow")
            return true
        } catch (ignored: ClassNotFoundException) {
            return false
        }
    }

    fun getOnebotServiceProviderName(): String {
        if (!checkOverflowCore()) return "null"
        val clazz = Class.forName("top.mrxiaom.overflow.internal.message.OnebotMessages")
        val instance = clazz.getDeclaredField("INSTANCE").get(null)
        val method = clazz.getDeclaredMethod("getAppName\$overflow_core")
        method.trySetAccessible()
        val appName = method.invoke(instance)
        return appName as String
    }

    fun getOnebotServiceProviderVersion(): String {
        if (!checkOverflowCore()) return "null"
        val clazz = Class.forName("top.mrxiaom.overflow.internal.message.OnebotMessages")
        val instance = clazz.getDeclaredField("INSTANCE").get(null)
        val method = clazz.getDeclaredMethod("getAppVersion\$overflow_core")
        method.trySetAccessible()
        val appVersion = method.invoke(instance)
        return appVersion as String
    }
}