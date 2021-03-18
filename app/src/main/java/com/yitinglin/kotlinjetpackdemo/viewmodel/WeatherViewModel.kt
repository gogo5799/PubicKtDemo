package com.yitinglin.kotlinjetpackdemo.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.yitinglin.kotlinjetpackdemo.service.network.AppConfig
import com.yitinglin.kotlinjetpackdemo.service.network.VolleySingleton
import org.json.JSONException
import org.json.JSONObject

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val _weatherInfo: MutableLiveData<JSONObject> by lazy {
        MutableLiveData<JSONObject>()
    }

    val weatherInfo: LiveData<JSONObject> = _weatherInfo

    fun fetchWeatherDataViewModel(){
        val jsonRequest = object : JsonObjectRequest(
                Request.Method.GET, getUrl(), null, Response.Listener {
            try {
                Log.i("test",it.toString())
                 _weatherInfo.value= it

            } catch (e: JSONException) {
                Log.d("JSON err", "EXC: ${e.localizedMessage}")

            }
        }, Response.ErrorListener {
            Log.d("Response err", "EXC: $it")

        }) {}
        VolleySingleton.getInstance(getApplication()).requestQueue.add(jsonRequest)
    }

    private fun getUrl():String{
        val appConfig=AppConfig()
        return  appConfig.weatherInfoUrl

    }




}
