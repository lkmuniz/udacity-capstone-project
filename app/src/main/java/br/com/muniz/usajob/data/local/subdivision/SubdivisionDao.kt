package br.com.muniz.usajob.data.local.subdivision

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SubdivisionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(jobs: List<SubdivisionEntity>)

    @Query("SELECT * FROM subdivision")
    fun getAllSubdivision(): Flow<List<SubdivisionEntity>>

    @Query("DELETE FROM subdivision")
    fun clearTable()
}