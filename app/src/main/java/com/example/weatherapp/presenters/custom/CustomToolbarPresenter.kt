package com.example.weatherapp.presenters.custom

import com.example.weatherapp.presenters.base.BasePresenter
import com.example.weatherapp.views.custom.ToolbarController
import com.example.weatherapp.views.custom.ToolbarView

class CustomToolbarPresenter : BasePresenter<ToolbarView, ToolbarController>() {

    override fun setModel(model: ToolbarController) {
        super.setModel(model)
        if (isWorkable()) updateView()
    }

    fun resetModel() {
        model = null
        updateView()
    }

    override fun bindView(view: ToolbarView) {
        super.bindView(view)
        if (isWorkable()) updateView()
    }

    override fun updateView() {
        val view = getView() ?: return
        val model = this.model
        if (model == null) {
            view.setDefaultData()
        } else {
            view.setDataByListener(model)
        }
    }
}