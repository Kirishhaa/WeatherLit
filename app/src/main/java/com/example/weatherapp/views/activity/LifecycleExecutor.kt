package com.example.weatherapp.views.activity

import androidx.lifecycle.LifecycleOwner

private typealias LifecycleTask = (LifecycleOwner) -> Unit

class LifecycleExecutor {

    private var lifecycleOwner: LifecycleOwner? = null

    private val tasks = mutableListOf<LifecycleTask>()

    fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner
        notifyLifecycleOwner()
    }

    fun unregisterLifecycleOwner() {
        this.lifecycleOwner = null
    }

    fun execute(task: LifecycleTask) {
        val lifecycleOwner = this.lifecycleOwner
        if (lifecycleOwner == null) {
            tasks += task
        } else {
            task(lifecycleOwner)
        }
    }

    private fun notifyLifecycleOwner() {
        val lifecycleOwner = this.lifecycleOwner ?: return
        tasks.forEach {
            it(lifecycleOwner)
        }
        tasks.clear()
    }

}