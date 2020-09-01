package com.jdevs.timeo.data.projects

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.jdevs.timeo.data.db.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectsDao : BaseDao<DBProject> {

    @Query("SELECT * FROM projects ORDER BY totalTime DESC")
    fun getProjects(): DataSource.Factory<Int, DBProject>

    @Query("SELECT * FROM projects ORDER BY totalTime DESC LIMIT 3")
    fun getTopProjects(): Flow<List<DBProject>>

    @Query("SELECT * FROM projects WHERE id = :id")
    fun getProjectById(id: Int): Flow<DBProject>
}
