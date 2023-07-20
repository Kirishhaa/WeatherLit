package com.example.weatherapp.data

import android.os.Bundle
import com.example.weatherapp.presenters.base.BasePresenter
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

class PresenterManager(
    presentersSize: Long,
    expirationValue: Long,
    expirationUnit: TimeUnit,
) {

    private val key = AtomicLong()

    companion object {
        const val PRESENTER_KEY = "presenter_key"
    }

    private val presenters: Cache<Long, BasePresenter<*, *>> = CacheBuilder.newBuilder()
        .maximumSize(presentersSize)
        .expireAfterWrite(expirationValue, expirationUnit)
        .build()


    fun savePresenter(presenter: BasePresenter<*, *>, outState: Bundle) {
        val presenterId = key.incrementAndGet()
        presenters.put(presenterId, presenter)
        outState.putLong(PRESENTER_KEY, presenterId)
    }

    fun restorePresenter(savedState: Bundle): BasePresenter<*, *>? {
        val key = savedState.getLong(PRESENTER_KEY)
        val presenter = presenters.getIfPresent(key)
        presenters.invalidate(key)
        return presenter
    }

}