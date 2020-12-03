import androidx.paging.PagingData
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import kotlinx.coroutines.flow.flowOf

class StubRecordsRepository(records: List<Record>) : RecordsRepository {
    private val records = records.toMutableList()

    override fun getRecords() = flowOf(PagingData.from(records))

    override suspend fun getRecord(id: Int) = records.find { it.id == id }!!
    override suspend fun addRecord(record: Record) {}
    override suspend fun deleteRecord(record: Record) {}

    override suspend fun updateRecord(record: Record) {
        records[0] = record
    }
}
