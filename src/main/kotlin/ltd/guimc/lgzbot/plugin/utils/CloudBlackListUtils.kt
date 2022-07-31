package ltd.guimc.lgzbot.plugin.utils

import ltd.guimc.lgzbot.plugin.PluginMain.logger
import ltd.guimc.lgzbot.plugin.utils.ImageUtils.url2imageMessage
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.PlainText
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object CloudBlackListUtils {
    fun check(target: Long): Int {
        // https://blacklist.baoziwl.com/query.php?qq=346041321
        val url = "https://blacklist.baoziwl.com/query.php?qq=$target"

        if (HttpUtils.getResponse(url).indexOf("3秒后跳转到包子云黑官网") == -1) {
            return 1
        }
        // TODO: Other sites
        return 0
    }

    fun baoziIsOld(doc: Document): Boolean {
        return try {
            val oldTips = doc.select("body > div > div > h2 > a")
            oldTips.text().contains("此人云黑记录存于老版云黑,点此进入查看")
        } catch (_: Exception) {
            false
        }
    }

    suspend fun baoziGetReason(target: Long, bot: Bot, subject: Contact): MessageChain {
        val url = "https://blacklist.baoziwl.com/query.php?qq=$target"
        val doc = Jsoup.connect(url)
            .userAgent("Mozilla")
            .timeout(10000)
            .get()
        val reason = MessageChainBuilder()
        if (!baoziIsOld(doc)) {
            val contect = doc.select("body > div > center")[0]
            val allP = contect.select("p")
            val allH1 = contect.select("h1")
            for (i in allP) {
                try {
                    reason.add(url2imageMessage(i.select("img").first()!!.attr("src"), bot, subject))
                    logger.info("添加Image")
                } catch (_: Exception) {
                    reason.add(PlainText(i.text() + "\n"))
                    logger.info("添加Text")
                }
            }
            for (i in allH1) {
                try {
                    reason.add(url2imageMessage(i.select("img").first()!!.attr("src"), bot, subject))
                    logger.info("添加Image")
                } catch (_: Exception) {
                    reason.add(PlainText(i.text() + "\n"))
                    logger.info("添加Text")
                }
            }
            reason.add(PlainText("\n\n详情请访问 https://blacklist.baoziwl.com/query.php?qq=$target"))
        } else {
            // https://blacklist.baoziwl.com/oldquery.php?qq=346041321
            val url2 = "https://blacklist.baoziwl.com/oldquery.php?qq=$target"
            val doc2 = Jsoup.connect(url2)
                .userAgent("Mozilla")
                .timeout(10000)
                .get()

            val contect = doc2.select("body > div > center")[0]
            val allP = contect.select("p")
            val allH1 = contect.select("h1")
            for (i in allP) {
                try {
                    reason.add(url2imageMessage(i.select("img").first()!!.attr("src"), bot, subject))
                    logger.info("添加Image")
                } catch (_: Exception) {
                    reason.add(PlainText(i.text() + "\n"))
                    logger.info("添加Text")
                }
            }
            for (i in allH1) {
                try {
                    reason.add(url2imageMessage(i.select("img").first()!!.attr("src"), bot, subject))
                    logger.info("添加Image")
                } catch (_: Exception) {
                    reason.add(PlainText(i.text() + "\n"))
                    logger.info("添加Text")
                }
            }
            reason.add(PlainText("\n\n详情请访问 https://blacklist.baoziwl.com/oldquery.php?qq=$target"))
        }
        return reason.build()
    }
}