package br.com.muniz.usajob.ui.joblist

import android.app.Application
import androidx.lifecycle.*
import br.com.muniz.usajob.Constants
import br.com.muniz.usajob.Constants.BASE_IMAGE_URL
import br.com.muniz.usajob.base.BaseViewModel
import br.com.muniz.usajob.data.Job
import br.com.muniz.usajob.data.local.getDatabase
import br.com.muniz.usajob.data.repository.JobRepository
import br.com.muniz.usajob.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JobListViewModel(application: Application) : BaseViewModel(application) {

    private var sharedPreferenceHelper = PreferenceHelper(application)

    private var _resultJob = MutableLiveData<List<Job>>()
    val resultJob: LiveData<List<Job>> = _resultJob

    private var _resultSubdivision = MutableLiveData<List<String>>()
    val resultSubdivision: LiveData<List<String>> = _resultSubdivision

    private var _imageUrl = MutableLiveData(BASE_IMAGE_URL)
    val imageUrl: LiveData<String> = _imageUrl

    private val jobRepository: JobRepository by lazy {
        val database = getDatabase(getApplication())
        JobRepository(database)
    }

    init {
        getSubdivisions()
        configViewModel()
    }

    private fun refreshJobs() {
        viewModelScope.launch {
            val location = getPrefLocation()
            jobRepository.refreshJobs(location).collect { state ->
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

    private fun configViewModel() {
        configDefaultPreference()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                jobRepository.jobLocal.map {
                    it.asDomainModel()
                }.collect {
                    if (it.isNotEmpty()) {
                        _resultJob.postValue(it)
                    } else {
                        refreshJobs()
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

    fun clearAndRefreshDataBase() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                jobRepository.clearJobRepository()
                configViewModel()
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

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(JobListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return JobListViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}