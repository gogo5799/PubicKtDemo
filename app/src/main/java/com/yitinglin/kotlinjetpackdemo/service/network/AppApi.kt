package com.yitinglin.kotlinjetpackdemo.service.network

import com.yitinglin.kotlinjetpackdemo.model.ClientUser
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AppApi {

    //20210120
    @POST("api/user" )
    suspend fun getUser(@Header("Authorization") h1:String, @Body user: ClientUser): Response<ClientUser>

    companion object{
        operator fun invoke(
        ) :AppApi{

            val appConfig=AppConfig()

            return Retrofit.Builder()
                .baseUrl(appConfig.appServerUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AppApi::class.java)
        }

    }
}
