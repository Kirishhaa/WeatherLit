package com.example.weatherapp.presenters.forecasted

import android.content.res.Resources
import android.text.format.DateFormat
import com.example.weatherapp.data.ForecastedWeatherItem
import com.example.weatherapp.data.IconProvider
import com.example.weatherapp.data.Translator
import com.example.weatherapp.presenters.base.BasePresenter
import com.example.weatherapp.recycler.forecasted.ForecastedItemView
import com.example.weatherapp.utils.NO_SELECTED_COLOR
import com.example.weatherapp.utils.SELECTED_COLOR

class ForecastedItemPresenter(
    resources: Resources,
) : BasePresenter<ForecastedItemView, ForecastedWeatherItem>() {

    private val translator = Translator(resources)

    override fun bindView(view: ForecastedItemView) {
        super.bindView(view)
        if (model != null) {
            updateView()
        }
    }

    override fun updateView() {
        val view = getView() ?: return
        val model = model ?: return

        val weather = model.weather

        val iconRes = IconProvider.getIconByID(weather.iconID)
        view.setIcon(iconRes)
        view.setTemp(weather.temp.toInt())
        view.setFeelLikeTemp(weather.feelsLikeTemp.toInt())

        val description = translator.translateWeatherDescription(weather.description)
        view.setDescription(description)

        val date = DateFormat.format("dd.MM", weather.dt).toString()
        view.setDate(date)

        val dayOfWeek = DateFormat.format("EEEE", weather.dt).toString()
        view.setDayOfWeek(dayOfWeek)

        val time = DateFormat.format("HH:mm", weather.dt).toString()
        view.setTime(time)

        if (model.isSelected) view.setColor(SELECTED_COLOR) else view.setColor(NO_SELECTED_COLOR)

    }
}