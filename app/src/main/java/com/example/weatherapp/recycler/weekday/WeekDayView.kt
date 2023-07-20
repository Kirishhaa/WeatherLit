package com.example.weatherapp.recycler.weekday

interface WeekDayView {

    fun setIcon(resId: Int)

    fun setName(name: String)

    fun setDegree(degree: String)

    fun getName(): String

}