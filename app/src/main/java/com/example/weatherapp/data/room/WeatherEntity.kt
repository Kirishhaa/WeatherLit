package com.example.weatherapp.data.room

import androidx.room.Entity
import com.example.weatherapp.data.Weather
import com.example.weatherapp.data.WeatherCoordinates

@Entity(
    tableName = "weather",
    primaryKeys = [
        "id"
    ]
)
data class WeatherEntity(
    val id: Int,
    val dt: Long,
    val lat: Double,
    val lon: Double,
    val group: String,
    val description: String,
    val temp: Double,
    val temp_f_l: Double,
    val temp_min: Double,
    val temp_max: Double,
    val icon_id: String,
    val pressure: Int,
    val humidity: Int,
    val w_speed: Double,
    val w_deg: Int,
    val cloudiness: Int,
) {

    fun toWeather() = Weather(
        dt = dt,
        coordinates = WeatherCoordinates(lat, lon),
        group = group,
        description = description,
        temp = temp,
        feelsLikeTemp = temp_f_l,
        tempMin = temp_min,
        tempMax = temp_max,
        iconID = icon_id,
        pressure = pressure,
        humidity = humidity,
        windSpeed = w_speed,
        windDeg = w_deg,
        cloudiness = cloudiness
    )

}