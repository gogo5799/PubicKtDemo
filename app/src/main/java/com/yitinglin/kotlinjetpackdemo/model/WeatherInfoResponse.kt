package com.yitinglin.kotlinjetpackdemo.model

import java.io.Serializable

data class WeatherInfoResponse(
    var responseCode :Int =0,
    var weatherInfo: WeatherInfo = WeatherInfo()
):Serializable
