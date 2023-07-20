package com.example.weatherapp.domain

import kotlinx.coroutines.flow.Flow

interface PermissionsController {

    fun isLocationAvailable(): Boolean

    fun isNetworkAvailable(): Boolean

    fun isAvailableAllPermissions(): Flow<Boolean>

}