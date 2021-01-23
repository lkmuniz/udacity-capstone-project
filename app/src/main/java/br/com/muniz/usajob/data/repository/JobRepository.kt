package br.com.muniz.usajob.data.repository

import br.com.muniz.usajob.Constants
import br.com.muniz.usajob.data.Job
import br.com.muniz.usajob.data.local.JobDatabase
import br.com.muniz.usajob.data.local.subdivision.Subdivision
import br.com.muniz.usajob.data.remote.Network
import br.com.muniz.usajob.utils.DataState
import br.com.muniz.usajob.utils.asDatabaseModel
import br.com.muniz.usajob.utils.subdivisionAsDatabaseModel
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

    val subdivisionList = jobDataBase.subdivisionDao.getAllSubdivision()

    suspend fun refreshJobs(
        locationName: String = "",
        page: String = Constants.PAGE_NUMBER,
        resultPerPage: String = Constants.RESULT_PER_PAGE,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<DataState> {
        return flow {
            emit(DataState.Loading)
            withContext(dispatcher) {
                try {
                    val url =
                        createUrl(locationName, page, resultPerPage)
                    Timber.d("Request url: $url")
                    val result = Network.jobs.getJobs(url).await()
                    val resultParsed = parseJobJsonResult(JSONObject(result), locationName)
                    jobDataBase.jobDao.insertAll(resultParsed.asDatabaseModel())
                    emit(DataState.Success)
                } catch (throwable: Throwable) {
                    Timber.e(throwable.message)
                    emit(DataState.Error)
                }
            }
        }.flowOn(dispatcher)
    }

    private fun createUrl(locationName: String, page: String, resultPerPage: String): String {
        var ret = "search?"
        if (locationName.isNotEmpty() && locationName != Constants.DEFAULT)
            ret += "LocationName=$locationName&"
        return "${ret}Page=$page&ResultsPerPage=$resultPerPage"
    }

    suspend fun refreshSubdivision(
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<DataState> {
        return flow {
            emit(DataState.Loading)
            withContext(dispatcher) {
                try {
                    val result = Network.jobs.getSubdivision().await()
                    val resultParsed = parseSubdivisionsJsonResult(JSONObject(result))
                    jobDataBase.subdivisionDao.insertAll(resultParsed.subdivisionAsDatabaseModel())
                    emit(DataState.Success)
                } catch (throwable: Throwable) {
                    Timber.e(throwable.message)
                    emit(DataState.Error)
                }
            }
        }.flowOn(dispatcher)
    }

    private fun parseSubdivisionsJsonResult(jsonObject: JSONObject): List<Subdivision> {
        val subdivisionList = ArrayList<Subdivision>()
        // First option of the spinner is Default
        var countrySubdivision = Subdivision(Constants.DEFAULT)
        subdivisionList.add(countrySubdivision)

        val codeListtItems = jsonObject.getJSONArray("CodeList")
        val searchResultItems = codeListtItems.getJSONObject(0).getJSONArray("ValidValue")

        for (i in 0 until searchResultItems.length()) {
            val subdivisionJson = searchResultItems.getJSONObject(i)
            var parentCode = subdivisionJson.getString("ParentCode")
            if (parentCode == "US") {
                var subdivisionName = subdivisionJson.getString("Value")
                if (subdivisionName == "Undefined")
                    subdivisionName = Constants.DEFAULT
                countrySubdivision = Subdivision(subdivisionName)
                subdivisionList.add(countrySubdivision)
            }

        }

        return subdivisionList
    }

    fun clearJobRepository() {
        jobDataBase.jobDao.clearTable()
    }

    suspend fun clearAndRefreshDatabase() {
        clearJobRepository()
        refreshJobs()
    }

    private fun parseJobJsonResult(
        jsonResult: JSONObject,
        locationPrefName: String
    ): List<Job> {

        val jobList = ArrayList<Job>()

        val searchResultJson = jsonResult.getJSONObject("SearchResult")
        val searchResultItems = searchResultJson.getJSONArray("SearchResultItems")

        for (i in 0 until searchResultItems.length()) lit@ {
            val jobJson = searchResultItems.getJSONObject(i)

            val jobId = jobJson.getLong("MatchedObjectId")

            val matchedObjectDescriptorJson = jobJson.getJSONObject("MatchedObjectDescriptor")

            val applyUri =
                matchedObjectDescriptorJson.getString("ApplyURI").replace("[", "").replace("]", "")
                    .replace("\\", "")

            val jobName = matchedObjectDescriptorJson.getString("PositionTitle")

            val positionLocationArray = matchedObjectDescriptorJson.getJSONArray("PositionLocation")

            val positionLocationJSONObject = positionLocationArray.getJSONObject(0)

            val locationName = getStringFromJSON(positionLocationJSONObject, "LocationName")

            val locationCountry = getStringFromJSON(positionLocationJSONObject, "CountryCode")

            val countrySubDivisionCode =
                getStringFromJSON(positionLocationJSONObject, "CountrySubDivisionCode")

            val longitude = getStringFromJSON(positionLocationJSONObject, "Longitude")

            val latitude = getStringFromJSON(positionLocationJSONObject, "Latitude")

            val organizationName = matchedObjectDescriptorJson.getString("OrganizationName")

            val jobCategoryArray = matchedObjectDescriptorJson.getJSONArray("JobCategory")
            val jobCategoryJSONObject = jobCategoryArray.getJSONObject(0)
            val jobCategory = jobCategoryJSONObject.getString("Name")

            val jobPositionRemunerationArray =
                matchedObjectDescriptorJson.getJSONArray("PositionRemuneration")
            val jobCPositionRemunerationJSONObject = jobPositionRemunerationArray.getJSONObject(0)
            val jobMinimumRange = jobCPositionRemunerationJSONObject.getString("MinimumRange")
            val jobMaximumRange = jobCPositionRemunerationJSONObject.getString("MaximumRange")
            val jobRateIntervalCode =
                jobCPositionRemunerationJSONObject.getString("RateIntervalCode")

            if ((hasNoPrefLocation(locationPrefName) ||
                        isPrefLocation(locationName, locationPrefName))
                && isUnitedStates(locationCountry)
            ) {
                val job = Job(
                    jobId,
                    applyUri,
                    locationName,
                    locationCountry,
                    countrySubDivisionCode,
                    longitude,
                    latitude,
                    organizationName,
                    jobName,
                    jobCategory,
                    jobMinimumRange,
                    jobMaximumRange,
                    jobRateIntervalCode
                )
                jobList.add(job)
            }
        }

        return jobList
    }

    private fun hasNoPrefLocation(locationPrefName: String): Boolean {
        return locationPrefName == Constants.DEFAULT
    }

    private fun isPrefLocation(location: String, locationPrefName: String): Boolean {
        return location.contains(locationPrefName)
    }

    private fun isUnitedStates(locationCountry: String): Boolean {
        return locationCountry == Constants.COUNTRY_CODE
    }

    private fun getStringFromJSON(objectJSONObject: JSONObject, searchString: String): String {
        if (objectJSONObject.has(searchString))
            return objectJSONObject.getString(searchString)

        return ""
    }

}


