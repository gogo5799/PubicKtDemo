package com.yitinglin.kotlinjetpa

import com.yitinglin.kotlinjetpackdemo.model.ClientUser
import com.yitinglin.kotlinjetpackdemo.model.WeatherInfo
import com.yitinglin.kotlinjetpackdemo.model.WeatherInfoResponse
import retrofit2.Response
import java.net.SocketTimeoutException

abstract class SafeApiRequests{
    suspend fun apiUserRequests(call:suspend()->Response<ClientUser>): ClientUser? {
        val getClientUser:ClientUser= ClientUser()
        try {
            val userResponse=call.invoke()
            return if (userResponse.isSuccessful) {
                userResponse.body()
            }else{
                userResponse.body()?.errorCode =-12
                userResponse.body()
            }
        }catch (es: SocketTimeoutException){
            getClientUser.errorCode =-13
            return getClientUser
        }catch (et: Throwable){
            getClientUser.errorCode =-14
            return getClientUser
        }
    }

    suspend fun apiWeatherRequests(call:suspend()-> Response<WeatherInfo>): WeatherInfoResponse {
        val getWeatherInfoResponse:WeatherInfoResponse= WeatherInfoResponse()
        try {
            val weatherResponse = call.invoke()
            return if (weatherResponse.isSuccessful){
                getWeatherInfoResponse.responseCode=1//weatherResponse isSuccessful
                if (weatherResponse.body()!=null){
                    getWeatherInfoResponse.weatherInfo=weatherResponse.body() as WeatherInfo
                    getWeatherInfoResponse
                }else {
                    getWeatherInfoResponse.responseCode=-15
                    getWeatherInfoResponse
                }
            }else  {
                getWeatherInfoResponse.responseCode=-12
                getWeatherInfoResponse
            }
        }catch (es: SocketTimeoutException){
            getWeatherInfoResponse.responseCode=-13
            return getWeatherInfoResponse
        }catch (et: Throwable){
            getWeatherInfoResponse.responseCode=-14
            return getWeatherInfoResponse
        }
    }
}
