package com.example.weatherapp.data

import android.annotation.SuppressLint
import android.location.LocationListener
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.weatherapp.domain.LocationController
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class LocationControllerImpl(
    private val locationManager: LocationManager,
    minTimeMs: Long? = null,
    minDistanceM: Float? = null,
) : LocationController {

    private val _minTimeMs = minTimeMs ?: MIN_TIME_LOCATION_MS

    private val _minDistanceM = minDistanceM ?: MIN_LOCATION_DISTANCE_M

    companion object {

        val BASE_COORDINATES = WeatherCoordinates(0.0, 0.0)

        private const val MIN_TIME_LOCATION_MS = 0L

        private const val MIN_LOCATION_DISTANCE_M = 0f
    }

    private val networkProvider = LocationManager.NETWORK_PROVIDER
    private val gpsProvider = LocationManager.GPS_PROVIDER

    private var longitude: Double? = null
    private var latitude: Double? = null

    private val currentWeatherCoord = MutableStateFlow<WeatherCoordinates?>(null)

    private val locationListener = LocationListener { location ->
        latitude = location.latitude
        longitude = location.longitude
        currentWeatherCoord.value = WeatherCoordinates(location.latitude, location.longitude)
    }

    @SuppressLint("MissingPermission")
    override fun bindLocationManager() {
        Log.d("LocationControllerImpl", "bound")
        Handler(Looper.getMainLooper()).post {
            locationManager.requestLocationUpdates(
                networkProvider,
                _minTimeMs,
                _minDistanceM,
                locationListener
            )
            locationManager.requestLocationUpdates(
                gpsProvider,
                _minTimeMs,
                _minDistanceM,
                locationListener
            )
        }
    }

    override fun unbindLocationManager() {
        locationManager.removeUpdates(locationListener)
    }

    @SuppressLint("MissingPermission")
    override fun getUserCoordinates(): Flow<WeatherCoordinates?> = flow {
        coroutineScope {
            while (true) {
                val coordinates = currentWeatherCoord
                if (coordinates.value == null) {
                    try {
                        val location = locationManager.getLastKnownLocation(networkProvider)
                        if (location == null) {
                            emit(null)
                        } else {
                            emit(WeatherCoordinates(location.latitude, location.longitude))
                        }
                    } catch (e: Exception) {
                        emit(null)
                    }
                } else {
                    emit(coordinates.value)
                }
                delay(200)
            }
        }
    }

    override fun getBaseCoordinates(): WeatherCoordinates = BASE_COORDINATES

    override fun isEnabled(): Flow<Boolean> = flow {

        coroutineScope {
            var isEnable =
                locationManager.isProviderEnabled(networkProvider) || locationManager.isProviderEnabled(
                    gpsProvider
                )
            emit(isEnable)
            while (true) {
                delay(100)
                val enable =
                    locationManager.isProviderEnabled(networkProvider) || locationManager.isProviderEnabled(
                        gpsProvider
                    )
                if (isEnable != enable) {
                    isEnable = enable
                    emit(isEnable)
                }
            }
        }
    }

}