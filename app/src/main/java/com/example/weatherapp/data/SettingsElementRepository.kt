package com.example.weatherapp.data

import com.example.weatherapp.domain.room.SettingsElementDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class SettingsElementRepository(
    private val settingsDao: SettingsElementDao,
) {

    suspend fun updateByTitle(title: String, selectedValue: String) = withContext(Dispatchers.IO) {
        settingsDao.updateByTitle(title, selectedValue)
    }

    fun getAll(): Flow<List<SettingsElement>> = settingsDao.getAll().flowOn(Dispatchers.IO)

}