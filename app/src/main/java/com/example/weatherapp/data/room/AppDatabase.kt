package com.example.weatherapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.domain.room.SettingsElementDao
import com.example.weatherapp.domain.room.WeathersDao

@Database(
    version = 1,
    entities = [
        WeatherEntity::class,
        SettingsElementEntity::class
    ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getWeatherDao(): WeathersDao

    abstract fun getSettingsElementDao(): SettingsElementDao

}