package com.example.weatherapp.data

import android.os.Parcel
import android.os.Parcelable

data class WeatherCoordinates(
    val lat: Double,
    val lon: Double,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(lat)
        parcel.writeDouble(lon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WeatherCoordinates> {
        override fun createFromParcel(parcel: Parcel): WeatherCoordinates {
            return WeatherCoordinates(parcel)
        }

        override fun newArray(size: Int): Array<WeatherCoordinates?> {
            return arrayOfNulls(size)
        }
    }
}