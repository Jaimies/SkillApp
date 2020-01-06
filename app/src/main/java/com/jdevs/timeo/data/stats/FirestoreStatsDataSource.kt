package com.jdevs.timeo.data.stats

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreException.Code
import com.jdevs.timeo.data.auth.AuthRepository
import com.jdevs.timeo.data.firestore.FirestoreDataSource
import com.jdevs.timeo.data.firestore.ItemsLiveData
import com.jdevs.timeo.data.firestore.createCollectionMonitor
import com.jdevs.timeo.data.firestore.model.FirestoreDayStats
import com.jdevs.timeo.data.firestore.model.FirestoreMonthStats
import com.jdevs.timeo.data.firestore.model.FirestoreWeekStats
import com.jdevs.timeo.util.FirestoreConstants.TIME
import com.jdevs.timeo.util.StatsConstants
import com.jdevs.timeo.util.StatsConstants.DAY_PROPERTY
import com.jdevs.timeo.util.StatsConstants.DAY_STATS_COLLECTION
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
    authRepository: AuthRepository
) : FirestoreDataSource(authRepository), StatsRemoteDataSource {

    private val dayStatsMonitor =
        createCollectionMonitor(FirestoreDayStats::class, StatsConstants.PAGE_SIZE, DAY_PROPERTY)

    private val weekStatsMonitor =
        createCollectionMonitor(FirestoreWeekStats::class, StatsConstants.PAGE_SIZE, DAY_PROPERTY)

    private val monthStatsMonitor =
        createCollectionMonitor(FirestoreMonthStats::class, StatsConstants.PAGE_SIZE, DAY_PROPERTY)

    override val dayStats
        get() = dayStatsMonitor.safeAccess().getLiveData()

    override val weekStats
        get() = weekStatsMonitor.safeAccess().getLiveData()

    override val monthStats
        get() = monthStatsMonitor.safeAccess().getLiveData()

    private var dayStatsRef: CollectionReference by SafeInit()
    private var weekStatsRef: CollectionReference by SafeInit()
    private var monthStatsRef: CollectionReference by SafeInit()

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
