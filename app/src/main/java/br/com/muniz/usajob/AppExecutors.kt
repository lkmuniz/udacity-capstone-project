package br.com.muniz.usajob

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

open class AppExecutors(
    private val diskIO: Executor,
    private val networkIO: Executor,
    private val mainThread: Executor = MainThreadExecutor()
) {

//    constructor() : this(
//        Executors.newSingleThreadExecutor(),
//        Executors.newFixedThreadPool(3),
//        MainThreadExecutor()
//    )

    fun diskIO(): Executor {
        return diskIO
    }

    fun networkIO(): Executor {
        return networkIO
    }

    fun mainThread(): Executor {
        return mainThread
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}
