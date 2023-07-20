package com.example.weatherapp.domain.room

import androidx.room.Dao
import androidx.room.Query
import com.example.weatherapp.data.SettingsElement
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsElementDao {

    @Query("SELECT * FROM settings")
    fun getAll(): Flow<List<SettingsElement>>

    @Query("UPDATE settings SET selected_value =:selected_value WHERE title =:title")
    suspend fun updateByTitle(title: String, selected_value: String)

}