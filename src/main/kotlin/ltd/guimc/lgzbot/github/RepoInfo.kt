/*
 * THIS FILE IS PART OF lgz-bot PROJECT
 *
 * You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.
 * Your modified application must also be licensed under the AGPLv3.
 *
 * Copyright (c) 2022 - now Guimc Team.
 */

package ltd.guimc.lgzbot.github

import ltd.guimc.lgzbot.utils.GithubUtils
import java.time.LocalDateTime


// TODO: Add some features
class RepoInfo(
    val repo: String,
    val author: OwnerInfo,
    val descriptor: String,
    val createdTime: LocalDateTime,
    val updateTime: LocalDateTime,
    val lastCommitInfo: CommitInfo,
    val language: String,
    val defaultBranch: String
) {
    var commit: CommitInfo = lastCommitInfo
    override fun equals(other: Any?): Boolean {
        if (other !is RepoInfo) return false
        return other.repo == this.repo
    }

    fun checkUpdate(): Boolean {
        val newCommit = GithubUtils.getLastCommit(repo)
        return if (newCommit.commitId != commit.commitId) {
            commit = newCommit
            true
        } else {
            false
        }
    }
}