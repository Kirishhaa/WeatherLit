package com.example.weatherapp.views.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.weatherapp.App
import com.example.weatherapp.views.dialog.AbstractCancellableDialog
import com.example.weatherapp.views.dialog.RequireLocationDialog
import com.example.weatherapp.views.dialog.RequirePermissionDialog
import java.util.*

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = applicationContext as App
        val localeValue = app.getLocaleValue()
        changeLocals(localeValue)
    }

    fun listenRequirePermissionDialog(onFinish: () -> Unit) {
        supportFragmentManager.setFragmentResultListener(RequirePermissionDialog.REQUEST_KEY, this) { _, bundle ->
            val isCancelled = bundle.getBoolean(AbstractCancellableDialog.ARG_IS_CANCELLED)
            if (isCancelled) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
                onFinish()
            }
        }
    }

    fun changeLocals(localeValue: String) {
        val newLocale = Locale(localeValue)
        Locale.setDefault(newLocale)
        val app = applicationContext as App
        app.saveLocaleValue(localeValue)
        val config = android.content.res.Configuration()
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }

}