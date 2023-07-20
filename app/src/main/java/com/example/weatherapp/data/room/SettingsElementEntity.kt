package com.example.weatherapp.data.room

import androidx.room.Entity

@Entity(
    tableName = "settings",
    primaryKeys = [
        "title", "selected_value"
    ]
)
data class SettingsElementEntity(
    val title: String,
    val values: List<String>,
    val selected_value: String,
)