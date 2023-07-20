package com.example.weatherapp.data

data class AverageDayOfWeek(
    val dt: Long,
    val name: String,
    val temp: Double,
    val feelLikeTemp: Double,
    val iconID: String,
)