package com.jdevs.timeo.data.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.jdevs.timeo.data.db.model.DBProject

@Dao
interface ProjectsDao : BaseDao<DBProject> {

    @Query("SELECT * FROM projects ORDER BY id DESC")
    fun getProjects(): DataSource.Factory<Int, DBProject>
}
