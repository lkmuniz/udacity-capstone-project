package br.com.muniz.usajob.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.com.muniz.usajob.data.local.getDatabase
import br.com.muniz.usajob.data.repository.JobRepository
import retrofit2.HttpException

class RefreshDataWork(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = JobRepository(database)
        return try {
            repository.refreshJobs()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}