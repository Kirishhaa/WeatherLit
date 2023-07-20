package com.example.weatherapp.presenters

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.format.DateFormat
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import com.example.weatherapp.R
import com.example.weatherapp.data.*
import com.example.weatherapp.domain.WeatherRepository
import com.example.weatherapp.recycler.weekday.WeekDaysAdapter
import com.example.weatherapp.views.mainfragment.MainView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class MainPresenter(
    applicationContext: Context
): SynchronizePresenter<MainView, WeatherRepository>(applicationContext),
    WeekDaysAdapter.Listener{

    private val translator = Translator(applicationContext.resources)

    private val mainHandler = Handler(Looper.getMainLooper())

    private var daysOfWeek: List<AverageDayOfWeek>? = null

    private var currentWeather: Weather? = null

    private var forecastedWeather: List<ForecastedWeatherItem>? = null

    private var isLoaded = AtomicBoolean(false)

    private var additionalInfoList: List<AdditionalInfo> = emptyList()

    override fun setModel(model: WeatherRepository) {
        super.setModel(model)
        if(isWorkable()) updateView()
    }

    override fun bindView(view: MainView) {
        super.bindView(view)
        val model = this.model
        if (model != null && !isLoaded.getAndSet(true)) {
            loadData(model)
        } else {
            updateView()
        }
    }

    override fun updateView() {
        val view = getView() ?: return
        mainHandler.post {
            Log.d("MainPresenter", "updateView")
            val daysOfWeek = this.daysOfWeek
            if (daysOfWeek != null) {
                view.setDaysOfWeek(daysOfWeek)
                view.showWeekDayWeatherView(true)
            }
            val currentWeather = this.currentWeather
            if (currentWeather != null) {
                val typeWeather = translator.translateWeatherGroup(currentWeather.group)
                view.setDegreeLabel(currentWeather.temp.toInt().toString()+"°C")
                view.setTypeWeatherLabel(typeWeather)
                view.showCurrentWeatherView(true)
            }
            val additionalInfoList = this.additionalInfoList
            if (additionalInfoList.isNotEmpty()) {
                view.setAddInfoList(additionalInfoList)
                view.showAddInfo(true)
            }
        }
    }

    private fun loadData(model: WeatherRepository) {
        val helper = DayOfWeekHelper()
        CoroutineScope(Dispatchers.IO).launch {
            launch {
                model.getCurrentWeather().collect {
                    if (it != null) {
                        currentWeather = it
                        mainHandler.post {
                            updateView()
                        }
                        additionalInfoList = collectAdditionalInfo(it)
                        mainHandler.post {
                            updateView()
                        }
                    } else {
                        mainHandler.post {
                            getView()?.showAddInfo(false)
                            getView()?.showCurrentWeatherView(false)
                        }
                    }
                }
            }
            launch {
                model.getForecastedWeather().collect {
                    if (it.isNotEmpty()) {
                        forecastedWeather =
                            it.map { weather -> ForecastedWeatherItem(weather, false) }
                        daysOfWeek = helper.createDaysOfWeekList(it)
                        mainHandler.post {
                            updateView()
                        }
                    } else {
                        mainHandler.post {
                            getView()?.showWeekDayWeatherView(false)
                        }
                    }
                }
            }
        }
    }

    private fun collectAdditionalInfo(weather: Weather): List<AdditionalInfo> {
        val resources = applicationContext.resources
        val tempMax = AdditionalInfo(
            "Temp max",
            weather.tempMax.toInt().toString() + "°C",
            null,
            ResourcesCompat.getDrawable(resources, R.drawable.ic_max_temp, null)!!
        )
        val tempMin = AdditionalInfo(
            "Temp min",
            weather.tempMin.toInt().toString() + "°C",
            null,
            ResourcesCompat.getDrawable(resources, R.drawable.ic_temp_min, null)!!
        )
        val cloudiness = AdditionalInfo(
            "Cloudiness",
            weather.cloudiness.toString() + "%",
            null,
            ResourcesCompat.getDrawable(resources, R.drawable.ic_cloudiness, null)!!
        )
        val humidity = AdditionalInfo(
            "Humidity",
            weather.humidity.toString() + "%",
            null,
            ResourcesCompat.getDrawable(resources, R.drawable.ic_humidity, null)!!
        )
        val pressure = AdditionalInfo(
            "Pressure",
            weather.pressure.toString(),
            "hPa",
            ResourcesCompat.getDrawable(resources, R.drawable.ic_pressure, null)!!
        )
        val windSpeed = AdditionalInfo(
            "Wind speed",
            weather.windSpeed.toString(),
            "m/s",
            ResourcesCompat.getDrawable(resources, R.drawable.ic_wind_speed, null)!!
        )
        return listOf(tempMax, tempMin, humidity, cloudiness, pressure, windSpeed)
    }

    fun onSeeMorePressed() {
        val view = getView() ?: return
        val forecastedWeather = this.forecastedWeather
        if (isLoaded.get() && forecastedWeather != null) {
            view.goToForecastedWeather(forecastedWeather)
        }
    }

    override fun onWeekDayClicked(name: String) {
        val view = getView() ?: return
        val dayOfWeek = name.split(",")[0]
        val mutableList = mutableListOf<ForecastedWeatherItem>()
        var startedPosition: Int? = null
        forecastedWeather?.forEachIndexed { index, forecastedItem ->
            val weather = forecastedItem.weather
            val weatherDayOfWeek = DateFormat.format("EEE", weather.dt).toString()
            val isSelected = weatherDayOfWeek == dayOfWeek
            if (startedPosition == null && isSelected) startedPosition = index
            mutableList += forecastedItem.copy(isSelected = isSelected)
        }
        if (startedPosition == null) startedPosition = 0
        view.goToForecastedWeather(mutableList, startedPosition!!)
    }
}