package ltd.guimc.lgzbot.utils

import org.json.JSONObject

object HttpUtils {
    fun getJson(url: String): JSONObject {
        val connection = java.net.URL(url).openConnection() as java.net.HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()
        // 转换到json对象
        val raw = connection.inputStream.bufferedReader().readText()
        return JSONObject(raw)
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