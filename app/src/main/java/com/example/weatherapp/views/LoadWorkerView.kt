package com.example.weatherapp.views

import androidx.work.WorkInfo

interface LoadWorkerView {

    fun onStartLoad()

    fun setProgress(progress: Int)

    fun onFinishLoad(result: com.example.weatherapp.utils.FinalResult<WorkInfo>)

    fun isNotOnline()

    fun doOnIsntTime()

    fun runPermissionsLauncher()

    fun runLocationLauncher()

    fun onPermissionsDisabled()

    fun onLocationDisabled()

}