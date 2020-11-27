package com.maxpoliakov.skillapp.data.activities

import androidx.room.Dao
import androidx.room.Query
import com.maxpoliakov.skillapp.data.db.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivitiesDao : BaseDao<DBActivity> {
    @Query("SELECT * FROM activities ORDER BY totalTime DESC")
    fun getActivities(): Flow<List<DBActivity>>

    @Query("SELECT * FROM activities WHERE id <> :activityId ORDER BY totalTime DESC")
    fun getParentActivitySuggestions(activityId: Int): Flow<List<DBActivity>>

    @Query(
        """SELECT activities.*, SUM(records.time) as lastWeekTime FROM activities
        LEFT JOIN records ON activityId = activities.id
        AND date(records.timestamp, 'localtime') > date('now','localtime', '-6 day')
        AND date(records.timestamp, 'localtime') <= date('now', 'localtime')
        WHERE activities.id = :id"""
    )
    fun getActivity(id: Int): Flow<DBActivity>

    @Query("UPDATE activities SET totalTime = totalTime + :by WHERE id = :id")
    suspend fun increaseTime(id: Int, by: Long)
}
