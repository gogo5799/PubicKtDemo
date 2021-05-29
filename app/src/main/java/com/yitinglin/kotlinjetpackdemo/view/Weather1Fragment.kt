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
import com.yitinglin.kotlinjetpackdemo.model.WeatherInfoResponse
import com.yitinglin.kotlinjetpackdemo.service.internetConnectivity
import com.yitinglin.kotlinjetpackdemo.service.toastShow
import com.yitinglin.kotlinjetpackdemo.viewmodel.WeatherInfoViewModel
import kotlinx.android.synthetic.main.fragment_weather1.*
import com.yitinglin.kotlinjetpackdemo.service.whenState

class Weather1Fragment : Fragment() {

    private var selectItem : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainContainer1.visibility=View.GONE
        progressBar1.visibility=View.VISIBLE

        arguments?.takeIf { it.containsKey("selectItem") }?.apply {
            selectItem=getInt("selectItem")
            if (selectItem == 0){
                context?.toastShow("selectItem==0",500)
            }else{
                arguments?.takeIf { it.containsKey("weatherResponse") }?.apply {
                    val getWeatherInfo =this.getSerializable("weatherResponse") as WeatherInfoResponse
                    if (getWeatherInfo.responseCode ==1){
                        //weatherResponse isSuccessful
                        weatherDisplay(getWeatherInfo)
                    }else{
                        mainContainer1.visibility=View.GONE
                        progressBar1.visibility=View.VISIBLE
                        context?.toastShow("error=>${getWeatherInfo.responseCode}",500)
                    }
                }
            }
        }


    }


    private fun weatherDisplay(weatherInfoResponse: WeatherInfoResponse) {
        mainContainer1.visibility=View.VISIBLE
        progressBar1.visibility=View.GONE
        address_city1.text=weatherInfoResponse.weatherInfo.records.locations[0].locationsName
        address_district1.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].locationName
        when(selectItem){
            1->{
                //天氣圖
                val getImage=whenState(weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[1].time[0].elementValue[1].value.toInt())
                if (getImage!=-99){
                    weather_img1.setImageResource(getImage)
                }
                //天氣
                weather_status1.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[1].time[0].elementValue[0].value
                //溫度
                weather_temp1.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[3].time[0].elementValue[0].value
                //降雨機率
                weather1_PoP12h.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[0].time[0].elementValue[0].value
                //相對濕度
                weather1_RH.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[4].time[0].elementValue[0].value
                //舒適度指數
                weather1_CI.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[5].time[0].elementValue[0].value
                //風速
                weather1_WS.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[8].time[0].elementValue[0].value
                //風向
                weather1_WD.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[9].time[0].elementValue[0].value
                //露點溫度
                weather1_Td.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[10].time[0].elementValue[0].value
            }
            2->{
                //天氣圖
                val getImage=whenState(weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[1].time[1].elementValue[1].value.toInt())
                if (getImage!=-99){
                    weather_img1.setImageResource(getImage)
                }
                //天氣
                weather_status1.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[1].time[1].elementValue[0].value
                //溫度
                weather_temp1.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[3].time[1].elementValue[0].value
                //降雨機率
                weather1_PoP12h.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[0].time[1].elementValue[0].value
                //相對濕度
                weather1_RH.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[4].time[1].elementValue[0].value
                //舒適度指數
                weather1_CI.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[5].time[1].elementValue[0].value
                //風速
                weather1_WS.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[8].time[1].elementValue[0].value
                //風向
                weather1_WD.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[9].time[1].elementValue[0].value
                //露點溫度
                weather1_Td.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[10].time[1].elementValue[0].value
            }
            else->{
                //天氣圖
                val getImage=whenState(weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[1].time[2].elementValue[1].value.toInt())
                if (getImage!=-99){
                    weather_img1.setImageResource(getImage)
                }
                //天氣
                weather_status1.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[1].time[2].elementValue[0].value
                //溫度
                weather_temp1.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[3].time[2].elementValue[0].value
                //降雨機率
                weather1_PoP12h.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[0].time[2].elementValue[0].value
                //相對濕度
                weather1_RH.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[4].time[2].elementValue[0].value
                //舒適度指數
                weather1_CI.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[5].time[2].elementValue[0].value
                //風速
                weather1_WS.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[8].time[2].elementValue[0].value
                //風向
                weather1_WD.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[9].time[2].elementValue[0].value
                //露點溫度
                weather1_Td.text=weatherInfoResponse.weatherInfo.records.locations[0].location[4].weatherElement[10].time[2].elementValue[0].value
            }
        }
    }
}