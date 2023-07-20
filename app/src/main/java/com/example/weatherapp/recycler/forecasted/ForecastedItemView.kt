package com.example.weatherapp.recycler.forecasted

interface ForecastedItemView {

    fun setIcon(iconRes: Int)

    fun setDayOfWeek(value: String)

    fun setDate(date: String)

    fun setTemp(temp: Int)

    fun setFeelLikeTemp(temp: Int)

    fun setDescription(description: String)

    fun setTime(time: String)

    fun setColor(color: Int)

}