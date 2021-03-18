package com.yitinglin.kotlinjetpackdemo.model

import java.io.Serializable

data class ClientUserInfo (
        var name:String="",
        var gender:String="",
        var tel:String="",
        var photo :String=""
): Serializable