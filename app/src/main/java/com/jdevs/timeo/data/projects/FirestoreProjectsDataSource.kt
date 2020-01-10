package com.jdevs.timeo.data.projects

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.jdevs.timeo.data.auth.AuthRepository
import com.jdevs.timeo.data.firestore.FirestoreDataSource
import com.jdevs.timeo.data.firestore.createCollectionMonitor
import com.jdevs.timeo.model.Project
import com.jdevs.timeo.util.FirestoreConstants.TOTAL_TIME
import com.jdevs.timeo.util.ProjectsConstants
import com.jdevs.timeo.util.ProjectsConstants.TOP_PROJECTS_COUNT
import javax.inject.Inject
import javax.inject.Singleton

interface ProjectsRemoteDataSource : ProjectsDataSource {

    fun resetMonitor()
}

@Singleton
class FirestoreProjectsDataSource @Inject constructor(authRepository: AuthRepository) :
    FirestoreDataSource(authRepository), ProjectsRemoteDataSource {

    private val projectsMonitor =
        createCollectionMonitor(FirestoreProject::class, ProjectsConstants.PAGE_SIZE)

    override val projects
        get() = projectsMonitor.safeAccess().getLiveData()

    private var projectsRef by SafeAccess<CollectionReference>()

    override fun getTopProjects(): LiveData<List<Project>> {

        val liveData = MutableLiveData<List<Project>>()

        projectsRef.orderBy(TOTAL_TIME, Query.Direction.DESCENDING).limit(TOP_PROJECTS_COUNT)
            .addSnapshotListener { querySnapshot, _ ->

                querySnapshot?.run {

                    val newList = documentChanges.map {

                        it.document.toObject(FirestoreProject::class.java).mapToDomain()
                    }

                    liveData.value = newList
                }
            }

        return liveData
    }

    override fun getProjectById(id: Int, documentId: String): LiveData<Project> {

        val project = projectsRef.document(documentId)
        val liveData = MutableLiveData<Project>()

        project.addSnapshotListener { documentSnapshot, _ ->

            if (documentSnapshot != null) {

                liveData.value =
                    documentSnapshot.toObject(FirestoreProject::class.java)?.run { mapToDomain() }
            }
        }

        return liveData
    }

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

    override fun resetMonitor() = projectsMonitor.reset()
}
