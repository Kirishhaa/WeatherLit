package com.example.weatherapp.presenters

import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asFlow
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.weatherapp.App
import com.example.weatherapp.data.LoadWorker
import com.example.weatherapp.data.LocationControllerImpl
import com.example.weatherapp.data.PermissionsControllerImpl
import com.example.weatherapp.data.TimestampParser
import com.example.weatherapp.presenters.base.BasePresenter
import com.example.weatherapp.utils.*
import com.example.weatherapp.views.LoadWorkerView
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import java.util.concurrent.atomic.AtomicBoolean

abstract class SynchronizePresenter<V : LoadWorkerView, M>
    (protected val applicationContext: Context) : BasePresenter<V, M>() {

    companion object {
        private const val CHECKABLE_HOURS_TIME = 3
    }

    private var requestedLocation = false
    private var requestedPerms = false

    private var isRequestSuccessful = false

    private val isSynchronizing = AtomicBoolean(false)

    private var synchronizeJob: Job? = null

    fun sync(userUpdate: Boolean) {
        if (!isSynchronizing.getAndSet(true)) {
            Log.d("SynchronizePresenter", "sync")
            val handler = Handler(Looper.getMainLooper())
            val workManager = WorkManager.getInstance(applicationContext)
            val app = (applicationContext as App)
            val lastTimestamp = app.getUpdateTimestamp()
            val currentTimestamp = System.currentTimeMillis()
            val parser = TimestampParser()
            val lastHours = parser.getHoursFromTimestamp(lastTimestamp)
            val curHours = parser.getHoursFromTimestamp(currentTimestamp)
            synchronizeJob = CoroutineScope(Dispatchers.IO).launch {
                if (curHours - lastHours >= CHECKABLE_HOURS_TIME || (userUpdate && app.getUsersCoordinates() == null)) {
                    val locationManager =
                        applicationContext.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
                    val locationController = LocationControllerImpl(locationManager)
                    val permissionsController = PermissionsControllerImpl(applicationContext)
                    val isFirstLaunch = app.getIsFirstLaunch()
                    combine(
                        isOnline(),
                        locationController.isEnabled(),
                        permissionsController.isAvailableAllPermissions()
                    ) { online, enabled, availablePerms ->
                        return@combine Triple(online, enabled, availablePerms)
                    }.collect {
                        val online = it.first
                        val enabled = it.second
                        val availablePerms = it.third
                        Log.d("SynchronizePresenter", "collected")
                        if (online) {
                            if (enabled) {
                                if (availablePerms) {
                                    runWorker(app, workManager, handler, currentTimestamp)
                                } else {
                                    if (userUpdate) {
                                        getView()?.runPermissionsLauncher()
                                        tryToCancelWork(workManager)

                                        cancel()
                                        return@collect
                                    } else if (isFirstLaunch) {
                                        if (requestedPerms) {
                                            Log.d("SynchronizePresenter", "runWorker")
                                            runWorker(app, workManager, handler, currentTimestamp)
                                        } else {
                                            Log.d("SynchronizePresenter", "runPermLauncher")
                                            requestedPerms = true
                                            getView()?.runPermissionsLauncher()
                                            tryToCancelWork(workManager)
                                            cancel()
                                            return@collect
                                        }
                                    } else {
                                        Log.d("SynchronizePresenter", "else branch permissions")
                                        waitingAfterPressed()
                                        cancel()
                                        return@collect
                                    }
                                }
                            } else {
                                if (userUpdate) {
                                    getView()?.runLocationLauncher()
                                    tryToCancelWork(workManager)
                                    waitingAfterPressed()
                                    cancel()
                                    return@collect
                                } else if (isFirstLaunch) {
                                    if (requestedLocation) {
                                        runWorker(app, workManager, handler, currentTimestamp)
                                    } else {
                                        requestedLocation = true
                                        getView()?.runLocationLauncher()
                                        tryToCancelWork(workManager)
                                        cancel()
                                        return@collect
                                    }
                                } else {
                                    Log.d("SynchronizePresenter", "else branch location")
                                    waitingAfterPressed()
                                    cancel()
                                    return@collect
                                }
                            }
                        } else {
                            handler.post { getView()?.isNotOnline() }
                            tryToCancelWork(workManager)
                            waitingAfterPressed()
                            cancel()
                            return@collect
                        }
                    }
                } else {
                    handler.post { getView()?.doOnIsntTime() }
                    tryToCancelWork(workManager)
                    waitingAfterPressed()
                    cancel()
                }
            }
            synchronizeJob?.invokeOnCompletion {
                Log.d("SynchronizePresenter", "completion")
                isSynchronizing.set(false)
            }
        }
    }

    private fun tryToCancelWork(workManager: WorkManager) {
        if (!isRequestSuccessful) {
            Log.d("SynchronizePresenter", "cancel work")
            workManager.cancelAllWork()
        }
    }

    private suspend fun waitingAfterPressed() {
        delay(3000)
    }

    private fun runWorker(
        app: App,
        workManager: WorkManager,
        handler: Handler,
        timestamp: Long,
    ) {

        val oneTimeWorker = OneTimeWorkRequestBuilder<LoadWorker>().build()
        workManager.enqueue(oneTimeWorker)
        handler.post {
            getView()?.onStartLoad()
        }
        var result: FinalResult<WorkInfo>? = null
        CoroutineScope(Dispatchers.IO).launch {
            workManager.getWorkInfoByIdLiveData(oneTimeWorker.id).asFlow().collect {
                val progress = it.progress.getInt(LoadWorker.PROGRESS, 0)
                when (it.state) {
                    WorkInfo.State.RUNNING -> {
                        isRequestSuccessful =
                            it.progress.getBoolean(LoadWorker.REQUESTED_SUCCESSFULLY, false)
                    }
                    WorkInfo.State.SUCCEEDED -> {
                        Log.d("WorkerPresenter", "WorkInfoState Succeeded")
                        result = SuccessfulResult(it)
                        app.setUpdateTimestamp(timestamp)
                        isRequestSuccessful = false
                        cancel()
                        return@collect
                    }
                    WorkInfo.State.FAILED -> {
                        Log.d("WorkerPresenter", "WorkInfoState Failed")
                        result = ErrorResult(FailedWork())
                        isRequestSuccessful = false
                        cancel()
                        return@collect
                    }
                    WorkInfo.State.CANCELLED -> {
                        Log.d("WorkerPresenter", "WorkInfoState Cancelled")
                        result = ErrorResult(CancelledWork())
                        isRequestSuccessful = false
                        cancel()
                        return@collect
                    }
                    WorkInfo.State.BLOCKED -> {
                        Log.d("WorkerPresenter", "WorkInfoState Blocked")
                        result = ErrorResult(CancelledWork())
                        isRequestSuccessful = false
                        cancel()
                        return@collect
                    }
                    else -> handler.post { getView()?.setProgress(progress) }
                }
            }
        }.invokeOnCompletion {
            resetWorkerData(handler, result!!)
        }
    }

    private fun resetWorkerData(handler: Handler, result: FinalResult<WorkInfo>) {
        handler.post { getView()?.setProgress(100) }
        handler.post { getView()?.onFinishLoad(result) }
        isSynchronizing.set(false)
        synchronizeJob?.cancel()
    }


    private fun isOnline(): Flow<Boolean> = flow {
        val connectivityService =
            applicationContext.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityService.activeNetwork
        if (network == null) emit(false) else {
            val activeNetwork = connectivityService.getNetworkCapabilities(network)
            if (activeNetwork == null) emit(false) else {
                when {
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> emit(true)
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> emit(true)
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> emit(true)
                    else -> emit(false)
                }
            }
        }
    }

}