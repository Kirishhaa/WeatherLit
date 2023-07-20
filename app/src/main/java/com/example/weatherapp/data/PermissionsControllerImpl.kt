package com.example.weatherapp.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.example.weatherapp.domain.PermissionsController
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class PermissionsControllerImpl(
    private val applicationContext: Context,
) : PermissionsController {

    private fun checkPermission(name: String) = ContextCompat.checkSelfPermission(
        applicationContext,
        name
    ) == PackageManager.PERMISSION_GRANTED

    override fun isLocationAvailable(): Boolean =
        checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) &&
                checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)

    override fun isNetworkAvailable(): Boolean = checkPermission(Manifest.permission.INTERNET)
            && checkPermission(Manifest.permission.ACCESS_NETWORK_STATE)

    override fun isAvailableAllPermissions(): Flow<Boolean> = flow {
        coroutineScope {
            var isEnable = isLocationAvailable() && isNetworkAvailable()
            emit(isEnable)
            while (true) {
                delay(100)
                val enable = isLocationAvailable() && isNetworkAvailable()
                if (isEnable != enable) {
                    isEnable = enable
                    emit(isEnable)
                }
            }
        }
    }

}