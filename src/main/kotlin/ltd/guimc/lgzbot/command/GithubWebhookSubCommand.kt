package ltd.guimc.lgzbot.command

import ltd.guimc.lgzbot.PluginMain
import ltd.guimc.lgzbot.files.GithubWebhookSubData
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.contact.Group

object GithubWebhookSubCommand: CompositeCommand(
    owner = PluginMain,
    primaryName = "ghsub",
    description = "Github Webhook Sub"
) {
    @SubCommand("add")
    @Description("Add a new sub")
    suspend fun CommandSender.iI1I1I1i1I1I1iI1i1I1Ii1I1i1I1I(repo: String) {
        requireNotNull(bot)
        require(subject is Group)
        if (GithubWebhookSubData.sub[bot!!.id] == null)
            GithubWebhookSubData.sub[bot!!.id] = mutableMapOf()

        if (GithubWebhookSubData.sub[bot!!.id]!![repo] == null)
            GithubWebhookSubData.sub[bot!!.id]!![repo] = mutableListOf()

        if (GithubWebhookSubData.sub[bot!!.id]!![repo]!!.indexOf(subject!!.id) != -1) {
            sendMessage("You have already added this repo!")
            return
        }

        GithubWebhookSubData.sub[bot!!.id]!![repo]!!.add(subject!!.id)
        sendMessage("OK")
    }

    @SubCommand("remove")
    @Description("Remove a repo from sub")
    suspend fun CommandSender.iI1I1I1I1i1I1Ii1I1i1I1I1I1Ii1I1(repo: String) {
        requireNotNull(bot)
        require(subject is Group)
        if (GithubWebhookSubData.sub[bot!!.id] == null)
            GithubWebhookSubData.sub[bot!!.id] = mutableMapOf()

        if (GithubWebhookSubData.sub[bot!!.id]!![repo] == null)
            GithubWebhookSubData.sub[bot!!.id]!![repo] = mutableListOf()

        if (GithubWebhookSubData.sub[bot!!.id]!![repo]!!.indexOf(subject!!.id) == -1) {
            sendMessage("You have already removed this repo!")
            return
        }

        GithubWebhookSubData.sub[bot!!.id]!![repo]!!.remove(subject!!.id)
        sendMessage("OK")
    }
}