package com.example.weatherapp.views.activity

private typealias LifecycleTask<T> = (T) -> Unit

class TaskExecutor<T> {

    private var obj: T? = null

    private val tasks = mutableListOf<LifecycleTask<T>>()

    fun registerObject(obj: T) {
        this.obj = obj
        notifyObject()
    }

    fun unregisterObject() {
        this.obj = null
    }

    fun execute(task: LifecycleTask<T>) {
        val obj = this.obj
        if (obj == null) {
            tasks += task
        } else {
            task(obj)
        }
    }

    private fun notifyObject() {
        val obj = this.obj ?: return
        tasks.forEach {
            it(obj)
        }
        tasks.clear()
    }

}