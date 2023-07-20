package com.example.weatherapp.presenters.settings

import android.os.Handler
import android.os.Looper
import com.example.weatherapp.data.SettingsElement
import com.example.weatherapp.data.SettingsElementRepository
import com.example.weatherapp.mvcadapters.OuterSettingsAdapter.SettingsElementsItem
import com.example.weatherapp.presenters.base.BasePresenter
import com.example.weatherapp.views.custom.CustomSettings
import com.example.weatherapp.views.custom.CustomSettings.State
import com.example.weatherapp.views.settings.SettingsView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class SettingsPresenter : BasePresenter<SettingsView, SettingsElementRepository>(),
    CustomSettings.Listener {

    private val mainHandler = Handler(Looper.getMainLooper())

    private var isLoaded = AtomicBoolean(false)

    private var settingsElements: List<SettingsElement>? = null

    private var settingsElementsState: MutableList<State>? = null

    private var settingsElementsItems: List<SettingsElementsItem>? = null

    override fun bindView(view: SettingsView) {
        super.bindView(view)
        if (!isLoaded.getAndSet(true)) {
            loadData()
        } else {
            updateView()
        }
    }

    override fun updateView() {
        settingsElementsItems = settingsElements?.zip(settingsElementsState!!)?.map {
            SettingsElementsItem(it.first, it.second)
        }
        mainHandler.post {
            getView()?.setSettingItems(settingsElementsItems!!)
        }
    }

    private fun loadData() {
        val model = this.model ?: return
        CoroutineScope(Dispatchers.IO).launch {
            model.getAll().collect { setting ->
                settingsElements = setting
                if (settingsElementsState == null) {
                    settingsElementsState = setting.map { State(false) }.toMutableList()
                }
                updateView()
                cancel()
                return@collect
            }
        }
    }

    override fun onSettingsElementPressed(title: String, value: String) {
        val model = this.model ?: return
        doOperationFromSettingsElementTitle(title, value)
        CoroutineScope(Dispatchers.IO).launch {
            model.updateByTitle(title, value)
            loadData()
        }
    }

    override fun updateStateElement(title: String, state: State) {
        val index = settingsElements!!.indexOfFirst { elem -> elem.title == title }
        settingsElementsState!![index] = state
    }

    private fun doOperationFromSettingsElementTitle(title: String, value: String) {
        when (title) {
            "Langugage" -> {
                val localeValue = provideLocaleByTitle(title, value)
                getView()?.changeLocals(localeValue)
                getView()?.setSettingsLabel()
            }
            else -> {
                throw IllegalStateException("Illegal operation: $title")
            }
        }
    }

    private fun provideLocaleByTitle(title: String, value: String): String {
        return when (title) {
            "Langugage" -> {
                when (value) {

                    "English" -> "en"

                    "Ukrainian" -> "uk"

                    else -> throw IllegalStateException("illegal value $value")
                }
            }
            else -> throw IllegalStateException("illegal title $title")
        }
    }
}