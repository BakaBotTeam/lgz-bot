/*
 * THIS FILE IS PART OF lgz-bot PROJECT
 *
 * You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.
 * Your modified application must also be licensed under the AGPLv3.
 *
 * Copyright (c) 2022 - now Guimc Team.
 */

package ltd.guimc.lgzbot.github

import java.time.LocalDateTime

// TODO: Add some features
class CommitInfo(
    val commitId: String,
    val commitMessage: String,
    val author: UserInfo,
    val commitTime: LocalDateTime,
    val verified: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (other !is CommitInfo) return false
        return other.commitId == this.commitId
    }
}