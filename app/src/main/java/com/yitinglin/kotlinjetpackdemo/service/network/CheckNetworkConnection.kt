package com.yitinglin.kotlinjetpackdemo.service.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.yitinglin.kotlinjetpackdemo.service.toastShow

//測試連接網路狀態物件
object CheckNetworkConnection {
     fun checkInternetConnection(context: Context):Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork?:return false
        val activeNetwork=connectivityManager.getNetworkCapabilities(network)?:return false
        return when{
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)->true
            else->false
        }
    }
}