package com.jdevs.timeo.data.projects

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.CollectionReference
import com.jdevs.timeo.data.firestore.FirestoreListDataSource
import com.jdevs.timeo.data.firestore.createCollectionWatcher
import com.jdevs.timeo.data.firestore.watch
import com.jdevs.timeo.data.firestore.watchCollection
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.util.ProjectsConstants
import com.jdevs.timeo.util.ProjectsConstants.FIRESTORE_PROJECTS_PAGE_SIZE
import com.jdevs.timeo.util.ProjectsConstants.TOP_PROJECTS_COUNT
import javax.inject.Inject
import javax.inject.Singleton

interface ProjectsRemoteDataSource : ProjectsDataSource {

    val projects: List<LiveData<Operation<Project>>>
}

@Singleton
class FirestoreProjectsDataSource @Inject constructor(authRepository: AuthRepository) :
    FirestoreListDataSource(authRepository),
    ProjectsRemoteDataSource {

    private val projectsWatcher =
        createCollectionWatcher(FIRESTORE_PROJECTS_PAGE_SIZE, FirestoreProject::mapToDomain)

    override val projects
        get() = projectsWatcher.safeAccess().getLiveDataList()

    private var projectsRef by SafeAccess<CollectionReference>()

    override fun getTopProjects() =
        projectsRef.watchCollection(FirestoreProject::mapToDomain, TOP_PROJECTS_COUNT)

    override fun getProjectById(id: Int, documentId: String) =
        projectsRef.document(documentId).watch(FirestoreProject::mapToDomain)

    override suspend fun addProject(name: String) {

        projectsRef.add(FirestoreProject(name = name))
    }

    override suspend fun saveProject(project: Project) {

        projectsRef.document(project.documentId).set(project.mapToFirestore())
    }

    override suspend fun deleteProject(project: Project) {

        projectsRef.document(project.documentId).delete()
    }

    override fun resetRefs(uid: String) {

        projectsRef = createRef(uid, ProjectsConstants.COLLECTION, projectsWatcher)
    }
}
