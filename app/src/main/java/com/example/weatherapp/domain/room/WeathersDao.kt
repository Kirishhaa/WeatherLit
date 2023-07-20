package com.example.weatherapp.domain.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.room.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeathersDao {

    @Insert(entity = WeatherEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecastedWeather(list: List<WeatherEntity>)

    @Insert(entity = WeatherEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(weather: WeatherEntity)

    @Query("SELECT * FROM weather WHERE id != 1")
    fun getForecastedWeather(): Flow<List<WeatherEntity>>

    @Query("SELECT * FROM weather WHERE id = 1")
    fun getCurrentWeather(): Flow<WeatherEntity?>

}