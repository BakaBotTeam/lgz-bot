package ltd.guimc.lgzbot.plugin.utils

import org.kohsuke.github.GHBranch
import org.kohsuke.github.GHCommit
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GitHub


object GithubUtils {
    val ghClient = GitHub.connectAnonymously()!!

    fun getRepo(repo: String): GHRepository {
        return ghClient.getRepository(repo) ?: throw IllegalArgumentException("Repository $repo does not exist")
    }

    fun GHRepository.getLatestCommit(): GHCommit {
        return this.listCommits().first()
    }
}