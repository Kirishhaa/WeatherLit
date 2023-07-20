package com.example.weatherapp.domain

import com.example.weatherapp.data.Weather
import com.example.weatherapp.data.WeatherCoordinates
import com.example.weatherapp.utils.FinalResult

interface WeatherService {

    suspend fun getCurrentWeather(coordinates: WeatherCoordinates): FinalResult<Weather>

    suspend fun getForecastedWeather(coordinates: WeatherCoordinates): FinalResult<List<Weather>>

}