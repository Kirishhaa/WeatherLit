package com.example.weatherapp.views.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import com.example.weatherapp.R

class RequirePermissionDialog : AbstractCancellableDialog(REQUEST_KEY) {

    companion object {
        const val REQUEST_KEY = "RequirePermissionDialogRequestKey"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(context)
            .setCancelable(true)
            .setTitle(resources.getString(R.string.require_accept_permission_title))
            .setMessage(resources.getString(R.string.require_accept_permission_message))
            .create()
}