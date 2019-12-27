package com.jdevs.timeo.data.source

import androidx.lifecycle.LiveData
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record
import kotlinx.coroutines.Dispatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test

@Suppress("UNCHECKED_CAST")
class DefaultTimeoRepositoryTest {

    private val activity1 = Activity(name = "Activity 1")
    private val activity2 = Activity(name = "Activity 2")
    private val activity3 = Activity(name = "Activity 3")
    private val record1 = Record(name = "Record 1")
    private val record2 = Record(name = "Record 2")
    private val record3 = Record(name = "Record 3")

    private val localActivities = listOf(activity1, activity2)
    private val remoteActivities = listOf(activity1, activity3)
    private val localRecords = listOf(record1, record2)
    private val remoteRecords = listOf(record1, record3)

    private lateinit var remoteDataSource: FakeDataSource
    private lateinit var localDataSource: FakeDataSource

    // Class under test
    private lateinit var repository: DefaultTimeoRepository

    @Before
    fun createRepository() {

        localDataSource = FakeDataSource(localActivities, localRecords)
        remoteDataSource = FakeDataSource(remoteActivities, remoteRecords)

        repository = DefaultTimeoRepository(
            remoteDataSource, localDataSource, FakeUserManager, Dispatchers.Main
        )
    }

    @Test
    fun activitiesLiveData_whenUserIsSignedIn_returnsRemoteActivities() {

        // GIVEN - A user is signed in
        FakeUserManager.signIn()

        // WHEN - Getting the list of activities from repository
        val activities = repository.activitiesLiveData as LiveData<List<Activity>>

        // THEN - Activities are loaded from the remote data source
        assertThat(activities.value, IsEqual(remoteActivities))
    }

    @Test
    fun activitiesLiveData_whenUserIsNotSignedIn_returnsLocalActivities() {

        // GIVEN - A user is not signed in
        FakeUserManager.signOut()

        // WHEN - Getting the list of activities from repository
        val activities = repository.activitiesLiveData as LiveData<List<Activity>>

        // THEN - Activities are loaded from the remote data source
        assertThat(activities.value, IsEqual(localActivities))
    }

    @Test
    fun recordsLiveData_whenUserIsSignedIn_returnsRemoteRecords() {

        // GIVEN - A user is signed in
        FakeUserManager.signIn()

        // WHEN - Getting the list of records from repository
        val activities = repository.recordsLiveData as LiveData<List<Record>>

        // THEN - Records are loaded from the remote data source
        assertThat(activities.value, IsEqual(remoteRecords))
    }

    @Test
    fun recordsLiveData_whenUserIsNotSignedIn_returnsLocalRecords() {

        // GIVEN - A user is not signed in
        FakeUserManager.signOut()

        // WHEN - Getting the list of records from repository
        val activities = repository.recordsLiveData as LiveData<List<Record>>

        // THEN - Records are loaded from the local data source
        assertThat(activities.value, IsEqual(localRecords))
    }
}