package com.example.weatherapp.recycler.forecasted

import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.example.weatherapp.R
import com.example.weatherapp.presenters.forecasted.ForecastedItemPresenter
import com.example.weatherapp.recycler.base.AbstractViewHolder
import com.example.weatherapp.utils.SELECTED_COLOR

class ForecastedViewHolder(private val resources: Resources, view: View) :
    AbstractViewHolder<ForecastedItemPresenter>(view),
    ForecastedItemView {
    private val icon = view.findViewById<ImageView>(R.id.ic_item)!!
    private val dayOfWeek = view.findViewById<TextView>(R.id.day_of_week_item)!!
    private val date = view.findViewById<TextView>(R.id.date_item)!!
    private val temp = view.findViewById<TextView>(R.id.temp_item)!!
    private val feelLikeTemp = view.findViewById<TextView>(R.id.feel_like_temp_item)!!
    private val description = view.findViewById<TextView>(R.id.description_item)!!
    private val time = view.findViewById<TextView>(R.id.time_item)!!

    override fun bindPresenter(presenter: ForecastedItemPresenter) {
        super.bindPresenter(presenter)
        presenter.bindView(this)
    }

    override fun setIcon(iconRes: Int) {
        this.icon.setImageResource(iconRes)
    }

    override fun setDayOfWeek(value: String) {
        this.dayOfWeek.text = value
    }

    override fun setDate(date: String) {
        this.date.text = date
    }

    override fun setTemp(temp: Int) {
        this.temp.text = temp.toString()
    }

    override fun setFeelLikeTemp(temp: Int) {
        this.feelLikeTemp.text = temp.toString()
    }

    override fun setDescription(description: String) {
        this.description.text = description
    }

    override fun setTime(time: String) {
        this.time.text = time
    }

    override fun setColor(color: Int) {
        if (color == SELECTED_COLOR) {
            itemView.background =
                ResourcesCompat.getDrawable(resources, R.drawable.selected_item_bg, null)
        } else {
            itemView.background =
                ResourcesCompat.getDrawable(resources, R.drawable.unselected_item, null)
        }
    }

}