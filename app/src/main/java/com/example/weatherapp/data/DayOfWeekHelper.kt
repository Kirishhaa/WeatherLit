package com.example.weatherapp.data

import android.text.format.DateFormat

class DayOfWeekHelper {

    fun createDaysOfWeekList(forecasted: List<Weather>): List<AverageDayOfWeek> {
        val listAverageDays = mutableListOf<AverageDayOfWeek>()
        val iconIDList = mutableListOf<String>()
        val nameList = mutableListOf<String>()
        var currentDayOfWeek: String? = null
        var dt: Long? = null
        var averageTemp = 0.0
        var averageFeelLikeTemp = 0.0
        var sizeIteration = 0
        forecasted.forEach {

            val dayOfWeek = DateFormat.format("EEE", it.dt).toString()

            if (currentDayOfWeek == null) {
                currentDayOfWeek = dayOfWeek
                dt = it.dt
            }

            if (currentDayOfWeek != dayOfWeek) {

                listAverageDays.add(
                    createAverageDayOfWeek(
                        names = nameList,
                        iconIDs = iconIDList,
                        averageTemp = averageTemp / sizeIteration,
                        averageFeelLikeTemp = averageFeelLikeTemp / sizeIteration,
                        dt = dt!!
                    )
                )

                currentDayOfWeek = dayOfWeek
                dt = it.dt
                sizeIteration = 0
                iconIDList.clear()
                nameList.clear()
                averageTemp = 0.0
                averageFeelLikeTemp = 0.0
            }

            sizeIteration++
            iconIDList.add(it.iconID)
            nameList.add(it.group)
            averageTemp += it.temp
            averageFeelLikeTemp += it.feelsLikeTemp

        }

        if (sizeIteration != 0) {
            listAverageDays.add(
                createAverageDayOfWeek(
                    names = nameList,
                    iconIDs = iconIDList,
                    averageTemp = averageTemp / sizeIteration,
                    averageFeelLikeTemp = averageFeelLikeTemp / sizeIteration,
                    dt = forecasted.last().dt
                )
            )
        }

        return listAverageDays
    }

    private fun createAverageDayOfWeek(
        names: List<String>,
        iconIDs: List<String>,
        averageTemp: Double,
        averageFeelLikeTemp: Double,
        dt: Long,
    ): AverageDayOfWeek {
        val averageName = findAverageStringInList(names)
        val averageIconId = findAverageStringInList(iconIDs)

        return AverageDayOfWeek(
            name = averageName,
            temp = averageTemp,
            feelLikeTemp = averageFeelLikeTemp,
            iconID = averageIconId,
            dt = dt
        )
    }


    private fun findAverageStringInList(list: List<String>): String {
        val map = mutableMapOf<String, Int>()

        list.forEach {
            val value = map[it] ?: 0
            map[it] = value + 1
        }

        var max = 0
        var result = ""
        map.forEach {
            if (it.value > max) {
                max = it.value
                result = it.key
            }
        }
        return result
    }

}