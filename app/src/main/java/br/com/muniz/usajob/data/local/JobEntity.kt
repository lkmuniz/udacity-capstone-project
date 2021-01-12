package br.com.muniz.usajob.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "job")
data class JobEntity(
    @PrimaryKey val id: Long,
    val Code: String,
    val Value: String,
    val LastModified: String,
    val IsDisabled: String
) {
}