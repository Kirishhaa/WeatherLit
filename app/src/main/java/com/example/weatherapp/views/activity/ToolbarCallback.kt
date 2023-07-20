package com.example.weatherapp.views.activity

interface ToolbarCallback {

    fun setDefaultTownLabel()

    fun setTownLabel(label: String, isUpdate: Boolean, isShow: Boolean)

    fun setToolbarProgress(progress: Int)

    fun hideProgressToolbar()

    fun showProgressToolbar()

}