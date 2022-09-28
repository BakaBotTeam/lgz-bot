/*
 * THIS FILE IS PART OF lgz-bot PROJECT
 *
 * You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.
 * Your modified application must also be licensed under the AGPLv3.
 *
 * Copyright (c) 2022 - now Guimc Team.
 */

package ltd.guimc.lgzbot.utils

import org.json.JSONArray
import org.json.JSONObject
import java.util.*

object HttpUtils {
    fun getJsonObject(url: String, auth: String? = null): JSONObject {
        val connection = java.net.URL(url).openConnection() as java.net.HttpURLConnection
        connection.requestMethod = "GET"
        if (auth != null) {
            val basicAuth = "Basic : " + String(Base64.getEncoder().encode(auth.toByteArray()))
            connection.setRequestProperty("Authorization", basicAuth)
        }
        connection.connect()
        // 转换到json对象
        val raw = connection.inputStream.bufferedReader().readText()
        return JSONObject(raw)
    }

    fun getJsonArray(url: String, auth: String? = null): JSONArray {
        val connection = java.net.URL(url).openConnection() as java.net.HttpURLConnection
        connection.requestMethod = "GET"
        if (auth != null) {
            val basicAuth = "Basic : " + String(Base64.getEncoder().encode(auth.toByteArray()))
            connection.setRequestProperty("Authorization", basicAuth)
        }
        connection.connect()
        // 转换到json对象
        val raw = connection.inputStream.bufferedReader().readText()
        return JSONArray(raw)
    }

    fun getResponse(url: String): String {
        val connection = java.net.URL(url).openConnection() as java.net.HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()
        return connection.inputStream.bufferedReader().readText()
    }

    fun pushJson(url: String, json: String): String {
        val connection = java.net.URL(url).openConnection() as java.net.HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setRequestProperty("Content-Length", json.length.toString())
        connection.doOutput = true
        connection.connect()
        connection.outputStream.write(json.toByteArray())
        return connection.responseMessage
    }
}