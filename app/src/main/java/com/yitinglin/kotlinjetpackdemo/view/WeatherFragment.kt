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
import com.yitinglin.kotlinjetpackdemo.model.WeatherInfoResponse
import com.yitinglin.kotlinjetpackdemo.repository.WeatherListener
import com.yitinglin.kotlinjetpackdemo.service.getTime4String
import com.yitinglin.kotlinjetpackdemo.service.internetConnectivity
import com.yitinglin.kotlinjetpackdemo.service.responseCode
import com.yitinglin.kotlinjetpackdemo.service.toastShow
import com.yitinglin.kotlinjetpackdemo.viewmodel.WeatherInfoViewModel
import kotlinx.android.synthetic.main.fragment_weather.*
import java.util.*


class WeatherFragment : Fragment() , WeatherListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (internetConnectivity(requireContext())) getWeatherInfo()

    }

    private fun getWeatherInfo() {
        val weatherDataViewModel =ViewModelProvider(this).get(WeatherInfoViewModel::class.java)
        weatherDataViewModel.onWeatherApi(requireContext())
        weatherDataViewModel.weatherListener = this
    }

    override fun onStarted() {
//        progressBarWeather.show()
//        linearLayoutWeather.visibility = View.GONE
    }

    override fun onSuccess(weatherResponse: WeatherInfoResponse) {
//        progressBarWeather.hide()
//        linearLayoutWeather.visibility = View.VISIBLE

        if (weatherResponse.responseCode == 1) {
//            context?.toastShow("weatherResponse=1  locationsName=> ${weatherResponse.weatherInfo.records.locations[0].locationsName}", 1000)

            viewPager2.adapter= object : FragmentStateAdapter(this) {
                override fun getItemCount() = 3

                override fun createFragment(position: Int): Fragment {
                    val fragment =  Weather1Fragment()
                    fragment.arguments = Bundle().apply {
                        // Our object is just an integer :-P
                        putInt("selectItem", position + 1)
                        putSerializable("weatherResponse",weatherResponse)
//                        context?.toastShow("aa+${position+1}",500)
                    }
                    return fragment
                }

            }


            val date:Date=Date()

            tabLayout?.let {
                viewPager2?.let { it1 ->
                    TabLayoutMediator(it, it1) { tab, position ->
                        when (position) {
                            0 -> tab.text = date.getTime4String(weatherResponse.weatherInfo.records.locations[0].location[0].weatherElement[0].time[0].startTime)
                            1 -> tab.text = date.getTime4String(weatherResponse.weatherInfo.records.locations[0].location[0].weatherElement[0].time[1].startTime)
                            else -> tab.text = date.getTime4String(weatherResponse.weatherInfo.records.locations[0].location[0].weatherElement[0].time[2].startTime)
                        }
                    }.attach()
                }
            }

        } else  context?.toastShow(responseCode(weatherResponse.responseCode),500)
    }

    override fun onFailure(message: String) {
        context?.toastShow("error=>$message", 500)
    }

    override fun onFailure(responseCode: Int) {
        context?.toastShow(responseCode(responseCode),500)
    }
}




