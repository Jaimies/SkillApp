package com.jdevs.timeo.data.projects

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.jdevs.timeo.data.db.BaseDao

@Dao
interface ProjectsDao : BaseDao<DBProject> {

    @Query("SELECT * FROM projects ORDER BY id DESC")
    fun getProjects(): DataSource.Factory<Int, DBProject>

    @Query("SELECT * FROM projects WHERE id = :id")
    fun getProjectById(id: Int): LiveData<DBProject>
}
