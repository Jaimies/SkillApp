package com.jdevs.timeo.data

import androidx.lifecycle.LiveData
import com.jdevs.timeo.data.records.DefaultRecordsRepository
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.Record
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for the implementation of [RecordsRepository]
 */
@Suppress("UNCHECKED_CAST")
class RecordsRepositoryImplTest {

    private val activity1 = Activity(name = "Activity 1")
    private val activity2 = Activity(name = "Activity 2")
    private val activity3 = Activity(name = "Activity 3")
    private val record1 = Record(name = "Record 1")
    private val record2 = Record(name = "Record 2")
    private val record3 = Record(name = "Record 3")

    private val localActivities = listOf(activity1, activity2)
    private val localRecords = listOf(record1, record2)
    private val remoteActivities = listOf(activity1, activity3)
    private val remoteRecords = listOf(record1, record3)

    private lateinit var localDataSource: FakeDataSource
    private lateinit var remoteDataSource: FakeRemoteDataSource
    private lateinit var fakeAuthRepository: FakeAuthRepository

    // Class under test
    private lateinit var repository: DefaultRecordsRepository

    @Before
    fun createRepository() {

        localDataSource = FakeDataSource(localActivities, localRecords)
        remoteDataSource = FakeRemoteDataSource(remoteActivities, remoteRecords)

        fakeAuthRepository = FakeAuthRepository()
        repository =
            DefaultRecordsRepository(
                remoteDataSource,
                localDataSource,
                fakeAuthRepository
            )
    }

    @Test
    fun activities_whenUserIsSignedIn_requestsActivitiesFromRemoteDataSource() {

        // GIVEN - A user is signed in
        fakeAuthRepository.signIn()

        // WHEN - Getting the list of activities from repository
        val activities = repository.activities as LiveData<List<Activity>>

        // THEN - Activities are loaded from the remote data source
        assertThat(activities.value, IsEqual(remoteActivities))
    }

    @Test
    fun activities_whenUserIsNotSignedIn_requestsActivitiesFromLocalDataSource() {

        // GIVEN - A user is not signed in
        fakeAuthRepository.signOut()

        // WHEN - Getting the list of activities from repository
        val activities = repository.activities as LiveData<List<Activity>>

        // THEN - Activities are loaded from the remote data source
        assertThat(activities.value, IsEqual(localActivities))
    }

    @Test
    fun records_whenUserIsSignedIn_requestsRecordsFromRemoteDataSource() {

        // GIVEN - A user is signed in
        fakeAuthRepository.signIn()

        // WHEN - Getting the list of records from repository
        val activities = repository.records as LiveData<List<Record>>

        // THEN - Records are loaded from the remote data source
        assertThat(activities.value, IsEqual(remoteRecords))
    }

    @Test
    fun records_whenUserIsNotSignedIn_requestsRecordsFromLocalDataSource() {

        // GIVEN - A user is not signed in
        fakeAuthRepository.signOut()

        // WHEN - Getting the list of records from repository
        val activities = repository.records as LiveData<List<Record>>

        // THEN - Records are loaded from the local data source
        assertThat(activities.value, IsEqual(localRecords))
    }
}
