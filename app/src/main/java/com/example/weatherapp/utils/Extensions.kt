package com.example.weatherapp.utils

import com.example.weatherapp.data.Weather
import com.example.weatherapp.data.room.WeatherEntity

fun Weather.toEntity(index: Int) = WeatherEntity(
    id = index,
    dt = dt,
    lat = coordinates.lat,
    lon = coordinates.lon,
    group = group,
    description = description,
    temp = temp,
    temp_f_l = feelsLikeTemp,
    temp_min = tempMin,
    temp_max = tempMax,
    icon_id = iconID,
    pressure = pressure,
    humidity = humidity,
    w_speed = windSpeed,
    w_deg = windDeg,
    cloudiness = cloudiness
)