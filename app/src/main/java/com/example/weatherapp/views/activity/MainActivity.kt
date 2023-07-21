package com.example.weatherapp.views.activity

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
import com.example.weatherapp.App
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.views.AbstractScrollViewFragment
import com.example.weatherapp.views.custom.ToolbarController
import com.example.weatherapp.views.mainfragment.MainFragment
import kotlin.collections.set

class MainActivity : BaseActivity(), ToolbarCallback {

    private lateinit var binding: ActivityMainBinding

    private var scrollPositionsMap = mutableMapOf<String, Int>()

    override fun setDefaultTownLabel() {
        binding.customToolbar.setDefaultTownLabel()
    }

    override fun setTownLabel(label: String, isUpdate: Boolean, isShow: Boolean) {
        binding.customToolbar.setTownLabel(label, isUpdate, isShow)
    }

    override fun hideProgressToolbar() {
        binding.customToolbar.hideProgressBar()
    }

    override fun showProgressToolbar() {
        binding.customToolbar.showProgressBar()
    }

    override fun setToolbarProgress(progress: Int) {
        binding.customToolbar.setProgress(progress)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        scrollPositionsMap = if (savedInstanceState == null) {
            mutableMapOf()
        } else {
            val keys = savedInstanceState.getStringArray("keys") ?: emptyArray()
            val values = savedInstanceState.getIntArray("values")?.toTypedArray() ?: emptyArray()
            val mutableMap = mutableMapOf<String, Int>()
            for (i in keys.indices) {
                mutableMap[keys[i]] = values[i]
            }
            mutableMap
        }
        val name = (applicationContext as App).getUsersLocationName()
        setTownLabel(name!!, isUpdate = true, isShow = true)
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCallback, false)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MainFragment(), "MainFragment")
                .commit()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val keys = scrollPositionsMap.keys.toTypedArray()
        val values = scrollPositionsMap.values.toIntArray()
        outState.putStringArray("keys", keys)
        outState.putIntArray("values", values)
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentCallback)
    }

    private val fragmentCallback = object : FragmentLifecycleCallbacks() {

        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
            super.onFragmentResumed(fm, f)

            if (f is ToolbarController) {
                binding.customToolbar.registerListener(f)
            } else if (f !is DialogFragment) {
                binding.customToolbar.unregisterListener()
            }
            val tag = f.tag
            if (tag != null) {
                if (f is AbstractScrollViewFragment) {
                    f.setScrollViewPosition(0, scrollPositionsMap[tag] ?: 0)
                }
            }
        }

        override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
            super.onFragmentPaused(fm, f)
            val tag = f.tag
            if (tag != null) {
                if (f is AbstractScrollViewFragment) {
                    scrollPositionsMap[tag] = f.getScrollViewPosition().second
                }
            }
        }
    }

    override fun onNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return super.onNavigateUp()
    }
}