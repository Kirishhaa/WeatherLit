package com.example.weatherapp.views.settings

import com.example.weatherapp.mvcadapters.OuterSettingsAdapter.SettingsElementsItem

interface SettingsView {

    fun setSettingItems(settingElements: List<SettingsElementsItem>)

    fun changeLocals(localeValue: String)

    fun setSettingsLabel()
}