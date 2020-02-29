package com.jdevs.timeo.data.stats

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query.Direction.ASCENDING
import com.jdevs.timeo.data.DAY
import com.jdevs.timeo.data.DAY_STATS_COLLECTION
import com.jdevs.timeo.data.MONTH_STATS_COLLECTION
import com.jdevs.timeo.data.WEEK_STATS_COLLECTION
import com.jdevs.timeo.data.firestore.FirestoreListDataSource
import com.jdevs.timeo.data.firestore.watchCollection
import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.shared.time.WEEK_DAYS
import com.jdevs.timeo.shared.time.getDaysSinceEpoch
import com.jdevs.timeo.shared.time.getMonthSinceEpoch
import com.jdevs.timeo.shared.time.getWeeksSinceEpoch
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

interface StatsRemoteDataSource : StatsDataSource

@Singleton
class FirestoreStatsDataSource @Inject constructor(authRepository: AuthRepository) :
    FirestoreListDataSource(authRepository),
    StatsRemoteDataSource {

    override val dayStats
        get() = dayStatsRef.watchCollection(
            FirestoreDayStats::mapToDomain, OffsetDateTime::getDaysSinceEpoch
        )

    override val weekStats
        get() = weekStatsRef.watchCollection(
            FirestoreWeekStats::mapToDomain, OffsetDateTime::getWeeksSinceEpoch
        )

    override val monthStats
        get() = monthStatsRef.watchCollection(
            FirestoreMonthStats::mapToDomain, OffsetDateTime::getMonthSinceEpoch
        )

    private var dayStatsRef: CollectionReference by SafeAccess()
    private var weekStatsRef: CollectionReference by SafeAccess()
    private var monthStatsRef: CollectionReference by SafeAccess()

    override fun resetRefs(uid: String) {

        dayStatsRef = createRef(uid, DAY_STATS_COLLECTION)
        weekStatsRef = createRef(uid, WEEK_STATS_COLLECTION)
        monthStatsRef = createRef(uid, MONTH_STATS_COLLECTION)
    }

    private inline fun <reified I : Any, O> CollectionReference.watchCollection(
        crossinline mapFunction: (I) -> O,
        converter: OffsetDateTime.() -> Int
    ) =
        watchCollection(mapFunction, WEEK_DAYS.toLong(), DAY, ASCENDING) {

            this.whereGreaterThan(DAY, OffsetDateTime.now().converter() - WEEK_DAYS)
                .whereLessThanOrEqualTo(DAY, OffsetDateTime.now().converter())
        }
}
