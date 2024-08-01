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
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.isUploaded
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import java.io.IOException
import java.io.InputStream


object ImageUtils {
    suspend fun url2imageMessage(url: String, bot: Bot, subject: Contact): Image {
        val httpclients = HttpClients.createDefault()
        val httpget = HttpGet(url)
        val response = httpclients.execute(httpget)
        if (response.statusLine.statusCode == 200) {
            val entity = response.entity
            if (entity != null) {
                val inputstream = entity.content
                if (inputstream != null) {
                    val image = subject.uploadImage(inputstream.toExternalResource())
                    if (image.isUploaded(bot)) {
                        return image
                    }
                }
            }
        }
        throw Exception("图片上传失败")
    }

    @Throws(IOException::class)
    fun imgType(inputStream: InputStream): String {
        // 读取文件前几位
        val fileHeader = ByteArray(4)
        val read = inputStream.read(fileHeader, 0, fileHeader.size)
        inputStream.close()


        // 转为十六进制字符串
        val header: String = bytes2Hex(fileHeader)

        return if (header.contains("FFD8FF")) {
            "jpg"
        } else if (header.contains("89504E47")) {
            "png"
        } else if (header.contains("47494638")) {
            "gif"
        } else if (header.contains("424D")) {
            "bmp"
        } else if (header.contains("52494646")) {
            "webp"
        } else if (header.contains("49492A00")) {
            "tif"
        } else {
            "unknown"
        }
    }

    fun bytes2Hex(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in bytes) {
            val hex = Integer.toHexString(b.toInt() and 0xff)
            sb.append(if (hex.length == 2) hex else ("0$hex"))
        }
        return sb.toString()
    }
}