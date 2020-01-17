package com.jdevs.timeo.data.projects

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.jdevs.timeo.data.firestore.FirestoreListDataSource
import com.jdevs.timeo.data.firestore.createCollectionMonitor
import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.util.FirestoreConstants.TOTAL_TIME
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
        createCollectionMonitor(FirestoreProject::class, FIRESTORE_PROJECTS_PAGE_SIZE, domainMapper)

    override val projects
        get() = projectsMonitor.safeAccess().getLiveData()

    private var projectsRef by SafeAccess<CollectionReference>()

    override fun getTopProjects(): LiveData<List<Project>> {

        val liveData = MutableLiveData<List<Project>>()

        projectsRef.orderBy(TOTAL_TIME, Query.Direction.DESCENDING).limit(TOP_PROJECTS_COUNT)
            .addSnapshotListener { querySnapshot, _ ->

                querySnapshot?.documents?.mapNotNull {
                    it.toObject(FirestoreProject::class.java)
                }?.let { liveData.value = it.map(domainMapper::map) }
            }

        return liveData
    }

    override fun getProjectById(id: Int, documentId: String): LiveData<Project> {

        val project = projectsRef.document(documentId)
        val liveData = MutableLiveData<Project>()

        project.addSnapshotListener { documentSnapshot, _ ->

            documentSnapshot?.toObject(FirestoreProject::class.java)?.let {
                liveData.value = domainMapper.map(it)
            }
        }

        return liveData
    }

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
