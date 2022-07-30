package ltd.guimc.lgzbot.plugin.utils

import ltd.guimc.lgzbot.plugin.files.Data.repoListenList
import ltd.guimc.lgzbot.plugin.schedule.GithubSchedule
import net.mamoe.mirai.contact.Group
import org.kohsuke.github.GHBranch
import org.kohsuke.github.GHCommit
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GitHub


object GithubUtils {
    val ghClient = GitHub.connectAnonymously()!!

    fun getRepo(repo: String): GHRepository {
        return ghClient.getRepository(repo) ?: throw IllegalArgumentException("Repository $repo does not exist")
    }

    fun addRepo(repo: String, group: Group) {
        if (getRepo(repo) == null) return
        if (repoListenList.keys.contains(repo)) {
            repoListenList[repo]!!.add(group.id)
        } else {
            repoListenList[repo] = mutableListOf(group.id)
        }
    }

    fun removeRepo(repo: String, group: Group) {
        val repo2 = getRepo(repo)
        if (repoListenList.keys.contains(repo)) {
            repoListenList[repo]!!.remove(group.id)
        } else {
            throw IllegalArgumentException("Repository $repo does not exist")
        }
    }

    fun GHRepository.getLatestCommit(): GHCommit {
        return this.listCommits().first()
    }
}