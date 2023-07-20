package com.example.weatherapp.views.custom

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.weatherapp.R
import com.example.weatherapp.presenters.custom.CustomToolbarPresenter
import com.google.android.material.progressindicator.LinearProgressIndicator

class CustomToolbar @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : Toolbar(context, attributeSet, defStyleAttr), ToolbarView {

    private val progressIndicator: LinearProgressIndicator
    private val settings: ImageView
    private val synchronize: ImageView
    private val title: TextView
    private val navigateUp: ImageView

    private var townValue: String? = null

    private var presenter = CustomToolbarPresenter()

    init {
        presenter.bindView(this)
        LayoutInflater.from(context).inflate(R.layout.custom_toolbar, this, true)
        synchronize = findViewById(R.id.toolbar_synchronize)
        settings = findViewById(R.id.toolbar_settings)
        progressIndicator = findViewById(R.id.progressIndicator)
        navigateUp = findViewById(R.id.toolbar_navigate_up)
        title = findViewById(R.id.toolbar_town)
    }

    fun registerListener(listener: ToolbarController) {
        presenter.setModel(listener)
    }

    fun setDefaultTownLabel() {
        setTownLabel(townValue!!, isUpdate = false, isShow = true)
    }

    fun unregisterListener() {
        presenter.resetModel()
    }

    override fun setDataByListener(listener: ToolbarController) {
        title.isVisible = listener is ToolbarTitleController

        if (listener is ToolbarSyncController) {
            synchronize.isVisible = true
            synchronize.setOnClickListener { listener.onSynchronizePressed() }
        } else {
            synchronize.isVisible = false
            synchronize.setOnClickListener { }
        }
        if (listener is ToolbarSettingsController) {
            settings.isVisible = true
            settings.setOnClickListener { listener.onSettingsPressed() }
        } else {
            settings.isVisible = false
            settings.setOnClickListener { }
        }
        if (listener is ToolbarNavigateUpController) {
            navigateUp.isVisible = true
            navigateUp.setOnClickListener { listener.onNavigateUp() }
        } else {
            navigateUp.isVisible = false
            navigateUp.setOnClickListener { }
        }
    }

    fun setTownLabel(label: String, isUpdate: Boolean, isShow: Boolean) {
        if (isUpdate) townValue = label
        if (isShow) title.text = label
        Log.d("CustomToolbar", "setTownLabel")
    }

    override fun setProgress(progress: Int) {
        progressIndicator.progress = progress
    }

    override fun showProgressBar() {
        progressIndicator.isVisible = true
    }

    override fun hideProgressBar() {
        progressIndicator.isVisible = false
        progressIndicator.progress = 0
    }

    override fun setDefaultData() {
        title.isVisible = false
        synchronize.isInvisible = true
        settings.isVisible = false
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putString("townValue", townValue)
        bundle.putString("townLabel", title.text.toString())
        bundle.putBoolean("isVisibleProgress", progressIndicator.isVisible)
        bundle.putParcelable("parcelableState", super.onSaveInstanceState())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val bundle = state as Bundle
        townValue = bundle.getString("townValue")
        title.text = bundle.getString("townLabel")
        progressIndicator.isVisible = bundle.getBoolean("isVisibleProgress")
        super.onRestoreInstanceState(bundle.getParcelable("parcelableState"))
    }

}