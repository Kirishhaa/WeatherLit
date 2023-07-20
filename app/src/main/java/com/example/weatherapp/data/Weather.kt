package com.example.weatherapp.data

import android.os.Parcel
import android.os.Parcelable

data class ForecastedWeatherItem(
    val weather: Weather,
    val isSelected: Boolean,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Weather::class.java.classLoader)!!,
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(weather, flags)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ForecastedWeatherItem> {
        override fun createFromParcel(parcel: Parcel): ForecastedWeatherItem {
            return ForecastedWeatherItem(parcel)
        }

        override fun newArray(size: Int): Array<ForecastedWeatherItem?> {
            return arrayOfNulls(size)
        }
    }
}

data class Weather(
    val dt: Long,
    val coordinates: WeatherCoordinates,
    val group: String,
    val description: String,
    val temp: Double,
    val feelsLikeTemp: Double,
    val tempMin: Double,
    val tempMax: Double,
    val iconID: String,
    val pressure: Int,
    val humidity: Int,
    val windSpeed: Double,
    val windDeg: Int,
    val cloudiness: Int,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readParcelable(WeatherCoordinates::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(dt)
        parcel.writeParcelable(coordinates, flags)
        parcel.writeString(group)
        parcel.writeString(description)
        parcel.writeDouble(temp)
        parcel.writeDouble(feelsLikeTemp)
        parcel.writeDouble(tempMin)
        parcel.writeDouble(tempMax)
        parcel.writeString(iconID)
        parcel.writeInt(pressure)
        parcel.writeInt(humidity)
        parcel.writeDouble(windSpeed)
        parcel.writeInt(windDeg)
        parcel.writeInt(cloudiness)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Weather> {
        override fun createFromParcel(parcel: Parcel): Weather {
            return Weather(parcel)
        }

        override fun newArray(size: Int): Array<Weather?> {
            return arrayOfNulls(size)
        }
    }
}