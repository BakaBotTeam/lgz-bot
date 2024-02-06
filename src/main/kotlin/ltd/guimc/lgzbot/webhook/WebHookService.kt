package ltd.guimc.lgzbot.webhook

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ltd.guimc.lgzbot.PluginMain
import ltd.guimc.lgzbot.files.Config
import org.json.JSONObject

class WebHookService(githubWebHookReciver: GithubWebHookReciver) {
    private var server: ApplicationEngine
    private val errMsg = "\u50BB\u903C\u50BB\u903C\u64CD\u4F60\u5988\uFF0C\u4F60\u5988\u5927\u903C\u4EBA\u4EBA" +
        "\u63D2\uFF0C\u5DE6\u63D2\u63D2\u6211\u53F3\u63D2\u63D2\uFF0C\u63D2\u7684\u4F60\u5988\u903C\u5F00\u82B1"

    init {
        server = embeddedServer(Netty, port = Config.githubWebhookPort) {
            routing {
                post("/") {
                    val headers = this.context.request.headers
                    if (headers["Content-Type"] != "application/json") {
                        PluginMain.logger.error("Content-Type must be \"application/json\"")
                        return@post call.respondText(errMsg, status = HttpStatusCode.BadGateway)
                    }
                    if (headers["User-Agent"] == null || !headers["User-Agent"]!!.startsWith("GitHub-Hookshot/")) {
                        PluginMain.logger.warning("Received wrong user-agent")
                        return@post call.respondText(errMsg, status = HttpStatusCode.BadGateway)
                    }

                    val payload = withContext(Dispatchers.IO) {
                        call.receiveStream().bufferedReader(charset = Charsets.UTF_8).readText()
                    }

                    if (!githubWebHookReciver.validate(payload, Config.githubWebhookSecret, this.context.request)) {
                        PluginMain.logger.warning("Failed to validate request!")
                    }

                    val jsonObject = JSONObject(payload)

                    when(headers["X-GitHub-Event"]) {
                        "push" -> {
                            githubWebHookReciver.onPush(jsonObject)
                        }
                    }

                    return@post call.respondText("Accepted", status = HttpStatusCode.Accepted)
                }
            }
        }
    }

    fun start() {
        server.start(wait = false)
    }

    fun stop() {
        server.stop()
    }
}
