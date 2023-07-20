package com.example.weatherapp.views.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.data.SettingsElement
import com.example.weatherapp.data.Translator
import com.example.weatherapp.databinding.CustomSettingsElementBinding
import com.example.weatherapp.mvcadapters.InnerSettingsAdapter
import com.example.weatherapp.mvcadapters.OuterSettingsAdapter.SettingsElementsItem

class CustomSettings @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleRes: Int = 0,
    desStyleRes: Int = 0,
) : LinearLayout(context, attributeSet, defStyleRes, desStyleRes), InnerSettingsAdapter.Listener {

    private val binding: CustomSettingsElementBinding

    private var isExpanded = false

    private var listener: Listener? = null

    private var settingsElement: SettingsElement? = null
    private var state: State? = null

    init {
        val view =
            LayoutInflater.from(context).inflate(R.layout.custom_settings_element, this, true)
        binding = CustomSettingsElementBinding.bind(view)
        initializeListeners()
    }

    interface Listener {

        fun onSettingsElementPressed(title: String, value: String)

        fun updateStateElement(title: String, state: State)

    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun setSettingsElementItem(item: SettingsElementsItem) {
        this.settingsElement = item.settingsElement
        this.state = item.state
        updateData(item)
    }

    private fun updateData(item: SettingsElementsItem) {
        val translator = Translator(resources)
        binding.elementSettingsTitle.text =
            translator.translateSettingsTitle(item.settingsElement.title)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = InnerSettingsAdapter(this, translator)
        adapter.setTextValues(item.settingsElement.values)
        isExpanded = item.state.isExpanded
        binding.recyclerView.isVisible = isExpanded
        binding.recyclerView.adapter = adapter
    }

    private fun initializeListeners() {
        binding.elementSettingsTitle.setOnClickListener {
            expandCollapseLayout()
        }
    }

    private fun expandCollapseLayout() {
        isExpanded = if (isExpanded) {
            collapse(binding.recyclerView)
            false
        } else {
            expand(binding.recyclerView)
            true
        }
        val newState = State(isExpanded)
        state = newState
        listener?.updateStateElement(settingsElement!!.title, newState)
    }

    private fun expand(view: View) {
        val parentViewWidth = (view.parent as View).width
        val matchParentMeasureSpec =
            MeasureSpec.makeMeasureSpec(parentViewWidth, MeasureSpec.EXACTLY)
        val wrapContentMeasureSpec =
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        view.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        val targetHeight = view.measuredHeight

        view.layoutParams.height = 1
        view.isVisible = true

        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                view.layoutParams.height =
                    if (interpolatedTime == 1f) android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                    else (targetHeight * interpolatedTime).toInt()
                view.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        animation.duration = 400
        view.startAnimation(animation)
    }

    private fun collapse(view: View) {
        val initialHeight = view.measuredHeight

        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                if (interpolatedTime == 1f) {
                    view.isVisible = false
                } else {
                    view.layoutParams.height =
                        (initialHeight - (initialHeight * interpolatedTime)).toInt()
                    view.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        animation.duration = 400
        view.startAnimation(animation)
    }

    override fun onTextValuePressed(textValue: String) {
        listener?.onSettingsElementPressed(settingsElement!!.title, textValue)
    }

    data class State(val isExpanded: Boolean)
}