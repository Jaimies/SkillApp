package com.jdevs.timeo.data.stats

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.CollectionReference
import com.jdevs.timeo.data.DAY
import com.jdevs.timeo.data.DAY_STATS_COLLECTION
import com.jdevs.timeo.data.MONTH_STATS_COLLECTION
import com.jdevs.timeo.data.WEEK_STATS_COLLECTION
import com.jdevs.timeo.data.firestore.FirestoreListDataSource
import com.jdevs.timeo.data.firestore.createCollectionWatcher
import com.jdevs.timeo.domain.model.DayStats
import com.jdevs.timeo.domain.model.MonthStats
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.model.WeekStats
import com.jdevs.timeo.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton


interface StatsRemoteDataSource {

    val dayStats: List<LiveData<Operation<DayStats>>>
    val weekStats: List<LiveData<Operation<WeekStats>>>
    val monthStats: List<LiveData<Operation<MonthStats>>>
}

@Singleton
class FirestoreStatsDataSource @Inject constructor(authRepository: AuthRepository) :
    FirestoreListDataSource(authRepository),
    StatsRemoteDataSource {

    private val dayStatsWatcher =
        createCollectionWatcher(PAGE_SIZE, FirestoreDayStats::mapToDomain, DAY)

    private val weekStatsWatcher =
        createCollectionWatcher(PAGE_SIZE, FirestoreWeekStats::mapToDomain, DAY)

    private val monthStatsWatcher =
        createCollectionWatcher(PAGE_SIZE, FirestoreMonthStats::mapToDomain, DAY)

    override val dayStats
        get() = dayStatsWatcher.safeAccess().getLiveDataList()

    override val weekStats
        get() = weekStatsWatcher.safeAccess().getLiveDataList()

    override val monthStats
        get() = monthStatsWatcher.safeAccess().getLiveDataList()

    private var dayStatsRef: CollectionReference by SafeAccess()
    private var weekStatsRef: CollectionReference by SafeAccess()
    private var monthStatsRef: CollectionReference by SafeAccess()

    override fun resetRefs(uid: String) {

        dayStatsRef = createRef(uid, DAY_STATS_COLLECTION, dayStatsWatcher)
        weekStatsRef = createRef(uid, WEEK_STATS_COLLECTION, weekStatsWatcher)
        monthStatsRef = createRef(uid, MONTH_STATS_COLLECTION, monthStatsWatcher)
    }

    companion object {
        private const val PAGE_SIZE = 40L
    }
}
