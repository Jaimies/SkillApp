package com.jdevs.timeo.data.source.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jdevs.timeo.data.Activity

@Dao
interface ActivitiesDao {

    @Query("SELECT * FROM activities ORDER BY id DESC")
    fun getActivities(): DataSource.Factory<Int, Activity>

    @Insert
    suspend fun insert(activity: Activity)

    @Delete
    suspend fun delete(activity: Activity)

    @Update
    suspend fun update(activity: Activity)

    @Query("UPDATE activities SET totalTime = totalTime + :by WHERE id = :id")
    suspend fun increaseTime(id: Int, by: Long)

    @Query("DELETE FROM activities")
    suspend fun deleteAll()
}
