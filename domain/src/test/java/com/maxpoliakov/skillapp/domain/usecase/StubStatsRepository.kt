import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class StubStatsRepository : StatsRepository {
    val stats = mutableListOf<Statistic>()

    override fun getStats(skillId: Id): Flow<List<Statistic>> {
        return flowOf(stats)
    }

    override suspend fun addRecord(record: Record) {
        stats.add(Statistic(record.date, record.time))
    }
}
