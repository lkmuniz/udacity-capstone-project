package br.com.muniz.usajob.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(jobs: List<JobEntity>)

    @Query("SELECT * FROM job")
    fun getAllJobs(): Flow<List<JobEntity>>

    @Query("DELETE FROM job")
    fun clearTable()
}