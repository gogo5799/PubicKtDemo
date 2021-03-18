package com.yitinglin.kotlinjetpackdemo.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.yitinglin.kotlinjetpackdemo.R
import com.yitinglin.kotlinjetpackdemo.model.WeatherInfo
import com.yitinglin.kotlinjetpackdemo.service.internetConnectivity
import com.yitinglin.kotlinjetpackdemo.service.whenState
import com.yitinglin.kotlinjetpackdemo.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.fragment_weather1.*
import kotlinx.android.synthetic.main.fragment_weather3.*

class Weather3Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather3, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progressBar3.visibility = View.VISIBLE
        mainContainer3.visibility = View.GONE
        if (internetConnectivity(requireContext())) getWeatherData()
    }

    private fun getWeatherData() {
        val getViewModel: WeatherViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(WeatherViewModel::class.java)
        getViewModel.weatherInfo.observe(viewLifecycleOwner, Observer {
            Log.i("tvShow2() getViewModel it", it.toString())
            val getWeatherData = Gson().fromJson(it.toString(), WeatherInfo::class.java)
            if (getWeatherData.success == "true") {
                progressBar3.visibility = View.GONE
                mainContainer3.visibility = View.VISIBLE
                address_city3.text = getWeatherData.records.locations[0].locationsName
                address_district3.text =
                    getWeatherData.records.locations[0].location[0].locationName
                //天氣圖
                val getImage =
                    whenState(getWeatherData.records.locations[0].location[0].weatherElement[1].time[2].elementValue[1].value.toInt())
                if (getImage != -99) {
                    weather_img3.setImageResource(getImage)
                }
                //天氣
                weather_status3.text =
                    getWeatherData.records.locations[0].location[0].weatherElement[1].time[2].elementValue[0].value
                //溫度
                weather_temp3.text =
                    getWeatherData.records.locations[0].location[0].weatherElement[3].time[2].elementValue[0].value
                //降雨機率
                weather3_PoP12h.text =
                    getWeatherData.records.locations[0].location[0].weatherElement[0].time[2].elementValue[0].value
                //相對濕度
                weather3_RH.text =
                    getWeatherData.records.locations[0].location[0].weatherElement[4].time[2].elementValue[0].value
                //舒適度指數
                weather3_CI.text =
                    getWeatherData.records.locations[0].location[0].weatherElement[5].time[2].elementValue[0].value
                //風速
                weather3_WS.text =
                    getWeatherData.records.locations[0].location[0].weatherElement[8].time[2].elementValue[0].value
                //風向
                weather3_WD.text =
                    getWeatherData.records.locations[0].location[0].weatherElement[9].time[2].elementValue[0].value
                //露點溫度
                weather3_Td.text =
                    getWeatherData.records.locations[0].location[0].weatherElement[10].time[2].elementValue[0].value
            } else {
                progressBar3.visibility = View.VISIBLE
                mainContainer3.visibility = View.GONE
            }
        })
        getViewModel.weatherInfo.value ?: getViewModel.fetchWeatherDataViewModel()
    }

}