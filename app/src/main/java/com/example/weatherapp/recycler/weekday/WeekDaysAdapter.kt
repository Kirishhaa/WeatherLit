package com.example.weatherapp.recycler.weekday

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.weatherapp.R
import com.example.weatherapp.data.AverageDayOfWeek
import com.example.weatherapp.presenters.WeekDayItemPresenter
import com.example.weatherapp.recycler.base.AbstractAdapter

class WeekDaysAdapter(
    private val listener: Listener,
    private val resources: Resources,
) : AbstractAdapter<WeekDaysViewHolder, WeekDayItemPresenter, AverageDayOfWeek>() {

    interface Listener {
        fun onWeekDayClicked(name: String)
    }

    private var weekDaysList = emptyList<AverageDayOfWeek>()

    override fun getModelId(model: AverageDayOfWeek): Any = model.dt

    override fun getItem(position: Int): AverageDayOfWeek = weekDaysList[position]

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): WeekDaysViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.day_of_week_item, parent, false)
        val holder = WeekDaysViewHolder(view)
        view.setOnClickListener {
            listener.onWeekDayClicked(holder.getName())
        }
        return WeekDaysViewHolder(view)
    }

    override fun getItemCount(): Int = weekDaysList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setDaysOfWeek(weekDayList: List<AverageDayOfWeek>) {
        weekDayList.forEach {
            presenters[it.dt] = WeekDayItemPresenter(resources)
        }
        this.weekDaysList = weekDayList
        notifyDataSetChanged()
    }

}