package br.com.muniz.usajob.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "job")
data class JobEntity(
    @PrimaryKey val id: Long,
    val applyUri: String,
    val locationName: String,
    val country: String,
    val countrySubDivisionCode: String,
    val longitude: String,
    val latitude: String,
    val organizationName: String,
    val jobName: String,
    val jobCategory: String,
    val jobMinimumRange: String,
    val jobMaximumRange: String,
    val jobRateIntervalCode: String,
)