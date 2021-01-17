package br.com.muniz.usajob.ui.joblist

import android.app.Application
import androidx.lifecycle.*
import br.com.muniz.usajob.base.BaseViewModel
import br.com.muniz.usajob.data.Job
import br.com.muniz.usajob.data.local.getDatabase
import br.com.muniz.usajob.data.repository.JobRepository
import br.com.muniz.usajob.utils.DataState
import br.com.muniz.usajob.utils.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JobListViewModel(application: Application) : BaseViewModel(application) {

    private var _resultJob = MutableLiveData<List<Job>>()
    val resultJob: LiveData<List<Job>> = _resultJob

    private val jobRepository: JobRepository by lazy {
        val database = getDatabase(getApplication())
        JobRepository(database)
    }

    init {
        configViewModel()
    }

    private fun refreshJobs() {
        viewModelScope.launch {
            jobRepository.refreshJobs().collect { state ->
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

    fun configViewModel() {
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

    fun clearAndRefreshDataBase(){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                jobRepository.clearAndRefreshDatabase()
            }
        }
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