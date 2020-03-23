package com.jdevs.timeo.data.tasks

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.jdevs.timeo.data.firestore.FirestoreListDataSource
import com.jdevs.timeo.data.firestore.createCollectionWatcher
import com.jdevs.timeo.data.firestore.watch
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.model.Task
import com.jdevs.timeo.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

interface TasksRemoteDataSource : TasksDataSource {

    fun getTasks(fetchNewItems: Boolean): List<LiveData<Operation<Task>>>
}

@Singleton
class FirestoreTasksDataSource @Inject constructor(authRepository: AuthRepository) :
    FirestoreListDataSource(authRepository),
    TasksRemoteDataSource {

    private val tasksWatcher =
        createCollectionWatcher(PAGE_SIZE, FirestoreTask::mapToDomain, "name")
    private var tasksRef by SafeAccess<CollectionReference>()

    override fun getTasks(fetchNewItems: Boolean) =
        tasksWatcher.safeAccess().getLiveDataList(fetchNewItems)

    override fun getTopTasks() =
        tasksRef.safeAccess().orderBy("name").limit(TOP_TASKS_COUNT)
            .watch(FirestoreTask::mapToDomain)

    override suspend fun addTask(name: String, projectId: String) {

        tasksRef.add(FirestoreTask(name = name, projectId = projectId))
    }

    override suspend fun deleteTask(task: Task) {

        tasksRef.document(task.id).delete()
    }

    override suspend fun setTaskCompleted(taskId: String, isCompleted: Boolean) {

        tasksRef.document(taskId).set(hashMapOf(IS_COMPLETED to isCompleted), SetOptions.merge())
    }

    override fun resetRefs(uid: String) {

        tasksRef = createRef(uid, TASKS_COLLECTION, tasksWatcher)
    }

    companion object {

        private const val TASKS_COLLECTION = "tasks"
        private const val IS_COMPLETED = "isCompleted"
        private const val PAGE_SIZE = 20L
        private const val TOP_TASKS_COUNT = 3L
    }
}
