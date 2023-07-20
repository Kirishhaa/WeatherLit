package com.example.weatherapp

import android.app.Application
import android.content.res.Configuration
import androidx.room.Room
import com.example.weatherapp.data.*
import com.example.weatherapp.data.room.AppDatabase
import com.example.weatherapp.domain.SettingsStorage
import com.example.weatherapp.domain.SettingsStorageImpl
import com.example.weatherapp.domain.WeatherRepository
import java.util.*
import java.util.concurrent.TimeUnit

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        changeLocals(Locale(settingsStorage.getLocaleValue()))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        changeLocals(Locale(settingsStorage.getLocaleValue()))
    }

    private val appDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "database.db"
        ).createFromAsset("sqlite.db").build()
    }

    private val applicationStorage by lazy { ApplicationStorage(applicationContext) }

    val presenterManager = PresenterManager(
        presentersSize = 6,
        expirationValue = 10,
        expirationUnit = TimeUnit.SECONDS
    )

    private val settingsStorage: SettingsStorage by lazy { SettingsStorageImpl(applicationContext) }

    val weatherRepository: WeatherRepository by lazy { WeatherRepositoryImpl(appDatabase.getWeatherDao()) }

    val settingsElementRepository by lazy { SettingsElementRepository(appDatabase.getSettingsElementDao()) }

    fun saveLocaleValue(localeValue: String) {
        settingsStorage.saveLocaleValue(localeValue)
        changeLocals(Locale(localeValue))
    }

    fun getIsFirstLaunch(): Boolean = applicationStorage.getIsFirstLaunch()

    fun changeIsFirstLaunch() = applicationStorage.changeIsFirstLaunch()

    fun getLocaleValue(): String = settingsStorage.getLocaleValue()

    fun getUpdateTimestamp() = applicationStorage.getUpdateTimestamp()

    fun setUpdateTimestamp(timestamp: Long) = applicationStorage.putUpdateTimestamp(timestamp)

    fun getRequestedPermissionsCount(): Int = applicationStorage.getRequestedPermissionsCount()

    fun incrementRequestedPermissions() = applicationStorage.incrementRequestedPermissions()

    fun getUsersCoordinates(): WeatherCoordinates? = applicationStorage.getUsersCoordinates()

    fun saveUsersCoordinates(coordinates: WeatherCoordinates) =
        applicationStorage.saveUsersCoordinates(coordinates)

    private fun changeLocals(newLocale: Locale) {
        Locale.setDefault(newLocale)
        val config = Configuration()
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }

    fun saveUsersLocationName(locationName: String) =
        applicationStorage.saveUsersLocationName(locationName)

    fun getUsersLocationName(): String? = applicationStorage.getUsersLocationName()

}