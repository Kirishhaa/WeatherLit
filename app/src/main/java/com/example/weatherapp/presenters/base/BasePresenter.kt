package com.example.weatherapp.presenters.base

import java.lang.ref.WeakReference

abstract class BasePresenter<V, M> {

    private var view: WeakReference<V>? = null

    @set:JvmName("basePresenterModel")
    protected var model: M? = null

    open fun bindView(view: V) {
        this.view = WeakReference(view)
    }

    open fun unbindView() {
        this.view = null
    }

    open fun setModel(model: M) {
        this.model = model
    }

    protected abstract fun updateView()

    protected fun getView(): V? = this.view?.get()

    protected fun isWorkable() = model != null && getView() != null

}