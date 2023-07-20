package com.example.weatherapp.views.dialog

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.weatherapp.R
import kotlin.properties.Delegates

class SplashWeatherTroubleDialog : DialogFragment(R.layout.fragment_trouble) {

    private var buttonRes by Delegates.notNull<Int>()
    private var textRes by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            buttonRes = savedInstanceState.getInt("buttonRes")
            textRes = savedInstanceState.getInt("textRes")
        }
    }

    companion object {
        fun onInstance(buttonRes: Int, textRes: Int): SplashWeatherTroubleDialog {
            val fr = SplashWeatherTroubleDialog()
            fr.buttonRes = buttonRes
            fr.textRes = textRes
            fr.isCancelable = false
            return fr
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<LinearLayout>(R.id.troubleRoot)
        val button = view.findViewById<Button>(R.id.troubleButton)
        button.setText(buttonRes)
        button.setOnClickListener {
            requireActivity().finish()
        }
        val textView = view.findViewById<TextView>(R.id.troubleTV)
        textView.setText(textRes)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            buttonRes = savedInstanceState.getInt("buttonRes")
            textRes = savedInstanceState.getInt("textRes")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("buttonRes", buttonRes)
        outState.putInt("textRes", textRes)
    }
}