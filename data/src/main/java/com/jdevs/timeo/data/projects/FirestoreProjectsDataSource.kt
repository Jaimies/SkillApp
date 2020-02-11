package com.jdevs.timeo.data.projects

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.CollectionReference
import com.jdevs.timeo.data.PROJECTS_COLLECTION
import com.jdevs.timeo.data.firestore.FirestoreListDataSource
import com.jdevs.timeo.data.firestore.createCollectionWatcher
import com.jdevs.timeo.data.firestore.watch
import com.jdevs.timeo.data.firestore.watchCollection
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

interface ProjectsRemoteDataSource : ProjectsDataSource {

    val projects: List<LiveData<Operation<Project>>>
}

@Singleton
class FirestoreProjectsDataSource @Inject constructor(authRepository: AuthRepository) :
    FirestoreListDataSource(authRepository),
    ProjectsRemoteDataSource {

    private val projectsWatcher = createCollectionWatcher(PAGE_SIZE, FirestoreProject::mapToDomain)

    override val projects get() = projectsWatcher.safeAccess().getLiveDataList()

    private var projectsRef by SafeAccess<CollectionReference>()

    override fun getTopProjects() =
        projectsRef.watchCollection(FirestoreProject::mapToDomain, TOP_PROJECTS_COUNT)

    override fun getProjectById(id: String) =
        projectsRef.document(id).watch(FirestoreProject::mapToDomain)

    override suspend fun addProject(name: String, description: String) {

        projectsRef.add(FirestoreProject(name = name, description = description))
    }

    override suspend fun saveProject(project: Project) {

        projectsRef.document(project.id).set(project.mapToFirestore())
    }

    override suspend fun deleteProject(project: Project) {

        projectsRef.document(project.id).delete()
    }

    override fun resetRefs(uid: String) {

        projectsRef = createRef(uid, PROJECTS_COLLECTION, projectsWatcher)
    }

    companion object {
        private const val PAGE_SIZE = 20L
        private const val TOP_PROJECTS_COUNT = 3L
    }
}
