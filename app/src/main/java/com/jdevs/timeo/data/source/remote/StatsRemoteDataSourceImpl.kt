package com.jdevs.timeo.data.source.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.jdevs.timeo.data.DayStats
import com.jdevs.timeo.data.source.AuthRepository
import com.jdevs.timeo.util.StatsConstants.DAY_STATS_COLLECTION
import com.jdevs.timeo.util.StatsConstants.MONTH_STATS_COLLECTION
import com.jdevs.timeo.util.StatsConstants.WEEK_STATS_COLLECTION
import com.jdevs.timeo.util.await
import com.jdevs.timeo.util.time.getDaysSinceEpoch
import com.jdevs.timeo.util.time.getMonthSinceEpoch
import com.jdevs.timeo.util.time.getWeeksSinceEpoch
import org.threeten.bp.OffsetDateTime

class StatsRemoteDataSourceImpl(
    private val dayStatsMonitor: CollectionMonitor,
    private val weekStatsMonitor: CollectionMonitor,
    private val monthStatsMonitor: CollectionMonitor,
    authRepository: AuthRepository
) : BaseRemoteDataSource(authRepository), StatsRemoteDataSource {

    override val dayStats
        get() = dayStatsMonitor.getLiveData().also { reset() }

    override val weekStats
        get() = weekStatsMonitor.getLiveData().also { reset() }

    override val monthStats
        get() = monthStatsMonitor.getLiveData().also { reset() }

    private lateinit var dayStatsRef: CollectionReference
    private lateinit var weekStatsRef: CollectionReference
    private lateinit var monthStatsRef: CollectionReference

    override suspend fun updateStats(date: OffsetDateTime, time: Long) {

        val refs = listOf(
            dayStatsRef.document(date.getDaysSinceEpoch().toString()),
            weekStatsRef.document(date.getWeeksSinceEpoch().toString()),
            monthStatsRef.document(date.getMonthSinceEpoch().toString())
        )

        refs.forEach { ref ->

            try {

                ref.update("time", FieldValue.increment(time)).await()
            } catch (e: FirebaseFirestoreException) {

                if (e.code == FirebaseFirestoreException.Code.NOT_FOUND) {

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
