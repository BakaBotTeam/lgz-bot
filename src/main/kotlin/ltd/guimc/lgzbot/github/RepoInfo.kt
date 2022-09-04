package ltd.guimc.lgzbot.github

import ltd.guimc.lgzbot.utils.GithubUtils
import java.time.LocalDateTime


// TODO: Add some features
class RepoInfo(val repo: String, val author: OwnerInfo, val descriptor: String, val createdTime: LocalDateTime, val updateTime: LocalDateTime, val lastCommitInfo: CommitInfo, val language: String, val defaultBranch: String) {
    var commit: CommitInfo? = null
    override fun equals(other: Any?): Boolean {
        if (other !is RepoInfo) return false
        return other.repo == this.repo
    }

    fun checkUpdate(): Boolean {
        val newCommit = GithubUtils.getLastCommit(repo)
        return if (commit == null || newCommit.commitId != commit!!.commitId) {
            commit = newCommit
            true
        } else {
            false
        }
    }
}