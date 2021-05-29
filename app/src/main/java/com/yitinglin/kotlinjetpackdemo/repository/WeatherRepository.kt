package com.yitinglin.kotlinjetpackdemo.repository

import com.yitinglin.kotlinjetpa.SafeApiRequests
import com.yitinglin.kotlinjetpackdemo.model.WeatherInfoResponse
import com.yitinglin.kotlinjetpackdemo.service.network.AppApi

class WeatherRepository(
    private val api: AppApi
) : SafeApiRequests() {
    suspend fun weatherLiveData() : WeatherInfoResponse {
        return apiWeatherRequests { api.getWeather() }
    }
}