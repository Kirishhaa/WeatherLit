package com.example.weatherapp.data

import android.util.Log
import com.example.weatherapp.domain.WeatherService
import com.example.weatherapp.utils.ErrorResult
import com.example.weatherapp.utils.FinalResult
import com.example.weatherapp.utils.SuccessfulResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

class WeatherServiceImpl(
    private val httpURLCreator: HttpURLCreator,
    private val jsonParser: JSONWeatherParser,
) : WeatherService {

    private val loggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    override suspend fun getCurrentWeather(coordinates: WeatherCoordinates): FinalResult<Weather> =
        withContext(Dispatchers.IO) {
            Log.d("WeatherServiceImpl", "getCurrentWeather")
            try {
                val url = httpURLCreator.createWeatherURL(coordinates)
                val request = Request.Builder()
                    .url(url)
                    .get()
                    .build()
                val call = client.newCall(request)
                val response = call.execute()
                if (response.isSuccessful && call.isExecuted()) {
                    val jsonBody = response.body!!.string()
                    val weather = jsonParser.parseWeatherFromJson(jsonBody)
                    return@withContext SuccessfulResult(weather)
                } else {
                    return@withContext ErrorResult(IllegalStateException())
                }
            } catch (e: Exception) {
                return@withContext ErrorResult(IllegalStateException())
            }
        }

    override suspend fun getForecastedWeather(coordinates: WeatherCoordinates): FinalResult<List<Weather>> =
        withContext(Dispatchers.IO) {
            Log.d("WeatherServiceImpl", "getForecastedWeather")
            try {
                val url = httpURLCreator.createForecastURL(coordinates)
                val request = Request.Builder()
                    .url(url)
                    .get()
                    .build()
                val call = client.newCall(request)
                val response = call.execute()
                if (response.isSuccessful && call.isExecuted()) {
                    val jsonBody = response.body!!.string()
                    val weatherList = jsonParser.parseWeatherListFromJson(jsonBody, coordinates)
                    return@withContext SuccessfulResult(weatherList)
                } else {
                    return@withContext ErrorResult(IllegalStateException())
                }
            } catch (e: Exception) {
                return@withContext ErrorResult(IllegalStateException())
            }
        }
}