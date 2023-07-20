package com.example.weatherapp.views.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import com.example.weatherapp.R

class RequireLocationDialog : AbstractCancellableDialog(REQUEST_KEY) {

    companion object {
        const val REQUEST_KEY = "RequireLocationDialogRequestKey"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(context)
            .setCancelable(true)
            .setTitle(resources.getString(R.string.require_accept_location_title))
            .setMessage(resources.getString(R.string.require_accept_location_message))
            .create()
}