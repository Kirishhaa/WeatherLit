package com.example.weatherapp.data

import android.content.res.Resources
import com.example.weatherapp.R

class Translator(private val resources: Resources) {

    fun translateAdditionalInfoSubValue(value: String?): String {
        return when (value) {
            null -> ""
            "hPa" -> resources.getString(R.string.measure_hPa)
            "m/s" -> resources.getString(R.string.measure_ms)
            else -> throw IllegalStateException("illegal value = $value")
        }
    }

    fun translateAdditionalInfoTitle(title: String): String {
        return when (title) {
            "Temp max" -> resources.getString(R.string.a_i_temp_max)
            "Temp min" -> resources.getString(R.string.a_i_temp_min)
            "Cloudiness" -> resources.getString(R.string.a_i_cloudiness)
            "Humidity" -> resources.getString(R.string.a_i_humidity)
            "Pressure" -> resources.getString(R.string.a_i_pressure)
            "Wind speed" -> resources.getString(R.string.a_i_wind_speed)
            else -> throw IllegalStateException("illegal title = $title")
        }

    }

    fun translateSettingsTitle(value: String): String {
        return when (value) {
            "Langugage" -> resources.getString(R.string.language)
            else -> throw IllegalStateException("illegal value = $value")
        }
    }

    fun translateSettingsValue(value: String): String {
        return when (value) {
            "Ukrainian" -> resources.getString(R.string.ukrainian)
            "English" -> resources.getString(R.string.english)
            else -> throw IllegalStateException("illegal value = $value")
        }
    }

    fun translateWeatherDescription(value: String): String {
        return when (value) {
            "thunderstorm with light rain" -> resources.getString(R.string.d_thunderstorm_with_light_rain)
            "thunderstorm with rain" -> resources.getString(R.string.d_thunderstorm_with_rain)
            "thunderstorm with heavy rain" -> resources.getString(R.string.d_thunderstorm_with_heavy_rain)
            "light thunderstorm" -> resources.getString(R.string.d_light_thunderstorm)
            "thunderstorm" -> resources.getString(R.string.d_thunderstorm)
            "heavy thunderstorm" -> resources.getString(R.string.d_heavy_thunderstorm)
            "ragged thunderstorm" -> resources.getString(R.string.d_ragged_thunderstorm)
            "thunderstorm with light drizzle" -> resources.getString(R.string.d_thunderstorm_with_light_drizzle)
            "thunderstorm with drizzle" -> resources.getString(R.string.d_thunderstorm_with_drizzle)
            "thunderstorm with heavy drizzle" -> resources.getString(R.string.d_thunderstorm_with_heavy_drizzle)
            "light intensity drizzle" -> resources.getString(R.string.d_light_intensity_drizzle)
            "drizzle" -> resources.getString(R.string.d_drizzle)
            "heavy intensity drizzle" -> resources.getString(R.string.d_heavy_intensity_drizzle)
            "light intensity drizzle rain" -> resources.getString(R.string.d_light_intensity_drizzle_rain)
            "drizzle rain" -> resources.getString(R.string.d_drizzle_rain)
            "heavy intensity drizzle rain" -> resources.getString(R.string.d_heavy_intensity_drizzle_rain)
            "shower rain and drizzle" -> resources.getString(R.string.d_shower_rain_and_drizzle)
            "heavy shower rain and drizzle" -> resources.getString(R.string.d_heavy_shower_rain_and_drizzle)
            "shower drizzle" -> resources.getString(R.string.d_shower_drizzle)
            "light rain" -> resources.getString(R.string.d_light_rain)
            "moderate rain" -> resources.getString(R.string.d_moderate_rain)
            "heavy intensity rain" -> resources.getString(R.string.d_heavy_intensity_rain)
            "very heavy rain" -> resources.getString(R.string.d_very_heavy_rain)
            "extreme rain" -> resources.getString(R.string.d_extreme_rain)
            "freezing rain" -> resources.getString(R.string.d_freezing_rain)
            "light intensity shower rain" -> resources.getString(R.string.d_light_intensity_shower_rain)
            "shower rain" -> resources.getString(R.string.d_shower_rain)
            "heavy intensity shower rain" -> resources.getString(R.string.d_heavy_intensity_shower_rain)
            "ragged shower rain" -> resources.getString(R.string.d_ragged_shower_rain)
            "light snow" -> resources.getString(R.string.d_light_snow)
            "snow" -> resources.getString(R.string.d_snow)
            "heavy snow" -> resources.getString(R.string.d_heavy_snow)
            "sleet" -> resources.getString(R.string.d_sleet)
            "light shower sleet" -> resources.getString(R.string.d_light_shower_sleet)
            "shower sleet" -> resources.getString(R.string.d_shower_sleet)
            "light rain and snow" -> resources.getString(R.string.d_light_rain_and_snow)
            "rain and snow" -> resources.getString(R.string.d_rain_and_snow)
            "light shower snow" -> resources.getString(R.string.d_light_shower_snow)
            "shower snow" -> resources.getString(R.string.d_shower_snow)
            "heavy shower snow" -> resources.getString(R.string.d_heavy_shower_snow)
            "mist" -> resources.getString(R.string.d_mist)
            "smoke" -> resources.getString(R.string.d_mist)
            "haze" -> resources.getString(R.string.d_haze)
            "sand/dust whirls" -> resources.getString(R.string.d_sand_dust_whirls)
            "fog" -> resources.getString(R.string.d_fog)
            "sand" -> resources.getString(R.string.d_sand)
            "dust" -> resources.getString(R.string.d_dust)
            "volcanic ash" -> resources.getString(R.string.d_volcanic_ash)
            "squalls" -> resources.getString(R.string.d_squalls)
            "tornado" -> resources.getString(R.string.d_tornado)
            "clear sky" -> resources.getString(R.string.d_clear_sky)
            "few clouds" -> resources.getString(R.string.d_few_clouds)
            "scattered clouds" -> resources.getString(R.string.d_scattered_clouds)
            "broken clouds" -> resources.getString(R.string.d_broken_clouds)
            "overcast clouds" -> resources.getString(R.string.d_overcast_clouds)
            else -> value
        }
    }

    fun translateWeatherGroup(value: String): String {
        return when (value) {
            "Thunderstorm" -> resources.getString(R.string.g_thunderstorm)
            "Drizzle" -> resources.getString(R.string.g_drizzle)
            "Rain" -> resources.getString(R.string.g_rain)
            "Snow" -> resources.getString(R.string.g_snow)
            "Mist" -> resources.getString(R.string.g_mist)
            "Smoke" -> resources.getString(R.string.g_smoke)
            "Haze" -> resources.getString(R.string.g_haze)
            "Dust" -> resources.getString(R.string.g_dust)
            "Fog" -> resources.getString(R.string.g_fog)
            "Sand" -> resources.getString(R.string.g_sand)
            "Ash" -> resources.getString(R.string.g_ash)
            "Squall" -> resources.getString(R.string.g_squall)
            "Tornado" -> resources.getString(R.string.g_tornado)
            "Clear" -> resources.getString(R.string.g_clear)
            "Clouds" -> resources.getString(R.string.g_clouds)
            else -> throw IllegalStateException("invalid value = $value")
        }
    }

}