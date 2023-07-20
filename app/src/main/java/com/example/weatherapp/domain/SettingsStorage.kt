package com.example.weatherapp.domain

import android.content.Context

interface SettingsStorage {

    fun saveLocaleValue(localeValue: String)

    fun getLocaleValue(): String
}

class SettingsStorageImpl(private val applicationContext: Context) : SettingsStorage {

    private val SETTINGS_STORAGE = "SETTINGS_STORAGE"

    override fun saveLocaleValue(localeValue: String) {
        val sp = applicationContext.getSharedPreferences(SETTINGS_STORAGE, Context.MODE_PRIVATE)
        sp.edit()
            .putString("localeValue", localeValue)
            .apply()
    }

    override fun getLocaleValue(): String {
        val sp = applicationContext.getSharedPreferences(SETTINGS_STORAGE, Context.MODE_PRIVATE)
        return sp.getString("localeValue", null) ?: "en"
    }

}