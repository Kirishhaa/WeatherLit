package com.example.weatherapp.views.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.work.WorkInfo
import com.example.weatherapp.App
import com.example.weatherapp.R
import com.example.weatherapp.presenters.SplashActivityPresenter
import com.example.weatherapp.utils.ErrorResult
import com.example.weatherapp.utils.FinalResult
import com.example.weatherapp.utils.SuccessfulResult
import com.example.weatherapp.utils.permissions
import com.example.weatherapp.views.LoadWorkerView
import com.example.weatherapp.views.dialog.AbstractCancellableDialog
import com.example.weatherapp.views.dialog.RequireLocationDialog
import com.example.weatherapp.views.dialog.SplashWeatherTroubleDialog

class SplashWeatherActivity : BaseActivity(), LoadWorkerView {

    private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>

    private var presenter: SplashActivityPresenter? = null

    private val presenterManager by lazy { (application as App).presenterManager }

    private val activityExecutor = TaskExecutor<AppCompatActivity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionsLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                presenter?.sync(false)
            }

        val locationLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                presenter?.sync(false)
            }

        supportFragmentManager.setFragmentResultListener(
            RequireLocationDialog.REQUEST_KEY,
            this
        ) { _, bundle ->
            val isCancelled = bundle.getBoolean(AbstractCancellableDialog.ARG_IS_CANCELLED)
            if (isCancelled) {
                locationLauncher.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }

        presenter = if (savedInstanceState == null) {
            SplashActivityPresenter(applicationContext)
        } else {
            presenterManager.restorePresenter(savedInstanceState) as SplashActivityPresenter
        }
        presenter?.bindView(this)
        presenter?.sync(false)
    }

    override fun onStart() {
        super.onStart()
        activityExecutor.registerObject(this)
    }

    override fun onStop() {
        super.onStop()
        activityExecutor.unregisterObject()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val presenter = this.presenter ?: return
        presenterManager.savePresenter(presenter, outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        val presenter = this.presenter ?: return
        presenter.unbindView()
    }

    private fun showTrouble(buttonTextRes: Int, messageTextRes: Int) {
        SplashWeatherTroubleDialog.onInstance(buttonTextRes, messageTextRes)
            .show(supportFragmentManager, null)
    }

    private fun navigateToMain() = activityExecutor.execute {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStartLoad() {}

    override fun onFinishLoad(result: FinalResult<WorkInfo>) {
        val app = application as App
        when (result) {
            is SuccessfulResult -> {
                Log.d("SplashWeatherActivity", "SuccessResult")
                if (app.getIsFirstLaunch()) {
                    app.changeIsFirstLaunch()
                }
                navigateToMain()
            }
            is ErrorResult -> {
                if (app.getIsFirstLaunch()) {
                    showTrouble(R.string.exit, R.string.error_fst_launch)
                } else {
                    navigateToMain()
                }
            }
        }
    }

    override fun isNotOnline() {
        val app = application as App
        if (app.getIsFirstLaunch()) showTrouble(R.string.exit, R.string.error_fst_launch)
        else navigateToMain()
    }

    override fun doOnIsntTime() = navigateToMain()



    override fun runLocationLauncher() = RequireLocationDialog().show(supportFragmentManager, null)

    override fun runPermissionsLauncher() = activityExecutor.execute {
        val app = (applicationContext as App)
        app.getRequestedPermissionsCount()
        permissionsLauncher.launch(permissions)
        app.incrementRequestedPermissions()
    }

}