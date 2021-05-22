package br.com.muniz.usajob

import android.app.Application
import androidx.work.*
import br.com.muniz.usajob.data.local.getDatabase
import br.com.muniz.usajob.data.repository.JobRepository
import br.com.muniz.usajob.ui.jobdetail.JobDetailViewModel
import br.com.muniz.usajob.ui.joblist.JobListViewModel
import br.com.muniz.usajob.work.RefreshDataWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MyApp : Application() {

    val applicationScope = CoroutineScope(Dispatchers.Default)

    val appModule = module {

        single { JobRepository(getDatabase(this@MyApp)) }

        single {
            AppExecutors(
                Executors.newSingleThreadExecutor(),
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1)
            )
        }

        viewModel {
            JobListViewModel(this@MyApp, get())
        }

        viewModel {
            JobDetailViewModel(this@MyApp)
        }

    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@MyApp)
            modules(listOf(appModule))
        }

        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWork>(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            Constants.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
}
