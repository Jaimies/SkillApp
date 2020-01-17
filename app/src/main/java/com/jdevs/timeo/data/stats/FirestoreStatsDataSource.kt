package com.jdevs.timeo.data.stats

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreException.Code
import com.jdevs.timeo.data.firestore.FirestoreListDataSource
import com.jdevs.timeo.data.firestore.ItemsLiveData
import com.jdevs.timeo.data.firestore.createCollectionMonitor
import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.util.FirestoreConstants.TIME
import com.jdevs.timeo.util.StatsConstants.DAY_PROPERTY
import com.jdevs.timeo.util.StatsConstants.DAY_STATS_COLLECTION
import com.jdevs.timeo.util.StatsConstants.FIRESTORE_STATS_PAGE_SIZE
import com.jdevs.timeo.util.StatsConstants.MONTH_STATS_COLLECTION
import com.jdevs.timeo.util.StatsConstants.WEEK_STATS_COLLECTION
import com.jdevs.timeo.util.await
import com.jdevs.timeo.util.time.getDaysSinceEpoch
import com.jdevs.timeo.util.time.getMonthSinceEpoch
import com.jdevs.timeo.util.time.getWeeksSinceEpoch
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton


interface StatsRemoteDataSource : StatsDataSource {

    override val dayStats: ItemsLiveData?
    override val weekStats: ItemsLiveData?
    override val monthStats: ItemsLiveData?

    suspend fun updateStats(date: OffsetDateTime, time: Long)

    fun resetDayStatsMonitor()
    fun resetWeekStatsMonitor()
    fun resetMonthStatsMonitor()
}

@Singleton
class FirestoreStatsDataSource @Inject constructor(
    authRepository: AuthRepository,
    dayStatsMapper: FirestoreStatsMapper,
    weekStatsMapper: FirestoreWeekStatsMapper,
    monthStatsMapper: FirestoreMonthStatsMapper
) :
    FirestoreListDataSource(authRepository),
    StatsRemoteDataSource {

    private val dayStatsMonitor =
        createCollectionMonitor(
            FirestoreDayStats::class, dayStatsMapper,
            FIRESTORE_STATS_PAGE_SIZE, DAY_PROPERTY
        )

    private val weekStatsMonitor =
        createCollectionMonitor(
            FirestoreWeekStats::class, weekStatsMapper,
            FIRESTORE_STATS_PAGE_SIZE, DAY_PROPERTY
        )

    private val monthStatsMonitor =
        createCollectionMonitor(
            FirestoreMonthStats::class, monthStatsMapper,
            FIRESTORE_STATS_PAGE_SIZE, DAY_PROPERTY
        )

    override val dayStats
        get() = dayStatsMonitor.safeAccess().getLiveData()

    override val weekStats
        get() = weekStatsMonitor.safeAccess().getLiveData()

    override val monthStats
        get() = monthStatsMonitor.safeAccess().getLiveData()

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

    override fun resetDayStatsMonitor() = dayStatsMonitor.reset()
    override fun resetWeekStatsMonitor() = weekStatsMonitor.reset()
    override fun resetMonthStatsMonitor() = monthStatsMonitor.reset()
}
