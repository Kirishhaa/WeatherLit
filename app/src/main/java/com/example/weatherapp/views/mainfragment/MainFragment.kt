package com.example.weatherapp.views.mainfragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkInfo
import com.example.weatherapp.App
import com.example.weatherapp.R
import com.example.weatherapp.data.AdditionalInfo
import com.example.weatherapp.data.AverageDayOfWeek
import com.example.weatherapp.data.ForecastedWeatherItem
import com.example.weatherapp.databinding.FragmentMainBinding
import com.example.weatherapp.mvcadapters.AdditionalInfoAdapter
import com.example.weatherapp.presenters.MainPresenter
import com.example.weatherapp.recycler.weekday.WeekDaysAdapter
import com.example.weatherapp.utils.ErrorResult
import com.example.weatherapp.utils.FinalResult
import com.example.weatherapp.utils.SuccessfulResult
import com.example.weatherapp.utils.permissions
import com.example.weatherapp.views.AbstractScrollViewFragment
import com.example.weatherapp.views.activity.ToolbarCallback
import com.example.weatherapp.views.custom.ToolbarSettingsController
import com.example.weatherapp.views.custom.ToolbarSyncController
import com.example.weatherapp.views.custom.ToolbarTitleController
import com.example.weatherapp.views.dialog.AbstractCancellableDialog
import com.example.weatherapp.views.dialog.RequireLocationDialog
import com.example.weatherapp.views.dialog.RequirePermissionDialog
import com.example.weatherapp.views.forecastedfragment.ForecastedListFragment
import com.example.weatherapp.views.forecastedfragment.ForecastedListFragment.Companion.ARG_FORECASTED_LIST
import com.example.weatherapp.views.forecastedfragment.ForecastedListFragment.Companion.ARG_SCROLLED_POSITION
import com.example.weatherapp.views.settings.SettingsFragment

