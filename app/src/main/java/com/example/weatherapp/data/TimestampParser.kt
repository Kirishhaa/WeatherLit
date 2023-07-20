package com.example.weatherapp.data

import android.text.format.DateFormat

class TimestampParser {

    fun getSecondsFromTimestamp(timestamp: Long): Long {
        val date = DateFormat.format("d.M.yyyy H:m:s", timestamp)
        val (_date, _time) = date.toString().split(" ")
        val (day, month, year) = _date.split(".").map { it.toInt() }
        val (hour, minute, second) = _time.split(":").map { it.toInt() }

        val minuteSeconds = minute * 60L
        val hourSeconds = hour * 3600L
        val daySeconds = day * 24L * 3600L
        val montSeconds = month * 30L * 24L * 3600L
        val yearSeconds = year * 365L * 30L * 24L * 3600L

        return second + minuteSeconds + hourSeconds + daySeconds + montSeconds + yearSeconds
    }

    fun getMinutesFromTimestamp(timestamp: Long): Long = getSecondsFromTimestamp(timestamp) / 60

    fun getHoursFromTimestamp(timestamp: Long): Long = getMinutesFromTimestamp(timestamp) / 60
}