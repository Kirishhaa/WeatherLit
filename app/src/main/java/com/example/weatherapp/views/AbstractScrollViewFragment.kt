package com.example.weatherapp.views

import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import androidx.annotation.LayoutRes
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.example.weatherapp.R

abstract class AbstractScrollViewFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {

    protected lateinit var scrollView: NestedScrollView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scrollView = view.findViewById(R.id.scrollView)
    }

    fun setScrollViewPosition(scrollX: Int, scrollY: Int) {
        scrollView.postDelayed({
            scrollView.scrollTo(scrollX, scrollY)
        }, 100)
    }

    fun getScrollViewPosition(): Pair<Int, Int> {
        return Pair(scrollView.scrollX, scrollView.scrollY)
    }

}