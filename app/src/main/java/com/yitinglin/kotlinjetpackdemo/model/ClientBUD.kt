package com.yitinglin.kotlinjetpackdemo.model

import java.io.Serializable

data class ClientBUD (
        var sysId:Long=0,
        var email:String="",
        var googleUnique:String="",
        var fbUnique:String="",
        var clientUserInfo:ClientUserInfo=ClientUserInfo()
):Serializable