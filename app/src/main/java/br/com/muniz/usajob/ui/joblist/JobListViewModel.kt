package br.com.muniz.usajob.ui.joblist

import android.app.Application
import androidx.lifecycle.*
import br.com.muniz.usajob.Constants
import br.com.muniz.usajob.base.BaseViewModel
import br.com.muniz.usajob.data.Job
import br.com.muniz.usajob.data.repository.JobRepository
import br.com.muniz.usajob.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JobListViewModel(
    application: Application,
    private val jobRepository: JobRepository
) :
    BaseViewModel(application) {

    private var sharedPreferenceHelper = PreferenceHelper(application)

    private var _resultJob = MutableLiveData<List<Job>>()
    val resultJob: LiveData<List<Job>> = _resultJob

    private var _resultSubdivision = MutableLiveData<List<String>>()
    val resultSubdivision: LiveData<List<String>> = _resultSubdivision

    init {
        getSubdivisions()
        configViewModel()
    }

    private fun refreshJobs(keyword: String) {
        viewModelScope.launch {
            val location = getPrefLocation()
            jobRepository.refreshRepositoryJobs(
                locationName = location,
                keyword = keyword
            ).collect { state ->
                when (state) {
                    DataState.Loading -> {
                        showLoading.value = true
                    }
                    DataState.Error -> {
                        showLoading.value = false
                    }
                    DataState.Success -> {
                        showLoading.value = false
                    }
                }
            }
        }
    }

    private fun refreshSubdivision() {
        viewModelScope.launch {
            jobRepository.refreshSubdivision().collect { state ->
                when (state) {
                    DataState.Loading -> {
                        showLoading.value = true
                    }
                    DataState.Error -> {
                        showLoading.value = false
                    }
                    DataState.Success -> {
                        showLoading.value = false
                    }
                }
            }
        }
    }

    private fun configViewModel(keyword: String = "") {
        configDefaultPreference()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                jobRepository.jobLocal.map {
                    it.asDomainModel()
                }.collect {
                    if (it.isNotEmpty()) {
                        _resultJob.postValue(it)
                    } else {
                        refreshJobs(keyword)
                    }
                }
            }
        }
    }

    private fun configDefaultPreference() {
        sharedPreferenceHelper.setValue(
            Constants.PAGE_NUMBER_PREF_KEY,
            Constants.PAGE_NUMBER,
            TypeValue.STRING
        )
        sharedPreferenceHelper.setValue(
            Constants.RESULT_PER_PAGE_PREF_KEY,
            Constants.RESULT_PER_PAGE,
            TypeValue.STRING
        )
    }

    fun clearAndRefreshDataBase(keyword: String = "") {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                refreshJobs(keyword)
            }
        }
    }

    private fun getSubdivisions() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                jobRepository.subdivisionList.map {
                    it.subdivisionAsNameList()
                }.collect {
                    if (it.isNotEmpty()) {
                        _resultSubdivision.postValue(it)
                    } else {
                        refreshSubdivision()
                    }
                }
            }
        }
    }

    fun saveLocationPreference(value: String?) {
        sharedPreferenceHelper.setValue(
            Constants.LOCATION_PREF_KEY,
            value!!,
            TypeValue.STRING
        )
        clearAndRefreshDataBase()
    }

    private fun clearSharedPreference() {
        sharedPreferenceHelper.clear()
    }

    private fun clearDataBase() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                jobRepository.clearJobRepository()
            }
        }
    }

    fun getPrefLocation(): String {
        return sharedPreferenceHelper.getValue(Constants.LOCATION_PREF_KEY, TypeValue.STRING)
            .toString()
    }

    fun logoutFlow() {
        clearSharedPreference()
        clearDataBase()
    }

}