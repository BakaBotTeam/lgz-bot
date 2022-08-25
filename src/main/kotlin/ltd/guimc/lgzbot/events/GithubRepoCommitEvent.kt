package ltd.guimc.lgzbot.events

import ltd.guimc.lgzbot.github.CommitInfo
import net.mamoe.mirai.event.AbstractEvent

class GithubRepoCommitEvent(repo: String, commitInfo: CommitInfo) : AbstractEvent()