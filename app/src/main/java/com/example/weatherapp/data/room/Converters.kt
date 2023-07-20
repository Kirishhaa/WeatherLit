package com.example.weatherapp.data.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import org.json.JSONObject

class Converters {

    @TypeConverter
    fun toString(list: List<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun toListString(string: String): List<String> {
        val jsonObj = JSONObject(string)
        val arrayJson = jsonObj.getJSONArray("values")
        val resList = mutableListOf<String>()
        for (i in 0 until arrayJson.length()) {
            resList += arrayJson.get(i).toString()
        }
        return resList
    }


}