package ltd.guimc.lgzbot.webhook

import io.ktor.server.request.*
import ltd.guimc.lgzbot.files.GithubWebhookSubData
import ltd.guimc.lgzbot.utils.RegexUtils
import ltd.guimc.lgzbot.utils.webhook.SecurityUtils.calcSignature
import net.mamoe.mirai.Bot
import net.mamoe.mirai.Mirai
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.console.util.ContactUtils.getFriendOrGroup
import net.mamoe.mirai.contact.BotIsBeingMutedException
import org.json.JSONArray
import org.json.JSONObject

class GithubWebHookReciver {
    @OptIn(ExperimentalStdlibApi::class)
    fun validate(payload: String, secret: String, request: ApplicationRequest): Boolean {
        val signature = request.headers["X-Hub-Signature-256"]
        return "sha256=" + calcSignature(payload, secret)?.toHexString() == signature
    }

    suspend fun onPush(event: JSONObject) {
        val repo = event.getJSONObject("repository").getString("full_name")
        event.getJSONArray("commits").forEach { rawcommit ->
            val commit = rawcommit as JSONObject
            val author = commit.getJSONObject("author")
            val ref = event.getString("ref")
            val addedLength = try { commit.getJSONArray("added").length() } catch (_: Throwable) { 0 }
            val removedLength = try { commit.getJSONArray("removed").length() } catch (_: Throwable) { 0 }
            val modifiedLength = try { commit.getJSONArray("modified").length() } catch (_: Throwable) { 0 }

            if (ref.startsWith("refs/tags")) return@forEach // Filter Releases

            GithubWebhookSubData.sub.keys.forEach { botid ->
                try {
                    val bot = Bot.getInstance(botid)
                    GithubWebhookSubData.sub[botid]!![repo]?.forEach {
                        try {
                            bot.getGroup(it)!!.sendMessage(
                                """[GitHub WebHook]
                            || New Commit to repo $repo
                            || Author: ${if (GithubWebhookSubData.ignore[bot.id]?.get(repo)?.indexOf(author.getString("username")) == -1) "${author.getString("name")} (${author.getString("email")})" else "<Ignored Author>"}
                            || Branch: ${RegexUtils.checkRisk(ref)}
                            || ++$addedLength --$removedLength **$modifiedLength
                            || Commit Message: ${RegexUtils.checkRisk(commit.getString("message"))}
                            || Details: ${commit.getString("url")}""".trimMargin()
                            )
                        } catch (_: Throwable) {
                        }
                    }
                } catch (_: Throwable) {}
            }
        }
    }
}
