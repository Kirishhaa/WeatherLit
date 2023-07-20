package com.example.weatherapp.presenters

import android.content.res.Resources
import android.text.format.DateFormat
import com.example.weatherapp.data.AverageDayOfWeek
import com.example.weatherapp.data.IconProvider
import com.example.weatherapp.data.Translator
import com.example.weatherapp.presenters.base.BasePresenter
import com.example.weatherapp.recycler.weekday.WeekDayView

class WeekDayItemPresenter(resources: Resources) : BasePresenter<WeekDayView, AverageDayOfWeek>() {

    private val translator = Translator(resources)

    override fun bindView(view: WeekDayView) {
        super.bindView(view)
        if (isWorkable()) updateView()
    }

    override fun updateView() {
        val view = getView() ?: return
        val model = this.model ?: return

        val name = translator.translateWeatherGroup(model.name)
        val date = DateFormat.format("dd.MM", model.dt).toString()
        val dayOfWeek = DateFormat.format("EEE", model.dt).toString()
        view.setName("$dayOfWeek, $date, $name")

        val iconRes = IconProvider.getIconByID(model.iconID)
        view.setIcon(iconRes)

        val degree = "${model.temp.toInt()}/${model.feelLikeTemp.toInt()}°C"
        view.setDegree(degree)
    }

}