package com.example.weatherapp.recycler.weekday

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.weatherapp.R
import com.example.weatherapp.presenters.WeekDayItemPresenter
import com.example.weatherapp.recycler.base.AbstractViewHolder

class WeekDaysViewHolder(view: View) : AbstractViewHolder<WeekDayItemPresenter>(view),
    WeekDayView {

    private val icon = view.findViewById<ImageView>(R.id.ic_item)!!

    private val name = view.findViewById<TextView>(R.id.name_item)!!

    private val degree = view.findViewById<TextView>(R.id.degree_item)!!

    override fun bindPresenter(presenter: WeekDayItemPresenter) {
        super.bindPresenter(presenter)
        presenter.bindView(this)
    }

    override fun setIcon(resId: Int) {
        this.icon.setImageResource(resId)
    }

    override fun setName(name: String) {
        this.name.text = name
    }

    override fun setDegree(degree: String) {
        this.degree.text = degree
    }

    override fun getName() = name.text.toString()

}