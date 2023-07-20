package com.example.weatherapp.mvcadapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.Translator

class InnerSettingsAdapter(
    private val listener: Listener,
    private val translator: Translator,
) : RecyclerView.Adapter<InnerSettingsAdapter.ViewHolder>() {

    private var textValues = emptyList<String>()

    interface Listener {
        fun onTextValuePressed(textValue: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.settings_element, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return textValues.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textValue.text = translator.translateSettingsValue(textValues[position])
        holder.textValue.setOnClickListener {
            listener.onTextValuePressed(textValues[position])
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setTextValues(textValues: List<String>) {
        this.textValues = textValues
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val textValue = view.findViewById<TextView>(R.id.elementSettingsTextField)!!

    }
}