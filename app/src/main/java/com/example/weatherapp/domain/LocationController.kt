package com.example.weatherapp.domain

import com.example.weatherapp.data.WeatherCoordinates
import kotlinx.coroutines.flow.Flow

interface LocationController {

    fun bindLocationManager()

    fun unbindLocationManager()

    fun getUserCoordinates(): Flow<WeatherCoordinates?>

    fun getBaseCoordinates(): WeatherCoordinates

    fun isEnabled(): Flow<Boolean>

}