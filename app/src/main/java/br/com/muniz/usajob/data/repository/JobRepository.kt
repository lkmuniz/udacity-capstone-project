package br.com.muniz.usajob.data.repository

import br.com.muniz.usajob.data.Job
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
import org.json.JSONObject
import timber.log.Timber

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
                    val resultParsed = parseAsteroidsJsonResult(JSONObject(result))

                    jobDataBase.jobDao.insertAll(resultParsed.asDatabaseModel())
                    emit(DataState.Success)
                } catch (throwable: Throwable) {
                    emit(DataState.Error)
                }
            }
        }.flowOn(dispatcher)
    }

    fun parseAsteroidsJsonResult(jsonResult: JSONObject): List<Job> {

        val jobList = ArrayList<Job>()

        val searchResultJson = jsonResult.getJSONObject("SearchResult")
        val searchResultItems = searchResultJson.getJSONArray("SearchResultItems")

        for (i in 0 until searchResultItems.length()) {
            val jobJson = searchResultItems.getJSONObject(i)

            val jobId = jobJson.getLong("MatchedObjectId")

            val matchedObjectDescriptorJson = jobJson.getJSONObject("MatchedObjectDescriptor")

            val applyUri = matchedObjectDescriptorJson.getString("ApplyURI").replace("[", "").replace("]","").replace("\\","")

            val positionLocationArray = matchedObjectDescriptorJson.getJSONArray("PositionLocation")

            val positionLocationJSONObject = positionLocationArray.getJSONObject(0)

            val locationName = positionLocationJSONObject.getString("LocationName")

            val locationCountry = positionLocationJSONObject.getString("CountryCode")

            val countrySubDivisionCode =
                positionLocationJSONObject.getString("CountrySubDivisionCode")

            val longitude = positionLocationJSONObject.getString("Longitude")

            val latitude = positionLocationJSONObject.getString("Latitude")


            Timber.d("New job added from JSON: " +
                    "id: $jobId"+
                    " applyUri: $applyUri"+
                    " locationName: $locationName"+
                    " locationCountry: $locationCountry"+
                    " longitude: $longitude"+
                    " latitude: $latitude"
            )
            val job = Job(jobId, applyUri, locationName, locationCountry, countrySubDivisionCode, longitude, latitude)
            jobList.add(job)
        }

        return jobList
    }
}


