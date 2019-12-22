package com.jdevs.timeo.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jdevs.timeo.data.Activity

@Dao
interface ActivityDao {

    @Query("SELECT * FROM activities ORDER BY id DESC")
    fun getActivities(): LiveData<List<Activity>>

    @Insert
    suspend fun insert(activity: Activity)

    @Query("DELETE FROM activities")
    suspend fun deleteAll()
}
