package com.yitinglin.kotlinjetpackdemo.repository

import com.yitinglin.kotlinjetpackdemo.model.WeatherInfoResponse

interface WeatherListener {
    fun onStarted()
    fun onSuccess(weatherResponse: WeatherInfoResponse)
    fun onFailure(message:String)
    fun onFailure(responseCode:Int)
}