class MainFragment : AbstractScrollViewFragment(R.layout.fragment_main), MainView,
    ToolbarTitleController, ToolbarSyncController, ToolbarSettingsController {

    private var toolbarCallback: ToolbarCallback? = null

    private lateinit var binding: FragmentMainBinding

    private lateinit var adapterWeekDays: WeekDaysAdapter
    private lateinit var adapterAddInfo: AdditionalInfoAdapter

    private lateinit var presenter: MainPresenter
    private val presenterManager by lazy { (requireActivity().application as App).presenterManager }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        toolbarCallback = context as ToolbarCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContext = requireContext().applicationContext

        setFragmentResultListener(RequireLocationDialog.REQUEST_KEY) { _, bundle ->
            val isCancelled = bundle.getBoolean(AbstractCancellableDialog.ARG_IS_CANCELLED)

            if (isCancelled) {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }

        setFragmentResultListener(RequirePermissionDialog.REQUEST_KEY) { _, bundle ->
            val isCancelled = bundle.getBoolean(AbstractCancellableDialog.ARG_IS_CANCELLED)

            if (isCancelled) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }

        presenter = if (savedInstanceState == null) {
            MainPresenter(appContext)
        } else {
            presenterManager.restorePresenter(savedInstanceState) as? MainPresenter
                ?: MainPresenter(appContext)
        }
        val app = (appContext as App)
        presenter.setModel(app.weatherRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        binding.addInfoRecycler.layoutManager = GridLayoutManager(context, 2)
        binding.weekDayRecycler.layoutManager = LinearLayoutManager(context)
        binding.seeMoreForecasted.setOnClickListener {
            presenter.onSeeMorePressed()
        }
        binding.tvForecast.setOnClickListener {
            presenter.onSeeMorePressed()
        }
        adapterAddInfo = AdditionalInfoAdapter(resources)
        adapterWeekDays = WeekDaysAdapter(presenter, resources)
        binding.addInfoRecycler.adapter = adapterAddInfo
        binding.weekDayRecycler.adapter = adapterWeekDays
    }

    override fun onStart() {
        super.onStart()
        presenter.bindView(this)
        toolbarCallback?.setDefaultTownLabel()
    }

    override fun onStop() {
        super.onStop()
        presenter.unbindView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenterManager.savePresenter(presenter, outState)
    }

    override fun onDetach() {
        super.onDetach()
        toolbarCallback = null
    }

    override fun setDegreeLabel(label: String) {
        binding.mainDegreeLabel.text = label
    }

    override fun setTypeWeatherLabel(label: String) {
        binding.mainTypeWeatherLabel.text = label
    }

    override fun goToForecastedWeather(
        forecasted: List<ForecastedWeatherItem>,
        scrolledPosition: Int,
    ) {
        val fr = ForecastedListFragment()
        fr.arguments = bundleOf(
            ARG_FORECASTED_LIST to forecasted,
            ARG_SCROLLED_POSITION to scrolledPosition
        )
        launchFragmentWithBackStack(fr, "ForecastedListFragment")
    }

    override fun showCurrentWeatherView(show: Boolean) {
        binding.mainDegreeLabel.isVisible = show
        binding.mainTypeWeatherLabel.isVisible = show
        binding.currentWeatherProgressBar.isVisible = !show
    }

    override fun showWeekDayWeatherView(show: Boolean) {
        binding.weekDayRecycler.isVisible = show
        binding.weekDaysProgressBar.isVisible = !show
    }

    override fun setDaysOfWeek(dayOfWeek: List<AverageDayOfWeek>) {
        adapterWeekDays.setDaysOfWeek(dayOfWeek)
    }

    override fun runLocationLauncher() {
        RequireLocationDialog().show(parentFragmentManager, null)
    }

    override fun runPermissionsLauncher() {
        val app = (requireContext().applicationContext as App)
        val requestCount = app.getRequestedPermissionsCount()

        if (requestCount < 2) {
            requestPermissions(permissions, 1141)
            app.incrementRequestedPermissions()
        } else {
            RequirePermissionDialog().show(parentFragmentManager, null)
        }
    }

    override fun onStartLoad() {
        toolbarCallback?.showProgressToolbar()
    }

    override fun setProgress(progress: Int) {
        toolbarCallback?.setToolbarProgress(progress)
    }

    override fun onFinishLoad(result: FinalResult<WorkInfo>) {
        Log.d("MainFragment", "onFinishLoad")
        toolbarCallback?.hideProgressToolbar()
        when (result) {
            is SuccessfulResult -> {
                val locationName =
                    (requireContext().applicationContext as App).getUsersLocationName()!!
                val isShow = this.isVisible
                toolbarCallback?.setTownLabel(locationName, true, isShow)
            }
            is ErrorResult -> {
                Toast.makeText(
                    context,
                    resources.getString(R.string.synchronize_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun isNotOnline() {
        Toast.makeText(context, resources.getString(R.string.is_not_online), Toast.LENGTH_SHORT)
            .show()
    }

    override fun doOnIsntTime() {
        Toast.makeText(context, resources.getString(R.string.is_not_time), Toast.LENGTH_SHORT)
            .show()
    }


    override fun onSettingsPressed() {
        launchFragmentWithBackStack(SettingsFragment(), "SettingsFragment")
    }

    private fun launchFragmentWithBackStack(fragment: Fragment, tag: String) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.enter_forecasted_list,
                R.anim.exit_forecasted_list,
                R.anim.enter_pop_forecasted_list,
                R.anim.exit_pop_forecasted_list
            )
            .replace(R.id.fragmentContainer, fragment, tag)
            .addToBackStack(null)
            .commit()
    }

    override fun onSynchronizePressed() = presenter.sync(true)

    override fun showAddInfo(show: Boolean) {
        binding.addInfoRecycler.isVisible = show
        binding.addInfoProgressBar.isVisible = !show
    }

    override fun setAddInfoList(additionalInfoList: List<AdditionalInfo>) {
        adapterAddInfo.setAdditionalInfoList(additionalInfoList)
    }
}