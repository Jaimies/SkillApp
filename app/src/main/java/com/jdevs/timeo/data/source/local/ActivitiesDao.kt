package com.jdevs.timeo.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.jdevs.timeo.data.Activity

@Dao
interface ActivitiesDao : BaseDao<Activity> {

    @Query("SELECT * FROM activities ORDER BY id DESC")
    fun getActivities(): DataSource.Factory<Int, Activity>

    @Query(
        """SELECT activities.*, SUM(records.time) as lastWeekTime FROM activities
        LEFT JOIN records ON activityId = activities.id
        AND DATETIME(records.creationDate) > DATETIME('now', '-7 day')
        WHERE activities.id = :id"""
    )
    fun getActivity(id: Int): LiveData<Activity>

    @Query("UPDATE activities SET totalTime = totalTime + :by WHERE id = :id")
    suspend fun increaseTime(id: Int, by: Long)
}
