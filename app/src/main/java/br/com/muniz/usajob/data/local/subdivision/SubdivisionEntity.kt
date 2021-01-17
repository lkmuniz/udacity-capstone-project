package br.com.muniz.usajob.data.local.subdivision

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subdivision")
data class SubdivisionEntity(
    @PrimaryKey val name: String
)