package com.example.weatherapp.data

import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.util.createCancellationSignal
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.weatherapp.App
import com.example.weatherapp.R
import com.example.weatherapp.domain.LocationController
import com.example.weatherapp.domain.PermissionsController
import com.example.weatherapp.domain.WeatherRepository
import com.example.weatherapp.domain.WeatherService
import com.example.weatherapp.utils.ErrorResult
import com.example.weatherapp.utils.SuccessfulResult
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.combine
import java.util.*

class LoadWorker(
    applicationContext: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(applicationContext, workerParameters) {

    private var currentProgress = 0

    companion object {
        const val PROGRESS = "progress"
        const val REQUESTED_SUCCESSFULLY = "REQUESTED_SUCCESSFULLY"
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val app = applicationContext as App
        toNextProgress()
        val coord = getUsersCoordinates()
        Log.d("LoadWorker", "coordinates = $coord")
        toNextProgress()
        val weatherService: WeatherService = WeatherServiceImpl(
            httpURLCreator = HttpURLCreatorImpl(),
            jsonParser = JSONWeatherParserImpl(),
        )
        toNextProgress()
        val repo: WeatherRepository = app.weatherRepository
        toNextProgress()
        toNextProgress()
        val name = getLocationName(coord)

        toNextProgress()
        toNextProgress()
        val currentWeather = weatherService.getCurrentWeather(coord)

        if (currentWeather is ErrorResult) {
            updateProgressTo(100)
            Log.d("LoadWorker", "currentWeather error")
            return@withContext Result.failure()
        }

        toNextProgress()
        val forecastedWeather = weatherService.getForecastedWeather(coord)

        if (forecastedWeather is ErrorResult) {
            updateProgressTo(100)
            Log.d("LoadWorker", "forecastedWeather error")
            return@withContext Result.failure()
        }

        val a = 5
        setProgress(workDataOf(REQUESTED_SUCCESSFULLY to true))

        toNextProgress()
        try {
            toNextProgress()
            val currentWeatherValue = (currentWeather as SuccessfulResult).data
            toNextProgress()
            repo.insertCurrentWeather(currentWeatherValue)
            toNextProgress()
            val forecastedWeatherValue = (forecastedWeather as SuccessfulResult).data
            toNextProgress()
            repo.insertForecastedWeather(forecastedWeatherValue)
            toNextProgress()
            app.saveUsersLocationName(name)
            if(coord!=LocationControllerImpl.BASE_COORDINATES) {
                app.saveUsersCoordinates(coord)
            }
        } catch (e: Exception) {
            updateProgressTo(100)
            return@withContext Result.failure()
        }

        updateProgressTo(100)
        return@withContext Result.success()
    }

    private suspend fun getUsersCoordinates(): WeatherCoordinates {

        val app = applicationContext as App
        val userCoordinates = app.getUsersCoordinates()

        val locationManager =
            applicationContext.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        val locationController: LocationController = LocationControllerImpl(locationManager)

        val permissionsController: PermissionsController =
            PermissionsControllerImpl(applicationContext)
        var coordinates: WeatherCoordinates? = null

        val enabledAndUserCoord = combine(
            locationController.getUserCoordinates(),
            locationController.isEnabled(),
            permissionsController.isAvailableAllPermissions()
        ) { coord, enabled, availablePerms ->
            if (availablePerms) locationController.bindLocationManager()
            if (coord != null && enabled && availablePerms) return@combine coord else return@combine null
        }

        val mainJob = CoroutineScope(Dispatchers.IO).launch {
            val checked = launch(start = CoroutineStart.LAZY) {
                delay(5000)
                coordinates = userCoordinates ?: LocationControllerImpl.BASE_COORDINATES
            }
            checked.invokeOnCompletion { if (!checked.isCancelled) cancel() }
            enabledAndUserCoord.collect { weatherCoordinates ->
                if (weatherCoordinates == null) {
                    checked.start()
                } else {
                    checked.cancel()
                    coordinates = weatherCoordinates
                    cancel()
                    return@collect
                }
            }
        }

        SupervisorJob(mainJob)
        mainJob.join()

        locationController.unbindLocationManager()
        return coordinates!!
    }

    private fun getLocationName(coordinates: WeatherCoordinates): String {
        val unknownLocationValue = applicationContext.resources.getString(R.string.unknown_location)
        return try {
            val (lat, lon) = coordinates
            val geo = Geocoder(this.applicationContext, Locale.getDefault())
            val addresses = geo.getFromLocation(lat, lon, 10)
            if (addresses == null || addresses.isEmpty()) {
                unknownLocationValue
            } else {
                addresses[0].locality ?: unknownLocationValue
            }
        } catch (e: Exception) {
            unknownLocationValue
        }
    }


    private suspend fun toNextProgress() {
        updateProgressTo(currentProgress)
        currentProgress += 7
    }

    private suspend fun updateProgressTo(progress: Int) =
        setProgress(workDataOf(PROGRESS to progress))

}