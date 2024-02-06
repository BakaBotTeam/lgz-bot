/*
 * THIS FILE IS PART OF lgz-bot PROJECT
 *
 * You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.
 * Your modified application must also be licensed under the AGPLv3.
 *
 * Copyright (c) 2022 - now Guimc Team.
 */

package ltd.guimc.lgzbot.utils.hypixel

import ltd.guimc.lgzbot.files.Config
import org.json.JSONObject
import kotlin.math.floor
import kotlin.math.sqrt

object HypixelApiUtils {
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