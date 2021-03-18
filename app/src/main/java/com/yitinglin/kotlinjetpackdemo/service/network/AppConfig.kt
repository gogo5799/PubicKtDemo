package com.yitinglin.kotlinjetpackdemo.service.network

class AppConfig {
    private val privateID=PrivateID()
    //app後端api
    val appServerUrl:String= "http://192.168.0.105:8080/"
    //中央氣象局 天氣預報api
    val weatherInfoUrl:String= "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-D0047-065?Authorization=${privateID.weatherAuth}"
}


private class PrivateID{
    val weatherAuth:String=""
}