package com.example.weatherapp.data

import com.example.weatherapp.utils.TOKEN

interface HttpURLCreator {

    fun createWeatherURL(
        coordinates: WeatherCoordinates,
        lang: String? = null,
        units: String? = "metric",
    ): String

    fun createWeatherURL(
        lat: Double,
        lon: Double,
        lang: String? = null,
        units: String? = "metric",
    ): String

    fun createForecastURL(
        coordinates: WeatherCoordinates,
        lang: String? = null,
        units: String? = "metric",
    ): String

    fun createForecastURL(
        lat: Double,
        lon: Double,
        lang: String? = null,
        units: String? = "metric",
    ): String

}

class HttpURLCreatorImpl : HttpURLCreator {

    override fun createWeatherURL(
        coordinates: WeatherCoordinates,
        lang: String?,
        units: String?,
    ): String {
        return createWeatherURL(coordinates.lat, coordinates.lon, lang, units)
    }

    override fun createWeatherURL(lat: Double, lon: Double, lang: String?, units: String?): String {
        val basePath =
            StringBuilder("https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$TOKEN")
        if (lang != null) {
            basePath.append("&lang=$lang")
        }
        if (units != null) {
            basePath.append("&units=$units")
        }
        return basePath.toString()
    }

    override fun createForecastURL(
        coordinates: WeatherCoordinates,
        lang: String?,
        units: String?,
    ): String {
        return createForecastURL(coordinates.lat, coordinates.lon, lang, units)
    }

    override fun createForecastURL(
        lat: Double,
        lon: Double,
        lang: String?,
        units: String?,
    ): String {
        val basePath =
            StringBuilder("https://api.openweathermap.org/data/2.5/forecast?lat=$lat&lon=$lon&appid=$TOKEN")
        if (lang != null) {
            basePath.append("&lang=$lang")
        }
        if (units != null) {
            basePath.append("&units=$units")
        }
        return basePath.toString()
    }

}