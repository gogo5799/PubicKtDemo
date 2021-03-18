package com.yitinglin.kotlinjetpackdemo.service.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yitinglin.kotlinjetpackdemo.model.ClientBUD
import com.yitinglin.kotlinjetpackdemo.model.ClientUser

//const val CURRENT_USER_ID=0

@Entity
data class User(
    var loginType:Int=0, //1-> google 2-> fb ...
    @PrimaryKey(autoGenerate = false) var sysId:Long=0,
    var email:String="",
    var googleUnique:String="",
    var fbUnique:String="",
    var name:String="",
    var gender:String="",
    var tel:String="",
    var photo :String=""
)
