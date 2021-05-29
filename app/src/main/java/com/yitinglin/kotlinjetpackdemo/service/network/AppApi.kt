package com.yitinglin.kotlinjetpackdemo.service.network

import com.yitinglin.kotlinjetpackdemo.model.ClientUser
import com.yitinglin.kotlinjetpackdemo.model.WeatherInfo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.concurrent.TimeUnit



const val getId= ""

interface AppApi {

    //20210120
    @POST("api/user")
    suspend fun getUser(
        @Header("Authorization") h1: String,
        @Body user: ClientUser
    ): Response<ClientUser>

    @GET("api/v1/rest/datastore/F-D0047-065?Authorization=$getId")
    suspend fun getWeather(): Response<WeatherInfo>

    companion object {
        private val appConfig = AppConfig()
        private val weatherUrl: String = appConfig.weatherInfoUrl
        private val userUrl: String = appConfig.appServerUrl
        val weatherClient by lazy {
            AppApi.invoke(weatherUrl)
        }
        val userClient by lazy {
            AppApi.invoke(userUrl)
        }

        private val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        operator fun invoke(baseUrl: String): AppApi {

            val client = OkHttpClient.Builder().apply {
                addNetworkInterceptor(loggingInterceptor)
                connectTimeout(15, TimeUnit.SECONDS)//15S
            }.build()

            return Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AppApi::class.java)
        }
    }
}
