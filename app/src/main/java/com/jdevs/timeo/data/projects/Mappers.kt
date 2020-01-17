package com.jdevs.timeo.data.projects

import com.jdevs.timeo.data.Mapper
import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.util.time.toDate
import com.jdevs.timeo.util.time.toOffsetDate
import javax.inject.Inject

class FirestoreProjectMapper @Inject constructor() : Mapper<Project, FirestoreProject> {

    override fun map(input: Project) = input.run {
        FirestoreProject(documentId, name, totalTime, timestamp = creationDate.toDate())
    }
}

class FirestoreDomainProjectMapper @Inject constructor() : Mapper<FirestoreProject, Project> {

    override fun map(input: FirestoreProject) = input.run {
        Project(
            documentId = documentId,
            name = name,
            totalTime = totalTime,
            lastWeekTime = getLastWeekTime(),
            creationDate = timestamp.toOffsetDate()
        )
    }
}


class DBProjectMapper @Inject constructor() : Mapper<Project, DBProject> {

    override fun map(input: Project) = input.run {
        DBProject(id, name, totalTime, lastWeekTime, creationDate)
    }
}

class DBDomainProjectMapper @Inject constructor() : Mapper<DBProject, Project> {

    override fun map(input: DBProject) = input.run {
        Project(
            id,
            name = name,
            totalTime = totalTime,
            lastWeekTime = lastWeekTime,
            creationDate = creationDate
        )
    }
}
