package com.example.weatherapp.presenters.forecasted

import com.example.weatherapp.data.ForecastedWeatherItem
import com.example.weatherapp.presenters.base.BasePresenter
import com.example.weatherapp.views.forecastedfragment.ForecastedListView

class ForecastedListPresenter : BasePresenter<ForecastedListView, List<ForecastedWeatherItem>>() {

    override fun bindView(view: ForecastedListView) {
        super.bindView(view)
        if (model != null) {
            updateView()
        }
    }

    override fun updateView() {
        val view = getView() ?: return
        val model = this.model ?: return
        view.setForecastedList(model)
    }
}