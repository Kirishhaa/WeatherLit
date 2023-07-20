package com.example.weatherapp.views.forecastedfragment

import com.example.weatherapp.data.ForecastedWeatherItem

interface ForecastedListView {

    fun setForecastedList(forecasted: List<ForecastedWeatherItem>)
}