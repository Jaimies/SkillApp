package com.jdevs.timeo.data.stats

import com.jdevs.timeo.data.Repository
import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.domain.repository.StatsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultStatsRepository @Inject constructor(
    remoteDataSource: StatsRemoteDataSource,
    localDataSource: StatsDataSource,
    authRepository: AuthRepository
) : Repository<StatsDataSource, StatsRemoteDataSource>(
    remoteDataSource, localDataSource, authRepository
),
    StatsRepository {

    override val dayStats get() = currentDataSource.dayStats
    override val weekStats get() = currentDataSource.weekStats
    override val monthStats get() = currentDataSource.monthStats
}
