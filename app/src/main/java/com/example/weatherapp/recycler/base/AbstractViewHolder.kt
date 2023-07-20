package com.example.weatherapp.recycler.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.presenters.base.BasePresenter

abstract class AbstractViewHolder<P : BasePresenter<*, *>>(view: View) :
    RecyclerView.ViewHolder(view) {

    protected var presenter: P? = null

    open fun bindPresenter(presenter: P) {
        this.presenter = presenter
    }

    open fun unbindPresenter() {
        this.presenter = null
    }

}