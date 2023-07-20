package com.example.weatherapp.views.mainfragment

import com.example.weatherapp.data.AdditionalInfo
import com.example.weatherapp.data.AverageDayOfWeek
import com.example.weatherapp.data.ForecastedWeatherItem
import com.example.weatherapp.views.LoadWorkerView

interface MainView : LoadWorkerView{

    fun setDegreeLabel(label: String)

    fun setTypeWeatherLabel(label: String)

    fun goToForecastedWeather(forecasted: List<ForecastedWeatherItem>, scrolledPosition: Int = 0)

    fun setDaysOfWeek(dayOfWeek: List<AverageDayOfWeek>)

    fun showCurrentWeatherView(show: Boolean)

    fun showWeekDayWeatherView(show: Boolean)

    fun showAddInfo(show: Boolean)

    fun setAddInfoList(additionalInfoList: List<AdditionalInfo>)
}