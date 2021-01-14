package br.com.muniz.usajob.ui.joblist

import android.app.Application
import androidx.lifecycle.*
import br.com.muniz.usajob.base.BaseViewModel
import br.com.muniz.usajob.data.Job
import br.com.muniz.usajob.data.local.getDatabase
import br.com.muniz.usajob.data.remote.Network
import br.com.muniz.usajob.data.repository.JobRepository
import br.com.muniz.usajob.utils.DataState
import br.com.muniz.usajob.utils.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class JobListViewModel(application: Application) : BaseViewModel(application){

    private var _resultJob = MutableLiveData<List<Job>>()
    val resultJob: LiveData<List<Job>> = _resultJob

    private var _statusLoading = MutableLiveData<Boolean>()
    val statusLoading: LiveData<Boolean> = _statusLoading

    private val jobRepository: JobRepository by lazy {
        val database = getDatabase(getApplication())
        JobRepository(database)
    }

    init {
        viewModelScope.launch {
            jobRepository.jobLocal.map {
                it.asDomainModel()
            }.collect {
                if(it.isNotEmpty()) {
                    _resultJob.postValue(it)
                } else {
                    refreshJobs()
                }
            }
        }
    }

    fun refreshJobs() {
        viewModelScope.launch {
            jobRepository.refreshJobs().collect { state ->
                when(state) {
                    DataState.Loading -> {
                        _statusLoading.value = true
                    }
                    DataState.Error -> {
                        _statusLoading.value = false
                    }
                    DataState.Success -> {
                        _statusLoading.value = false
                    }
                }
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