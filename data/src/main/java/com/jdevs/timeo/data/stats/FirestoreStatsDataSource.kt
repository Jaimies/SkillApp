package com.jdevs.timeo.data.stats

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query.Direction.ASCENDING
import com.jdevs.timeo.data.DAY
import com.jdevs.timeo.data.DAY_STATS_COLLECTION
import com.jdevs.timeo.data.MONTH_STATS_COLLECTION
import com.jdevs.timeo.data.WEEK_STATS_COLLECTION
import com.jdevs.timeo.data.firestore.FirestoreListDataSource
import com.jdevs.timeo.data.firestore.watch
import com.jdevs.timeo.data.util.whereInRange
import com.jdevs.timeo.domain.model.Statistic
import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.shared.util.getUnitsSinceEpoch
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.ChronoUnit.DAYS
import org.threeten.bp.temporal.ChronoUnit.MONTHS
import org.threeten.bp.temporal.ChronoUnit.WEEKS
import javax.inject.Inject
import javax.inject.Singleton

interface StatsRemoteDataSource : StatsDataSource

@Singleton
class FirestoreStatsDataSource @Inject constructor(authRepository: AuthRepository) :
    FirestoreListDataSource(authRepository),
    StatsRemoteDataSource {

    override val dayStats get() = dayStatsRef.watch(DAYS, DAY_STATS_ENTRIES)
    override val weekStats get() = weekStatsRef.watch(WEEKS)
    override val monthStats get() = monthStatsRef.watch(MONTHS)

    private lateinit var dayStatsRef: CollectionReference
    private lateinit var weekStatsRef: CollectionReference
    private lateinit var monthStatsRef: CollectionReference

    override fun resetRefs(uid: String) {
        dayStatsRef = createRef(uid, DAY_STATS_COLLECTION)
        weekStatsRef = createRef(uid, WEEK_STATS_COLLECTION)
        monthStatsRef = createRef(uid, MONTH_STATS_COLLECTION)
    }

    private fun CollectionReference.watch(
        unit: ChronoUnit, resultCount: Int = STATS_ENTRIES
    ): LiveData<List<Statistic>> {

        val unitsSinceEpoch = getUnitsSinceEpoch(unit)

        return this
            .whereInRange(DAY, unitsSinceEpoch - resultCount, unitsSinceEpoch)
            .orderBy(DAY, ASCENDING)
            .watch(FirestoreStat::mapToDomain)
    }
}
