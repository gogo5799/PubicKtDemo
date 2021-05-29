package com.yitinglin.kotlinjetpackdemo.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yitinglin.kotlinjetpackdemo.model.WeatherInfoResponse
import com.yitinglin.kotlinjetpackdemo.repository.WeatherListener
import android.content.Context
import com.yitinglin.kotlinjetpackdemo.repository.WeatherRepository
import com.yitinglin.kotlinjetpackdemo.service.Coroutines
import com.yitinglin.kotlinjetpackdemo.service.network.AppApi
import com.yitinglin.kotlinjetpackdemo.service.network.ApiException
import com.yitinglin.kotlinjetpackdemo.service.network.NoInternetException

class WeatherInfoViewModel: ViewModel() {
    var weatherListener: WeatherListener? =null

    private val _weatherInfoResponse: MutableLiveData<WeatherInfoResponse> by lazy {
        MutableLiveData<WeatherInfoResponse>()
    }
    val weatherInfoResponse : LiveData<WeatherInfoResponse> = _weatherInfoResponse

    fun onWeatherApi(context: Context){
        weatherListener?.onStarted()

        //success
        Coroutines().main {
            try {
                val weatherRepository= WeatherRepository(AppApi.weatherClient)
                val weatherResponse=weatherRepository.weatherLiveData()
                _weatherInfoResponse.value = weatherResponse
                weatherListener?.onSuccess(weatherResponse)
            }catch (e: ApiException){
                weatherListener?.onFailure(e.message.toString())
            }catch (e: NoInternetException){
                weatherListener?.onFailure(e.message.toString())
            }
        }

    }
}

//class WeatherInfoViewModel(application: Application) : AndroidViewModel(application) {
//
//    private val _weatherInfo: MutableLiveData<JSONObject> by lazy {
//        MutableLiveData<JSONObject>()
//    }
//
//    val weatherInfo: LiveData<JSONObject> = _weatherInfo
//
//    fun fetchWeatherDataViewModel(){
//        val jsonRequest = object : JsonObjectRequest(
//                Request.Method.GET, getUrl(), null, Response.Listener {
//            try {
//                Log.i("test",it.toString())
//                 _weatherInfo.value= it
//
//            } catch (e: JSONException) {
//                Log.d("JSON err", "EXC: ${e.localizedMeage}")
//
//            }
//        }, Response.ErrorListener {
//            Log.d("Response err", "EXC: $it")
//
//        }) {}
//        VolleySingleton.getInstance(getApplication()).requestQueue.add(jsonRequest)
//    }
//
//    private fun getUrl():String{
//        val appConfig=AppConfig()
//        return  appConfig.weatherInfoUrl
//
//    }




//}
