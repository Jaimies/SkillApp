package com.jdevs.timeo.data.projects

import com.google.firebase.firestore.CollectionReference
import com.jdevs.timeo.data.auth.AuthRepository
import com.jdevs.timeo.data.firestore.FirestoreDataSource
import com.jdevs.timeo.data.firestore.createCollectionMonitor
import com.jdevs.timeo.data.firestore.model.FirestoreProject
import com.jdevs.timeo.model.Project
import com.jdevs.timeo.util.ProjectsConstants
import javax.inject.Inject
import javax.inject.Singleton

interface ProjectsRemoteDataSource : ProjectsDataSource

@Singleton
class FirestoreProjectsDataSource @Inject constructor(
    authRepository: AuthRepository
) : FirestoreDataSource(authRepository), ProjectsRemoteDataSource {

    private val projectsMonitor =
        createCollectionMonitor(FirestoreProject::class, ProjectsConstants.PAGE_SIZE)

    override val projects
        get() = projectsMonitor.safeAccess().getLiveData()

    private var projectsRef by SafeAccess<CollectionReference>()

    override suspend fun addProject(project: Project) {

        projectsRef.add(project.toFirestore())
    }

    override suspend fun saveProject(project: Project) {

        projectsRef.document(project.documentId).set(project.toFirestore())
    }

    override suspend fun deleteProject(project: Project) {

        projectsRef.document(project.documentId).delete()
    }

    override fun resetRefs(uid: String) {

        projectsRef = createRef(uid, ProjectsConstants.COLLECTION, projectsMonitor)
    }
}
