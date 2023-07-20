package com.example.weatherapp.mvcadapters

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.AdditionalInfo
import com.example.weatherapp.data.Translator

class AdditionalInfoAdapter(resource: Resources) :
    RecyclerView.Adapter<AdditionalInfoAdapter.ViewHolder>() {

    private val translator = Translator(resource)

    private var additionalInfoList = emptyList<AdditionalInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_addition_info, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentAdditionalInfo = additionalInfoList[position]
        holder.setImage(currentAdditionalInfo.image)
        holder.setTitle(translator.translateAdditionalInfoTitle(currentAdditionalInfo.title))
        holder.setValue(currentAdditionalInfo.value)
        holder.setSubValue(translator.translateAdditionalInfoSubValue(currentAdditionalInfo.subValue))
    }

    override fun getItemCount(): Int = additionalInfoList.size


    @SuppressLint("NotifyDataSetChanged")
    fun setAdditionalInfoList(additionalInfoList: List<AdditionalInfo>) {
        this.additionalInfoList = additionalInfoList
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image = view.findViewById<ImageView>(R.id.additionInfoImage)
        private val title = view.findViewById<TextView>(R.id.additionInfoTitle)
        private val value = view.findViewById<TextView>(R.id.additionInfoValue)
        private val subValue = view.findViewById<TextView>(R.id.additionInfoSubValue)


        fun setImage(drawable: Drawable) {
            image.setImageDrawable(drawable)
        }

        fun setSubValue(value: String?) {
            subValue.text = value
        }

        fun setTitle(title: String) {
            this.title.text = title
        }

        fun setValue(value: String) {
            this.value.text = value
        }
    }
}