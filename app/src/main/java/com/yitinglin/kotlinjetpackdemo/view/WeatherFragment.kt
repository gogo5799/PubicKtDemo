package com.yitinglin.kotlinjetpackdemo.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


import com.yitinglin.kotlinjetpackdemo.R
//import com.yitinglin.kotlinjetpackdemo.model.Locations
//import com.yitinglin.kotlinjetpackdemo.model.WeatherElement
import com.yitinglin.kotlinjetpackdemo.model.WeatherInfo
import com.yitinglin.kotlinjetpackdemo.service.internetConnectivity
import com.yitinglin.kotlinjetpackdemo.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.fragment_weather.*


class WeatherFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (internetConnectivity(requireContext())) getWeatherData()

        viewPager2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 3

            //
            override fun createFragment(position: Int) =
                when (position) {
                    0 -> Weather1Fragment()
                    1 -> Weather2Fragment()
                    else -> Weather3Fragment()
                }
        }
    }

    private fun getWeatherData() {
        val getViewModel: WeatherViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(WeatherViewModel::class.java)

        getViewModel.weatherInfo.observe(viewLifecycleOwner, Observer {
            Log.i("tvShow2() getViewModel it", it.toString())
            val test = Gson().fromJson(it.toString(), WeatherInfo::class.java)
            if (test.success == "true") {
                tabLayout.visibility = View.VISIBLE
                TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
                    when (position) {
                        0 -> tab.text =
                            test.records.locations[0].location[0].weatherElement[0].time[0].startTime
                        1 -> tab.text =
                            test.records.locations[0].location[0].weatherElement[0].time[1].startTime
                        else -> tab.text =
                            test.records.locations[0].location[0].weatherElement[0].time[2].startTime
                    }
                }.attach()
            } else {
                tabLayout.visibility = View.GONE
            }
        })
        getViewModel.weatherInfo.value ?: getViewModel.fetchWeatherDataViewModel()
    }
}




