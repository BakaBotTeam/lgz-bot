package ltd.guimc.lgzbot.command

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ltd.guimc.lgzbot.PluginMain
import ltd.guimc.lgzbot.PluginMain.logger
import ltd.guimc.lgzbot.utils.Base64Utils
import ltd.guimc.lgzbot.utils.HttpUtils.getJsonObject
import ltd.guimc.lgzbot.utils.HttpUtils.getResponse
import ltd.guimc.lgzbot.utils.RegexUtils
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.isConsole
import net.mamoe.mirai.message.data.MusicKind
import net.mamoe.mirai.message.data.MusicShare
import net.mamoe.mirai.message.data.PlainText
import org.json.JSONObject
import java.net.URLEncoder

object MusicCommand: CompositeCommand(
    owner = PluginMain,
    primaryName = "music",
    description = "音乐助手"
) {
    @SubCommand("点歌", "dg")
    @Description("点首歌听吧~ (空格用加号代替)")
    suspend fun CommandSender.lgzbot_music_dg(name: String) {
        try {
            if (isConsole()) {
                logger.warning("请在群里使用")
                return
            }
            
            val url = "http://cloud-music.pl-fe.cn/search?keywords=${
                withContext(Dispatchers.IO) {
                    URLEncoder.encode(name.replace("+", " "), "utf-8")
                }
            }"
            // Get json
            val json = getJsonObject(url)
            // Get song id
            val song = json.getJSONObject("result").getJSONArray("songs").getJSONObject(0)
            val id = song.getLong("id")
            val songName = song.getString("name")
            val artist = song.getJSONArray("artists").getJSONObject(0).getString("name")
            // Get song image
            val image = RegexUtils.matchRegex(
                """"og:image" content="(.*)" />""",
                getResponse("https://music.163.com/song?id=$id")
            )!!.split("\"")[3]
            sendMessage(
                MusicShare(
                    MusicKind.NeteaseCloudMusic,
                    songName,
                    artist,
                    "http://music.163.com/song/$id/",
                    image,
                    "http://music.163.com/song/media/outer/url?id=$id",
                    "分享音乐"
                )
            )
        } catch (e: Exception) {
            logger.error(e)
            sendMessage("没有找到哦")
        }
    }

    @SubCommand("转换", "zh")
    @Description("转换音乐分享")
    suspend fun CommandSender.lgzbot_music_zh(name: String) {
        try {
            if (isConsole()) {
                logger.warning("请在聊天环境下使用")
                return
            }
            val url = "http://cloud-music.pl-fe.cn/search?keywords=${
                withContext(Dispatchers.IO) {
                    URLEncoder.encode(name.replace("+", " "), "utf-8")
                }
            }"
            // Get json
            val json = getJsonObject(url)
            // Get song id
            val song = json.getJSONObject("result").getJSONArray("songs").getJSONObject(0)
            val id = song.getLong("id")
            val songName = song.getString("name")
            val artist = song.getJSONArray("artists").getJSONObject(0).getString("name")
            // Get song image
            val image = RegexUtils.matchRegex(
                """"og:image" content="(.*)" />""",
                getResponse("https://music.163.com/song?id=$id")
            )!!.split("\"")[3]
            val musics = MusicShare(
                MusicKind.NeteaseCloudMusic,
                songName,
                artist,
                "http://music.163.com/song/$id/",
                image,
                "http://music.163.com/song/media/outer/url?id=$id",
                "分享音乐"
            )
            val musictype = when (musics.kind) {
                MusicKind.NeteaseCloudMusic -> "netease"
                MusicKind.QQMusic -> "qq"
                MusicKind.KugouMusic -> "kugou"
                MusicKind.KuwoMusic -> "kuwo"
                else -> throw Exception("不支持的音乐类型")
            }
            val shortjson = JSONObject()
                .put("type", musictype)
                .put("title", musics.title)
                .put("summary", musics.summary)
                .put("jumpUrl", musics.jumpUrl)
                .put("pictureUrl", musics.pictureUrl)
                .put("musicUrl", musics.musicUrl)
                .toString()
            sendMessage(PlainText("/music s ${Base64Utils.encode(shortjson.toByteArray())}"))
        } catch (e: Exception) {
            logger.error(e)
            sendMessage("没有找到哦")
        }
    }

    @SubCommand("s")
    @Description("转换音乐分享")
    suspend fun CommandSender.lgzbot_music_s(name: String) {
        try {
            if (isConsole()) {
                logger.warning("请在聊天环境下使用")
                return
            }
            logger.info(Base64Utils.decode(name).toString(Charsets.UTF_8))
            val json = JSONObject(Base64Utils.decode(name).toString(Charsets.UTF_8))
            val musictype = when (json.getString("type")) {
                "netease" -> MusicKind.NeteaseCloudMusic
                "qq" -> MusicKind.QQMusic
                "kugou" -> MusicKind.KugouMusic
                "kuwo" -> MusicKind.KuwoMusic
                else -> throw Exception("不支持的音乐类型")
            }
            val musics = MusicShare(
                musictype,
                json.getString("title"),
                json.getString("summary"),
                json.getString("jumpUrl"),
                json.getString("pictureUrl"),
                json.getString("musicUrl"),
                "分享音乐"
            )
            sendMessage(musics)
        } catch (e: Exception) {
            logger.error(e)
            sendMessage("出错惹!")
        }
    }
}
