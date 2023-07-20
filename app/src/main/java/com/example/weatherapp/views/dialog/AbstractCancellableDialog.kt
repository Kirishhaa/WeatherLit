package com.example.weatherapp.views.dialog

import android.content.DialogInterface
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.weatherapp.R

abstract class AbstractCancellableDialog(private val requestKey: String) : DialogFragment() {

    companion object {
        const val ARG_IS_CANCELLED = "IS_CANCELLED"
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        val result = bundleOf(ARG_IS_CANCELLED to true)
        setFragmentResult(requestKey, result)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setWindowAnimations(R.style.DialogFragmentAnimation)
    }

}