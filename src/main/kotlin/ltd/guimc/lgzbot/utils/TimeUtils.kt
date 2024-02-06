/*
 * THIS FILE IS PART OF lgz-bot PROJECT
 *
 * You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.
 * Your modified application must also be licensed under the AGPLv3.
 *
 * Copyright (c) 2022 - now Guimc Team.
 */

package ltd.guimc.lgzbot.utils

import java.text.DateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


object TimeUtils {
    fun convertDate(dateInMilliseconds: Long): String {
        return Instant.ofEpochMilli(dateInMilliseconds)
            .atZone(ZoneId.of("Asia/Shanghai"))
            .toLocalDateTime()
            .format(
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
            )
    }
}