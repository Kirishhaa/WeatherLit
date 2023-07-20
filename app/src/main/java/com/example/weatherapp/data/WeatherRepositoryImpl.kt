package com.example.weatherapp.data

import android.util.Log
import com.example.weatherapp.domain.WeatherRepository
import com.example.weatherapp.domain.room.WeathersDao
import com.example.weatherapp.utils.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class WeatherRepositoryImpl(
    private val weathersDao: WeathersDao,
) : WeatherRepository {

    private val currentIndex = 1

    override suspend fun insertForecastedWeather(list: List<Weather>) =
        withContext(Dispatchers.IO) {
            val listEntity =
                list.mapIndexed { index, weather -> weather.toEntity(currentIndex + index + 1) }
            weathersDao.insertForecastedWeather(listEntity)
        }

    override suspend fun insertCurrentWeather(weather: Weather) = withContext(Dispatchers.IO) {
        val entityWeather = weather.toEntity(currentIndex)
        weathersDao.insertCurrentWeather(entityWeather)
    }

    override fun getForecastedWeather(): Flow<List<Weather>> {
        Log.d("WeatherRepository", "getForecastedWeather")
        return weathersDao.getForecastedWeather()
            .map { list -> list.map { it.toWeather() } }.flowOn(Dispatchers.IO)
    }

    override fun getCurrentWeather(): Flow<Weather?> {
        Log.d("WeatherRepository", "getCurrentWeather")
        return weathersDao.getCurrentWeather()
            .map { it?.toWeather() }.flowOn(Dispatchers.IO)
    }

}