package br.com.muniz.usajob.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [JobEntity::class], version = 1)
abstract class JobDatabase : RoomDatabase() {
    abstract val jobDao: JobDao
}

@Volatile
private lateinit var INSTANCE: JobDatabase

fun getDatabase(context: Context): JobDatabase {
    synchronized(JobDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                JobDatabase::class.java,
                "asteroids"
            ).build()
        }
    }
    return INSTANCE
}