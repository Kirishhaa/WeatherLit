package com.example.weatherapp.views.custom

interface ToolbarView {

    fun setDefaultData()

    fun setDataByListener(listener: ToolbarController)

    fun setProgress(progress: Int)

    fun showProgressBar()

    fun hideProgressBar()

}