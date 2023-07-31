/*
 * THIS FILE IS PART OF lgz-bot PROJECT
 *
 * You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.
 * Your modified application must also be licensed under the AGPLv3.
 *
 * Copyright (c) 2022 - now Guimc Team.
 */

package ltd.guimc.lgzbot.hypixel

import ltd.guimc.lgzbot.files.Config
import org.json.JSONObject
import kotlin.math.floor
import kotlin.math.sqrt

object HypixelApiUtils {
    val BASE = 10000.0
    val GROWTH = 2500.0

    /* Constants to generate the total amount of XP to complete a level */
    val HALF_GROWTH = 0.5 * GROWTH

    /* Constants to look up the level from the total amount of XP */
    val REVERSE_PQ_PREFIX = -(BASE - 0.5 * GROWTH) / GROWTH
    val REVERSE_CONST = REVERSE_PQ_PREFIX * REVERSE_PQ_PREFIX
    val GROWTH_DIVIDES_2 = 2 / GROWTH

    fun request(url: String): JSONObject {
        val connection = java.net.URL("https://api.hypixel.net$url").openConnection() as java.net.HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("API-Key", Config.hypixelApiKey)
        connection.connect()
        // 转换到json对象
        val raw = connection.inputStream.bufferedReader().readText()
        val jsonObject = JSONObject(raw)
        if (!jsonObject.getBoolean("success")) {
            throw ApiFailedException("Request failed with message \"${jsonObject.getString("cause")}\"")
        }
        return jsonObject
    }

    fun getExactLevel(exp: Long): Double {
        return getLevel(exp) + getPercentageToNextLevel(exp)
    }

    private fun getTotalExpToFullLevel(level: Double): Double {
        return (HALF_GROWTH * (level - 2) + BASE) * (level - 1)
    }

    private fun getTotalExpToLevel(level: Double): Double {
        val lv = floor(level)
        val x0 = getTotalExpToFullLevel(lv)
        return if (level == lv) x0 else (getTotalExpToFullLevel(lv + 1) - x0) * (level % 1) + x0
    }

    private fun getPercentageToNextLevel(exp: Long): Double {
        val lv = getLevel(exp)
        val x0 = getTotalExpToLevel(lv)
        return (exp - x0) / (getTotalExpToLevel(lv + 1) - x0)
    }

    private fun getLevel(exp: Long): Double {
        return if (exp < 0) 1.0 else floor(1 + REVERSE_PQ_PREFIX + sqrt(REVERSE_CONST + GROWTH_DIVIDES_2 * exp))
    }

    fun resolveRank(rank: String): String? {
        return when (rank) {
            "ADMIN" -> "Admin"
            "MODERATOR" -> "Moderator"
            "HELPER" -> "Helper"
            "SUPERSTAR" -> "MVP++"
            "MVP_PLUS" -> "MVP+"
            "MVP" -> "MVP"
            "VIP_PLUS" -> "VIP+"
            "VIP" -> "VIP"
            else -> null
        }
    }

    fun resolveGameType(gametype: String): String {
        return when (gametype) {
            "QUAKECRAFT" -> "Quake"
            "WALLS" -> "Walls"
            "PAINTBALL" -> "Paintball"
            "SURVIVAL_GAMES " -> "Blitz Survival Games"
            "TNTGAMES" -> "TNT Games"
            "VAMPIREZ" -> "VampireZ"
            "WALLS3" -> "Mega Walls"
            "ARCADE" -> "Arcade"
            "ARENA" -> "Arena"
            "UHC" -> "UHC"
            "MCGO" -> "Cops and Crims"
            "BATTLEGROUND" -> "Warlords"
            "SUPER_SMASH" -> "Smash Heroes"
            "GINGERBREAD" -> "Turbo Kart Racers"
            "HOUSING" -> "Housing"
            "SKYWARS" -> "SkyWars"
            "TRUE_COMBAT " -> "Crazy Walls"
            "SPEED_UHC" -> "Speed UHC"
            "SKYCLASH" -> "SkyClash"
            "LEGACY" -> "Classic Games"
            "PROTOTYPE" -> "Prototype"
            "BEDWARS" -> "Bed Wars"
            "MURDER_MYSTERY " -> "Murder Mystery"
            "BUILD_BATTLE " -> "Build Battle"
            "DUELS" -> "Duels"
            "SKYBLOCK" -> "SkyBlock"
            "PIT" -> "Pit"
            "REPLAY" -> "Replay"
            "SMP" -> "SMP"
            "WOOL_GAMES" -> "Wool Wars"
            else -> "Lobby?"
        }
    }
}