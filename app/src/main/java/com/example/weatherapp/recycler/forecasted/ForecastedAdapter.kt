package com.example.weatherapp.recycler.forecasted

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.weatherapp.R
import com.example.weatherapp.data.ForecastedWeatherItem
import com.example.weatherapp.presenters.forecasted.ForecastedItemPresenter
import com.example.weatherapp.recycler.base.AbstractAdapter

class ForecastedAdapter(
    private val resources: Resources,
) : AbstractAdapter<ForecastedViewHolder, ForecastedItemPresenter, ForecastedWeatherItem>() {

    private var forecasted = emptyList<ForecastedWeatherItem>()

    override fun getModelId(model: ForecastedWeatherItem): Any = model.weather.dt

    override fun getItem(position: Int): ForecastedWeatherItem = forecasted[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastedViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.forecasted_item, parent, false)
        return ForecastedViewHolder(resources, view)
    }

    override fun getItemCount(): Int = forecasted.size

    @SuppressLint("NotifyDataSetChanged")
    fun setForecasted(forecasted: List<ForecastedWeatherItem>) {
        forecasted.forEach {
            presenters[it.weather.dt] = ForecastedItemPresenter(resources)
        }
        this.forecasted = forecasted
        notifyDataSetChanged()
    }

}