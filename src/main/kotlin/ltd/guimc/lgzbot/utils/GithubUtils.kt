package ltd.guimc.lgzbot.utils

import ltd.guimc.lgzbot.github.CommitInfo
import ltd.guimc.lgzbot.github.RepoInfo
import ltd.guimc.lgzbot.github.OwnerInfo
import ltd.guimc.lgzbot.github.UserInfo
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime

object GithubUtils {
    val gitLinkRegex = Regex("(git|https|git@)(:\\/\\/|)github.com(:|\\/).*\\/.*(.git|\\/| |)")
    val githubHead = Regex("(git|https|git@)(:\\/\\/|)github.com(:|\\/)")

    fun findGitLink(text: String): String? {
        return gitLinkRegex.find(text)?.value
    }

    fun apiObject(url: String): JSONObject = HttpUtils.getJsonObject("https://api.github.com$url")

    fun apiArray(url: String): JSONArray = HttpUtils.getJsonArray(url)

    fun convert(url: String): String {
        val s = url
        var dropLength = 0
        if (s.endsWith("/")) dropLength++
        if (s.endsWith(".git")) dropLength += 4
        if (s.endsWith(" ")) dropLength++
        return s.dropLast(dropLength).replace(githubHead, "")
    }

    fun getGithubRepo(repo: String): RepoInfo {
        val infoJson = apiObject("/repo/$repo")
        return RepoInfo(
            repo,
            OwnerInfo(infoJson.getJSONObject("owner").getString("login")),
            infoJson.getString("description"),
            LocalDateTime.parse(infoJson.getString("created_at")),
            LocalDateTime.parse(infoJson.getString("pushed_at")),
            getLastCommit(repo),
            infoJson.getString("language"),
            infoJson.getString("default_branch")
        )
    }

    fun getLastCommit(repo: String): CommitInfo {
        val infoJson = apiArray("/repo/$repo/commits").getJSONObject(0)
        val author = infoJson.getJSONObject("committer")
        val commit = infoJson.getJSONObject("commit")
        val commitID = infoJson.getString("sha").dropLast(8)

        // Commit Info
        val verification = try {
            commit.getJSONObject("verification").getBoolean("verified")
        } catch (_: Exception) {
            false
        }
        val commitMessage = commit.getString("message")
        val commitTime = LocalDateTime.parse(commit.getJSONObject("committer").getString("date"))

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