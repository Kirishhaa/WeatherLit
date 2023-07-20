package com.example.weatherapp.domain

import com.example.weatherapp.data.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun insertForecastedWeather(list: List<Weather>)

    suspend fun insertCurrentWeather(weather: Weather)

    fun getForecastedWeather(): Flow<List<Weather>>

    fun getCurrentWeather(): Flow<Weather?>

}