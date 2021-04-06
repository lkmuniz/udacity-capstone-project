package br.com.muniz.usajob.data.repository

import br.com.muniz.usajob.Constants
import br.com.muniz.usajob.data.Job
import br.com.muniz.usajob.data.local.JobDatabase
import br.com.muniz.usajob.data.local.subdivision.Subdivision
import br.com.muniz.usajob.data.remote.JobResult
import br.com.muniz.usajob.data.remote.Network
import br.com.muniz.usajob.data.remote.SubdivisionResult
import br.com.muniz.usajob.utils.DataState
import br.com.muniz.usajob.utils.asDatabaseModel
import br.com.muniz.usajob.utils.subdivisionAsDatabaseModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber

class JobRepository(private val jobDataBase: JobDatabase) {

    val jobLocal = jobDataBase.jobDao.getAllJobs()

    val subdivisionList = jobDataBase.subdivisionDao.getAllSubdivision()

    suspend fun refreshRepositoryJobs(
        locationName: String = "",
        keyword: String = "",
        page: String = Constants.PAGE_NUMBER,
        resultPerPage: String = Constants.RESULT_PER_PAGE,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<DataState> {
        return flow {
            emit(DataState.Loading)
            withContext(dispatcher) {
                try {
                    val result = Network.jobs.getJobs(
                        locationName = locationName,
                        page = page,
                        resultsPerPage = resultPerPage,
                        keyword = keyword
                    ).await()
                    val resultParsed = parseJobJsonResult(result, locationName)
                    jobDataBase.jobDao.insertAll(resultParsed.asDatabaseModel())
                    emit(DataState.Success)
                } catch (throwable: Throwable) {
                    Timber.e(throwable.message)
                    emit(DataState.Error)
                }
            }
        }.flowOn(dispatcher)
    }

    suspend fun refreshSubdivision(
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<DataState> {
        return flow {
            emit(DataState.Loading)
            withContext(dispatcher) {
                try {
                    val result = Network.jobs.getSubdivision().await()
                    val resultParsed = parseSubdivisionsJsonResult(result)
                    jobDataBase.subdivisionDao.insertAll(resultParsed.subdivisionAsDatabaseModel())
                    emit(DataState.Success)
                } catch (throwable: Throwable) {
                    Timber.e(throwable.message)
                    emit(DataState.Error)
                }
            }
        }.flowOn(dispatcher)
    }

    private fun parseSubdivisionsJsonResult(subdivisionResult: SubdivisionResult): List<Subdivision> {
        val subdivisionList = ArrayList<Subdivision>()
        // First option of the spinner is Default
        var countrySubdivision = Subdivision(Constants.DEFAULT)
        subdivisionList.add(countrySubdivision)

        subdivisionResult.codeList?.forEach { codeList ->
            codeList.validValue?.forEach { validValue ->
                if (validValue.parentCode == Constants.DEFAULT_COUNTRY) {
                    subdivisionList.add(Subdivision(validValue.value.toString()))
                }
            }
        }

        return subdivisionList
    }

    fun clearJobRepository() {
        jobDataBase.jobDao.clearTable()
    }

    suspend fun clearAndRefreshDatabase() {
        clearJobRepository()
        refreshRepositoryJobs()
    }

    private fun parseJobJsonResult(
        jobResult: JobResult,
        locationPrefName: String
    ): List<Job> {

        val jobList = ArrayList<Job>()

        jobResult.searchResult?.searchResultItems?.forEach { searchResultItems ->
            val jobId = searchResultItems.matchedObjectID!!.toLong()
            val applyUri = searchResultItems.matchedObjectDescriptor?.applyURI?.get(0)
            val positionLocationArray = searchResultItems.matchedObjectDescriptor?.positionLocation
            var locationName: String? = ""
            var countryCode: String? = ""
            var countrySubDivisionCode: String? = ""
            var longitude: String? = ""
            var latitude: String? = ""
            var skip = 0
            positionLocationArray?.forEach { posLocation ->

                locationName = verifyNull(posLocation.locationName)
                countryCode = verifyNull(posLocation.countryCode)
                countrySubDivisionCode = verifyNull(posLocation.countrySubDivisionCode)
                longitude = verifyNull(posLocation.longitude.toString())
                latitude = verifyNull(posLocation.latitude.toString())

                // Check if there is a pref location and if is the right location
                if (!(hasNoPrefLocation(locationPrefName) ||
                            isPrefLocation(locationName, locationPrefName))
                    && isUnitedStates(countryCode)
                ) {
                    skip = 1
                }
            }

            // if there is no location skip to the next job
            if (skip == 1) return@forEach

            val organizationName = searchResultItems.matchedObjectDescriptor?.organizationName
            val jobName = searchResultItems.matchedObjectDescriptor?.positionTitle
            var jobCategory = ""
            searchResultItems.matchedObjectDescriptor?.jobCategory?.forEach {
                jobCategory += "${it.name}"
            }

            val qualificationSummary =
                searchResultItems.matchedObjectDescriptor?.qualificationSummary

            val publicationStartDate =
                searchResultItems.matchedObjectDescriptor?.publicationStartDate
            val applicationCloseDate =
                searchResultItems.matchedObjectDescriptor?.applicationCloseDate
            val jobMinimumRange =
                searchResultItems.matchedObjectDescriptor?.positionRemuneration?.get(0)?.minimumRange
            val jobMaximumRange =
                searchResultItems.matchedObjectDescriptor?.positionRemuneration?.get(0)?.maximumRange
            val jobRateIntervalCode =
                searchResultItems.matchedObjectDescriptor?.positionRemuneration?.get(0)?.rateIntervalCode

            val job = Job(
                id = jobId,
                applyUri = applyUri!!,
                locationName = locationName!!,
                country = countryCode!!,
                countrySubDivisionCode = countrySubDivisionCode!!,
                longitude = longitude!!,
                latitude = latitude!!,
                organizationName = organizationName!!,
                jobName = jobName!!,
                jobCategory = jobCategory,
                jobQualificationSummary = qualificationSummary!!,
                publicationStartDate = publicationStartDate!!,
                applicationCloseDate = applicationCloseDate!!,
                jobMinimumRange = jobMinimumRange!!,
                jobMaximumRange = jobMaximumRange!!,
                jobRateIntervalCode = jobRateIntervalCode!!
            )
            jobList.add(job)
        }

        return jobList
    }

    private fun hasNoPrefLocation(locationPrefName: String): Boolean {
        return locationPrefName == Constants.DEFAULT
    }

    private fun isPrefLocation(location: String?, locationPrefName: String): Boolean {
        if (location != null) {
            return location.contains(locationPrefName)
        }
        return false
    }

    private fun isUnitedStates(locationCountry: String?): Boolean {
        return locationCountry == Constants.COUNTRY_CODE
    }

    private fun verifyNull(string: String?): String {
        return string ?: ""
    }

}


