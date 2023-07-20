package com.example.weatherapp.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ApplicationStorage(private val applicationContext: Context) {

    fun changeIsFirstLaunch() {
        val sp = applicationContext.getSharedPreferences("LauncherInfo", Context.MODE_PRIVATE)
        sp.edit().putBoolean("isFirstLaunch", false).apply()
    }

    fun getIsFirstLaunch(): Boolean {
        val sp = applicationContext.getSharedPreferences("LauncherInfo", Context.MODE_PRIVATE)
        return sp.getBoolean("isFirstLaunch", true)
    }

    fun getUpdateTimestamp(): Long {
        val sp = applicationContext.getSharedPreferences("LauncherInfo", Context.MODE_PRIVATE)
        return sp.getLong("updateTimestamp", 0)
    }

    fun putUpdateTimestamp(timestamp: Long) {
        val sp = applicationContext.getSharedPreferences("LauncherInfo", Context.MODE_PRIVATE)
        sp.edit()
            .putLong("updateTimestamp", timestamp)
            .apply()
    }

    fun getRequestedPermissionsCount(): Int {
        val sp = applicationContext.getSharedPreferences("LauncherInfo", Context.MODE_PRIVATE)
        return sp.getInt("requestPermissionsCount", 0)
    }

    fun incrementRequestedPermissions() {
        val sp = applicationContext.getSharedPreferences("LauncherInfo", Context.MODE_PRIVATE)
        sp.edit()
            .putInt("requestPermissionsCount", getRequestedPermissionsCount() + 1)
            .apply()
    }

    fun saveUsersCoordinates(coordinates: WeatherCoordinates) {
        val gson = Gson()
        val json = gson.toJson(coordinates)

        val sp = applicationContext.getSharedPreferences("LauncherInfo", Context.MODE_PRIVATE)
        sp.edit()
            .putString("usersCoordinates", json)
            .apply()
    }

    fun getUsersCoordinates(): WeatherCoordinates? {
        val sp = applicationContext.getSharedPreferences("LauncherInfo", Context.MODE_PRIVATE)
        val jsonUserCoordinates = sp.getString("usersCoordinates", null)

        val gson = Gson()
        val type = object : TypeToken<WeatherCoordinates?>() {}.type
        return gson.fromJson(jsonUserCoordinates, type)
    }

    fun saveUsersLocationName(locationName: String) {
        val sp = applicationContext.getSharedPreferences("LauncherInfo", Context.MODE_PRIVATE)
        sp.edit().putString("userLocationName", locationName).apply()
    }

    fun getUsersLocationName(): String? {
        val sp = applicationContext.getSharedPreferences("LauncherInfo", Context.MODE_PRIVATE)
        return sp.getString("userLocationName", null)
    }

}