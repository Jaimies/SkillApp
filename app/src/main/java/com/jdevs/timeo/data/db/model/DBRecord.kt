package com.jdevs.timeo.data.db.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.jdevs.timeo.data.Mapper
import com.jdevs.timeo.domain.model.Record
import org.threeten.bp.OffsetDateTime

@Keep
@Entity(
    tableName = "records",
    indices = [Index(value = ["activityId"])],
    foreignKeys = [ForeignKey(
        entity = DBActivity::class,
        parentColumns = ["id"],
        childColumns = ["activityId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class DBRecord(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = "",
    var time: Long = 0,
    var activityId: Int = 0,
    var creationDate: OffsetDateTime = OffsetDateTime.now()
) : Mapper<Record> {

    override fun mapToDomain() = Record(id, "", name, time, "", activityId, creationDate)
}
