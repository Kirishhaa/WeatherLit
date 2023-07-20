package com.example.weatherapp.mvcadapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.SettingsElement
import com.example.weatherapp.views.custom.CustomSettings
import com.example.weatherapp.views.custom.CustomSettings.State

class OuterSettingsAdapter(
    private val listener: CustomSettings.Listener,
) : RecyclerView.Adapter<OuterSettingsAdapter.ViewHolder>() {

    private var settingsElements = emptyList<SettingsElementsItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CustomSettings(parent.context)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = settingsElements.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.customSettings.setSettingsElementItem(settingsElements[position])
        holder.customSettings.setListener(listener)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSettingElements(settingsElements: List<SettingsElementsItem>) {
        this.settingsElements = settingsElements
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val customSettings = view as CustomSettings
    }

    data class SettingsElementsItem(
        val settingsElement: SettingsElement,
        val state: State,
    )

}