package com.jdevs.timeo.lintchecks

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API

@Suppress("UnstableApiUsage")
class IssueRegistry : IssueRegistry() {
    override val issues
        get() = listOf(MissingEmptyLinesDetector.ISSUE)

    override val api = CURRENT_API
}
