package com.theskillapp.skillapp

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

fun setupThreads() {
    ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
        override fun executeOnDiskIO(runnable: Runnable) = runnable.run()
        override fun postToMainThread(runnable: Runnable) = runnable.run()
        override fun isMainThread() = true
    })

    Dispatchers.setMain(Dispatchers.Default)
}

fun resetThreads() {
    ArchTaskExecutor.getInstance().setDelegate(null)
    Dispatchers.resetMain()
}
