package ltd.guimc.lgzbot.plugin.command

import ltd.guimc.lgzbot.plugin.PluginMain
import ltd.guimc.lgzbot.plugin.PluginMain.logger
import ltd.guimc.lgzbot.plugin.utils.HttpUtils.getJson
import ltd.guimc.lgzbot.plugin.utils.HttpUtils.getResponse
import ltd.guimc.lgzbot.plugin.utils.RegexUtils
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.isConsole
import net.mamoe.mirai.message.data.MusicKind
import net.mamoe.mirai.message.data.MusicShare
import net.mamoe.mirai.message.data.QuoteReply
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
            val url = "http://music.163.com/api/search/get/web?csrf_token=hlpretag=&hlposttag=&s=${
                URLEncoder.encode(
                    name.replace(
                        "+",
                        " "
                    ), "utf-8"
                )
            }&type=1&offset=0&total=true&limit=20"
            // Get json
            val json = getJson(url)
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
}