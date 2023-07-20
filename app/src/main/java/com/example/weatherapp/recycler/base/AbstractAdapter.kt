package com.example.weatherapp.recycler.base

import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.presenters.base.BasePresenter

abstract class AbstractAdapter<V : AbstractViewHolder<P>, P : BasePresenter<*, M>, M> :
    RecyclerView.Adapter<V>() {

    protected val presenters = HashMap<Any, P>()

    override fun onBindViewHolder(holder: V, position: Int) {
        val model = getItem(position)
        val presenter = getPresenter(model)
        if (presenter != null) {
            presenter.setModel(model)
            holder.bindPresenter(presenter)
        } else {
            holder.unbindPresenter()
        }

    }

    override fun onFailedToRecycleView(holder: V): Boolean {

        holder.unbindPresenter()

        return super.onFailedToRecycleView(holder)
    }

    override fun onViewRecycled(holder: V) {
        super.onViewRecycled(holder)

        holder.unbindPresenter()
    }

    protected open fun getPresenter(model: M): P? {
        return presenters[getModelId(model)]
    }

    protected abstract fun getModelId(model: M): Any

    protected abstract fun getItem(position: Int): M

}