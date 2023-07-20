package com.example.weatherapp.views.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.App
import com.example.weatherapp.R
import com.example.weatherapp.data.PresenterManager
import com.example.weatherapp.databinding.FragmentSettingsBinding
import com.example.weatherapp.mvcadapters.OuterSettingsAdapter
import com.example.weatherapp.mvcadapters.OuterSettingsAdapter.SettingsElementsItem
import com.example.weatherapp.presenters.settings.SettingsPresenter
import com.example.weatherapp.views.AbstractScrollViewFragment
import com.example.weatherapp.views.activity.BaseActivity
import com.example.weatherapp.views.activity.ToolbarCallback
import com.example.weatherapp.views.custom.ToolbarNavigateUpController
import com.example.weatherapp.views.custom.ToolbarTitleController

class SettingsFragment : AbstractScrollViewFragment(R.layout.fragment_settings), SettingsView,
    ToolbarTitleController, ToolbarNavigateUpController {

    private lateinit var binding: FragmentSettingsBinding

    private lateinit var presenter: SettingsPresenter
    private lateinit var presentersManager: PresenterManager

    private var toolbarCallback: ToolbarCallback? = null

    private val adapter by lazy { OuterSettingsAdapter(presenter) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        toolbarCallback = context as ToolbarCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = requireActivity().application as App
        presentersManager = app.presenterManager
        presenter = if (savedInstanceState == null) {
            SettingsPresenter()
        } else {
            presentersManager.restorePresenter(savedInstanceState) as SettingsPresenter
        }
        presenter.setModel(app.settingsElementRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        presenter.bindView(this)
        setSettingsLabel()
    }

    override fun onStop() {
        super.onStop()
        presenter.unbindView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presentersManager.savePresenter(presenter, outState)
    }

    override fun onDetach() {
        super.onDetach()
        toolbarCallback = null
    }

    override fun setSettingItems(settingElements: List<SettingsElementsItem>) {
        adapter.setSettingElements(settingElements)
    }

    override fun changeLocals(localeValue: String) {
        (requireActivity() as BaseActivity).changeLocals(localeValue)
    }

    override fun setSettingsLabel() {
        toolbarCallback?.setTownLabel(
            resources.getString(R.string.settings),
            isUpdate = false,
            isShow = true
        )
    }

    override fun onNavigateUp() {
        requireActivity().onNavigateUp()
    }

}