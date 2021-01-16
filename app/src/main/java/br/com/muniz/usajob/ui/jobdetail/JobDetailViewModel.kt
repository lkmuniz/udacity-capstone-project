package br.com.muniz.usajob.ui.jobdetail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.muniz.usajob.base.BaseViewModel

class JobDetailViewModel(application: Application) : BaseViewModel(application)  {

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(JobDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return JobDetailViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}