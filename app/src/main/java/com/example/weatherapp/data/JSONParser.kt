package com.example.weatherapp.data

import org.json.JSONObject

interface JSONWeatherParser {

    fun parseWeatherFromJson(json: String): Weather

    fun parseWeatherListFromJson(json: String, coordinates: WeatherCoordinates): List<Weather>

}


class JSONWeatherParserImpl : JSONWeatherParser {

    override fun parseWeatherFromJson(json: String): Weather {
        val mainObject = JSONObject(json)

        val coordinates = mainObject.getJSONObject("coord")
        val lat = coordinates.getDouble("lat")
        val lon = coordinates.getDouble("lon")
        val weatherCoordinates = WeatherCoordinates(lat, lon)
        return createWeather(mainObject, weatherCoordinates)
    }

    private fun createWeather(
        mainJsonObject: JSONObject,
        weatherCoordinates: WeatherCoordinates,
    ): Weather {
        val dt = mainJsonObject.getLong("dt") * 1000L

        val weather = mainJsonObject.getJSONArray("weather").getJSONObject(0)

        val group = weather.getString("main")
        val description = weather.getString("description")
        val iconID = weather.getString("icon")

        val main = mainJsonObject.getJSONObject("main")
        val temp = main.getDouble("temp")
        val feelsLikeTemp = main.getDouble("feels_like")
        val tempMin = main.getDouble("temp_min")
        val tempMax = main.getDouble("temp_max")
        val pressure = main.getInt("pressure")
        val humidity = main.getInt("humidity")

        val wind = mainJsonObject.getJSONObject("wind")
        val windSpeed = wind.getDouble("speed")
        val windDeg = wind.getInt("deg")

        val clouds = mainJsonObject.getJSONObject("clouds")
        val cloudiness = clouds.getInt("all")

        return Weather(
            dt = dt,
            coordinates = weatherCoordinates,
            group = group,
            description = description,
            temp = temp,
            feelsLikeTemp = feelsLikeTemp,
            tempMin = tempMin,
            tempMax = tempMax,
            iconID = iconID,
            pressure = pressure,
            humidity = humidity,
            windSpeed = windSpeed,
            windDeg = windDeg,
            cloudiness = cloudiness
        )
    }

    override fun parseWeatherListFromJson(
        json: String,
        coordinates: WeatherCoordinates,
    ): List<Weather> {
        val mainObject = JSONObject(json)
        val list = mainObject.getJSONArray("list")

        val weatherList = mutableListOf<Weather>()

        for (i in 0 until list.length()) {
            val listObject = list.getJSONObject(i)
            val weather = createWeather(listObject, coordinates)
            weatherList.add(weather)
        }

        return weatherList
    }

}