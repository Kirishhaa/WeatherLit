package com.example.weatherapp.views.forecastedfragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.App
import com.example.weatherapp.R
import com.example.weatherapp.data.ForecastedWeatherItem
import com.example.weatherapp.data.PresenterManager
import com.example.weatherapp.databinding.FragmentForecastedListBinding
import com.example.weatherapp.presenters.forecasted.ForecastedListPresenter
import com.example.weatherapp.recycler.forecasted.ForecastedAdapter
import com.example.weatherapp.views.AbstractScrollViewFragment
import com.example.weatherapp.views.activity.ToolbarCallback
import com.example.weatherapp.views.custom.ToolbarNavigateUpController
import com.example.weatherapp.views.custom.ToolbarTitleController

class ForecastedListFragment : AbstractScrollViewFragment(R.layout.fragment_forecasted_list),
    ForecastedListView, ToolbarTitleController, ToolbarNavigateUpController {

    private var toolbarCallback: ToolbarCallback? = null

    private lateinit var binding: FragmentForecastedListBinding

    companion object {
        const val ARG_FORECASTED_LIST = "argument_forecasted_list"
        const val ARG_SCROLLED_POSITION = "ARG_SCROLLED_POSITION"
    }

    private lateinit var presenter: ForecastedListPresenter

    private val presenterManager: PresenterManager by lazy { (requireActivity().application as App).presenterManager }

    private val adapter by lazy { ForecastedAdapter(resources) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        toolbarCallback = context as ToolbarCallback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentForecastedListBinding.bind(view)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = adapter

        presenter = if (savedInstanceState == null) {
            ForecastedListPresenter()
        } else {
            presenterManager.restorePresenter(savedInstanceState) as ForecastedListPresenter
        }

        val forecasted = requireArguments().getParcelableArrayList<ForecastedWeatherItem>(
            ARG_FORECASTED_LIST
        )!!
        presenter.setModel(forecasted)
    }

    override fun onStart() {
        super.onStart()
        presenter.bindView(this)
        toolbarCallback?.setTownLabel(
            resources.getString(R.string.forecasted_title),
            isUpdate = false,
            isShow = true
        )
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

    override fun setForecastedList(forecasted: List<ForecastedWeatherItem>) {
        val scrolledPosition = requireArguments().getInt(ARG_SCROLLED_POSITION)
        adapter.setForecasted(forecasted)
        binding.recyclerView.scrollToPosition(scrolledPosition)
    }

    override fun onNavigateUp() {
        requireActivity().onNavigateUp()
    }

}