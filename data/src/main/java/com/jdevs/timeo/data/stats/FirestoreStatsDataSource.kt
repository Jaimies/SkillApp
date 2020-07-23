package com.jdevs.timeo.data.stats

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query.Direction.ASCENDING
import com.jdevs.timeo.data.DAY
import com.jdevs.timeo.data.DAY_STATS_COLLECTION
import com.jdevs.timeo.data.MONTH_STATS_COLLECTION
import com.jdevs.timeo.data.WEEK_STATS_COLLECTION
import com.jdevs.timeo.data.firestore.FirestoreListDataSource
import com.jdevs.timeo.data.firestore.watch
import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.shared.util.daysSinceEpoch
import com.jdevs.timeo.shared.util.monthSinceEpoch
import com.jdevs.timeo.shared.util.weeksSinceEpoch
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

interface StatsRemoteDataSource : StatsDataSource

@Singleton
class FirestoreStatsDataSource @Inject constructor(authRepository: AuthRepository) :
    FirestoreListDataSource(authRepository),
    StatsRemoteDataSource {

    override val dayStats get() = dayStatsRef.watch(DAY_STATS_ENTRIES) { daysSinceEpoch }
    override val weekStats get() = weekStatsRef.watch { weeksSinceEpoch }
    override val monthStats get() = monthStatsRef.watch { monthSinceEpoch }

    private lateinit var dayStatsRef: CollectionReference
    private lateinit var weekStatsRef: CollectionReference
    private lateinit var monthStatsRef: CollectionReference

    override fun resetRefs(uid: String) {
        dayStatsRef = createRef(uid, DAY_STATS_COLLECTION)
        weekStatsRef = createRef(uid, WEEK_STATS_COLLECTION)
        monthStatsRef = createRef(uid, MONTH_STATS_COLLECTION)
    }

    private inline fun CollectionReference.watch(
        count: Int = STATS_ENTRIES,
        converter: OffsetDateTime.() -> Int
    ) =
        this.whereGreaterThan(DAY, OffsetDateTime.now().converter() - count)
            .whereLessThanOrEqualTo(DAY, OffsetDateTime.now().converter())
            .orderBy(DAY, ASCENDING)
            .limit(count.toLong())
            .watch(FirestoreStat::mapToDomain)
}
