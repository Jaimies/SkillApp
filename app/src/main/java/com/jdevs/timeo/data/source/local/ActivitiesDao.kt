package com.jdevs.timeo.data.source.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.jdevs.timeo.data.Activity

@Dao
interface ActivitiesDao : BaseDao<Activity> {

    @Query("SELECT * FROM activities ORDER BY id DESC")
    fun getActivities(): DataSource.Factory<Int, Activity>

    @Query("UPDATE activities SET totalTime = totalTime + :by WHERE id = :id")
    suspend fun increaseTime(id: Int, by: Long)

    @Query("DELETE FROM activities")
    suspend fun deleteAll()
}
