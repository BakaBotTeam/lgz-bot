package ltd.guimc.lgzbot.github

import java.time.LocalDateTime

// TODO: Add some features
class CommitInfo(val commitId: String, val commitMessage: String, val author: UserInfo, val commitTime: LocalDateTime, val verified: Boolean) {
    override fun equals(other: Any?): Boolean {
        if (other !is CommitInfo) return false
        return other.commitId == this.commitId
    }
}