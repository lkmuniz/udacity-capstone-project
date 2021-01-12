package br.com.muniz.usajob.data.repository

import br.com.muniz.usajob.data.local.JobDatabase
import br.com.muniz.usajob.data.remote.Network
import br.com.muniz.usajob.utils.DataState
import br.com.muniz.usajob.utils.asDatabaseModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class JobRepository(private val jobDataBase: JobDatabase) {

    val jobLocal = jobDataBase.jobDao.getAllJobs()

    suspend fun refreshJobs(
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<DataState> {
        return flow {
            emit(DataState.Loading)
            withContext(dispatcher) {
                try {
                    val result = Network.jobs.getJobs().await()
                    jobDataBase.jobDao.insertAll(result.asDatabaseModel())
                    emit(DataState.Success)
                } catch (throwable: Throwable) {
                    emit(DataState.Error)
                }
            }
        }.flowOn(dispatcher)
    }
}


