package com.jdevs.timeo.data.projects

import com.google.firebase.firestore.CollectionReference
import com.jdevs.timeo.data.firestore.FirestoreListDataSource
import com.jdevs.timeo.data.firestore.createCollectionMonitor
import com.jdevs.timeo.data.firestore.monitor
import com.jdevs.timeo.data.firestore.monitorCollection
import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.util.ProjectsConstants
import com.jdevs.timeo.util.ProjectsConstants.FIRESTORE_PROJECTS_PAGE_SIZE
import com.jdevs.timeo.util.ProjectsConstants.TOP_PROJECTS_COUNT
import javax.inject.Inject
import javax.inject.Singleton

interface ProjectsRemoteDataSource : ProjectsDataSource {

    fun resetMonitor()
}

@Singleton
class FirestoreProjectsDataSource @Inject constructor(
    authRepository: AuthRepository,
    private val mapper: FirestoreProjectMapper,
    private val domainMapper: FirestoreDomainProjectMapper
) :
    FirestoreListDataSource(authRepository),
    ProjectsRemoteDataSource {

    private val projectsMonitor =
        createCollectionMonitor(FirestoreProject::class, domainMapper, FIRESTORE_PROJECTS_PAGE_SIZE)

    override val projects
        get() = projectsMonitor.safeAccess().getLiveData()

    private var projectsRef by SafeAccess<CollectionReference>()

    override fun getTopProjects() =
        projectsRef.monitorCollection(FirestoreProject::class, domainMapper, TOP_PROJECTS_COUNT)

    override fun getProjectById(id: Int, documentId: String) =
        projectsRef.document(documentId).monitor(FirestoreProject::class, domainMapper)

    override suspend fun addProject(project: Project) {

        projectsRef.add(mapper.map(project))
    }

    override suspend fun saveProject(project: Project) {

        projectsRef.document(project.documentId).set(mapper.map(project))
    }

    override suspend fun deleteProject(project: Project) {

        projectsRef.document(project.documentId).delete()
    }

    override fun resetRefs(uid: String) {

        projectsRef = createRef(uid, ProjectsConstants.COLLECTION, projectsMonitor)
    }

    override fun resetMonitor() = projectsMonitor.reset()
}
