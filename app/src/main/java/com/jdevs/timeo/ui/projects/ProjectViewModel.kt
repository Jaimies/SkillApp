package com.jdevs.timeo.ui.projects

import com.jdevs.timeo.model.ProjectItem
import com.jdevs.timeo.ui.common.recordable.RecordableViewModel
import com.jdevs.timeo.util.time.getFriendlyHours

class ProjectState(project: ProjectItem) {
    val name = project.name
    val totalTime = getFriendlyHours(project.totalTime)
}

class ProjectViewModel : RecordableViewModel<ProjectState, ProjectItem>() {

    override fun createState(item: ProjectItem) = ProjectState(item)
}
