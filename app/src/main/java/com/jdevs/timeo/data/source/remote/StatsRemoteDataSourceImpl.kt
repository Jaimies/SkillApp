package com.jdevs.timeo.data.source.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreException.Code
import com.jdevs.timeo.data.DayStats
import com.jdevs.timeo.data.MonthStats
import com.jdevs.timeo.data.WeekStats
import com.jdevs.timeo.data.source.AuthRepository
import com.jdevs.timeo.util.FirestoreConstants.TIME_PROPERTY
import com.jdevs.timeo.util.StatsConstants
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

@Singleton
class StatsRemoteDataSourceImpl @Inject constructor(
    authRepository: AuthRepository
) : BaseRemoteDataSource(authRepository), StatsRemoteDataSource {

    private val dayStatsMonitor =
        createCollectionMonitor(DayStats::class.java, StatsConstants.PAGE_SIZE)

    private val weekStatsMonitor =
        createCollectionMonitor(WeekStats::class.java, StatsConstants.PAGE_SIZE)

    private val monthStatsMonitor =
        createCollectionMonitor(MonthStats::class.java, StatsConstants.PAGE_SIZE)

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

                ref.update(TIME_PROPERTY, FieldValue.increment(time)).await()
            } catch (e: FirebaseFirestoreException) {

                if (e.code == Code.NOT_FOUND) {

                    ref.set(DayStats(time, ref.id.toLong()))
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
