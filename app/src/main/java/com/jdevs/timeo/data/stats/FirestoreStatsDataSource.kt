package com.jdevs.timeo.data.stats

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreException.Code
import com.jdevs.timeo.data.firestore.FirestoreListDataSource
import com.jdevs.timeo.data.firestore.createCollectionMonitor
import com.jdevs.timeo.domain.model.DayStats
import com.jdevs.timeo.domain.model.MonthStats
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.model.WeekStats
import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.util.FirestoreConstants.TIME
import com.jdevs.timeo.util.StatsConstants.DAY_PROPERTY
import com.jdevs.timeo.util.StatsConstants.DAY_STATS_COLLECTION
import com.jdevs.timeo.util.StatsConstants.FIRESTORE_STATS_PAGE_SIZE
import com.jdevs.timeo.util.StatsConstants.MONTH_STATS_COLLECTION
import com.jdevs.timeo.util.StatsConstants.WEEK_STATS_COLLECTION
import com.jdevs.timeo.util.time.getDaysSinceEpoch
import com.jdevs.timeo.util.time.getMonthSinceEpoch
import com.jdevs.timeo.util.time.getWeeksSinceEpoch
import kotlinx.coroutines.tasks.await
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton


interface StatsRemoteDataSource {

    val dayStats: List<LiveData<Operation<DayStats>>>
    val weekStats: List<LiveData<Operation<WeekStats>>>
    val monthStats: List<LiveData<Operation<MonthStats>>>

    suspend fun updateStats(date: OffsetDateTime, time: Long)
}

@Singleton
class FirestoreStatsDataSource @Inject constructor(authRepository: AuthRepository) :
    FirestoreListDataSource(authRepository),
    StatsRemoteDataSource {

    private val dayStatsMonitor =
        createCollectionMonitor(
            FIRESTORE_STATS_PAGE_SIZE, FirestoreDayStats::mapToDomain, DAY_PROPERTY
        )

    private val weekStatsMonitor =
        createCollectionMonitor(
            FIRESTORE_STATS_PAGE_SIZE, FirestoreWeekStats::mapToDomain, DAY_PROPERTY
        )

    private val monthStatsMonitor =
        createCollectionMonitor(
            FIRESTORE_STATS_PAGE_SIZE, FirestoreMonthStats::mapToDomain, DAY_PROPERTY
        )

    override val dayStats
        get() = dayStatsMonitor.safeAccess().getLiveDataList()

    override val weekStats
        get() = weekStatsMonitor.safeAccess().getLiveDataList()

    override val monthStats
        get() = monthStatsMonitor.safeAccess().getLiveDataList()

    private var dayStatsRef: CollectionReference by SafeAccess()
    private var weekStatsRef: CollectionReference by SafeAccess()
    private var monthStatsRef: CollectionReference by SafeAccess()

    override suspend fun updateStats(date: OffsetDateTime, time: Long) {

        val refs = listOf(
            dayStatsRef.document(date.getDaysSinceEpoch().toString()),
            weekStatsRef.document(date.getWeeksSinceEpoch().toString()),
            monthStatsRef.document(date.getMonthSinceEpoch().toString())
        )

        refs.forEach { ref ->

            try {

                ref.update(TIME, FieldValue.increment(time)).await()
            } catch (e: FirebaseFirestoreException) {

                if (e.code == Code.NOT_FOUND) {

                    ref.set(FirestoreDayStats(time = time, day = ref.id.toLong()))
                }
            }
        }
    }

    override fun resetRefs(uid: String) {

        dayStatsRef = createRef(uid, DAY_STATS_COLLECTION, dayStatsMonitor)
        weekStatsRef = createRef(uid, WEEK_STATS_COLLECTION, weekStatsMonitor)
        monthStatsRef = createRef(uid, MONTH_STATS_COLLECTION, monthStatsMonitor)
    }
}
