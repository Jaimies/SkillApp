@file:Suppress("UnstableApiUsage")

package com.jdevs.timeo.lintchecks

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

class IssueRegistry : IssueRegistry() {
    override val issues: List<Issue>
        get() = listOf(MissingEmptyLinesDetector.ISSUE)

    override val api: Int = CURRENT_API
}
