package br.com.muniz.usajob.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.muniz.usajob.data.local.subdivision.SubdivisionDao
import br.com.muniz.usajob.data.local.subdivision.SubdivisionEntity

@Database(entities = [JobEntity::class, SubdivisionEntity::class], version = 1)
abstract class JobDatabase : RoomDatabase() {
    abstract val jobDao: JobDao

    abstract val subdivisionDao: SubdivisionDao
}

@Volatile
private lateinit var INSTANCE: JobDatabase

fun getDatabase(context: Context): JobDatabase {
    synchronized(JobDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                JobDatabase::class.java,
                "job_database"
            ).build()
        }
    }
    return INSTANCE
}