package com.jdevs.timeo.data.tasks

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.jdevs.timeo.data.db.BaseDao

@Dao
interface TasksDao : BaseDao<DBTask> {

    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getTasks(): DataSource.Factory<Int, DBTask>

    @Query("SELECT * FROM tasks ORDER BY id DESC LIMIT 3")
    fun getTopTasks(): LiveData<List<DBTask>>
}
