package ltd.guimc.lgzbot.utils

import ltd.guimc.lgzbot.files.GithubSubConfig
import ltd.guimc.lgzbot.github.CommitInfo
import ltd.guimc.lgzbot.github.OwnerInfo
import ltd.guimc.lgzbot.github.RepoInfo
import ltd.guimc.lgzbot.github.UserInfo
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object GithubUtils {
    val gitLinkRegex = Regex("(git|https|git@)(:\\/\\/|)github.com(:|\\/).*\\/.*(.git|\\/| |)")
    val githubHead = Regex("(git|https|git@)(:\\/\\/|)github.com(:|\\/)")

    fun findGitLink(text: String): String? {
        return gitLinkRegex.find(text)?.value
    }

    fun apiObject(url: String): JSONObject = HttpUtils.getJsonObject("https://${if (GithubSubConfig.key != "" ) GithubSubConfig.key else ""}api.github.com$url")

    fun apiArray(url: String): JSONArray = HttpUtils.getJsonArray("https://${if (GithubSubConfig.key != "" ) GithubSubConfig.key else ""}api.github.com$url")

    fun convert(url: String): String {
        val s = url
        var dropLength = 0
        if (s.endsWith("/")) dropLength++
        if (s.endsWith(".git")) dropLength += 4
        if (s.endsWith(" ")) dropLength++
        return s.dropLast(dropLength).replace(githubHead, "")
    }

    fun getGithubRepo(repo: String): RepoInfo {
        val infoJson = apiObject("/repos/$repo")
        return RepoInfo(
            repo,
            OwnerInfo(infoJson.getJSONObject("owner").getString("login")),
            infoJson.getString("description"),
            LocalDateTime.parse(infoJson.getString("created_at"), DateTimeFormatter.ISO_DATE_TIME),
            LocalDateTime.parse(infoJson.getString("pushed_at"), DateTimeFormatter.ISO_DATE_TIME),
            getLastCommit(repo),
            infoJson.getString("language"),
            infoJson.getString("default_branch")
        )
    }

    fun getLastCommit(repo: String): CommitInfo {
        val infoJson = apiArray("/repos/$repo/commits").getJSONObject(0)
        val commit = infoJson.getJSONObject("commit")
        val author = commit.getJSONObject("committer")
        val commitID = infoJson.getString("sha").dropLast(8)

        // Commit Info
        val verification = try {
            commit.getJSONObject("verification").getBoolean("verified")
        } catch (_: Exception) {
            false
        }
        val commitMessage = commit.getString("message")
        val commitTime = LocalDateTime.parse(commit.getJSONObject("committer").getString("date"), DateTimeFormatter.ISO_DATE_TIME)

        return CommitInfo(
            commitID,
            commitMessage,
            UserInfo(
                author.getString("name"),
                author.getString("email")
            ),
            commitTime,
            verification
        )
    }
}