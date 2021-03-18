package com.yitinglin.kotlinjetpackdemo.repository

import com.yitinglin.kotlinjetpackdemo.model.ClientUser


interface UserListener {
    fun onStarted()
    fun onSuccess(userResponse: ClientUser)
    fun onSuccessMsg(message:String)
    fun onFailure(message:String)
    fun onFailure(responseCode:Int)
    fun onGetUserDaoSuccess(userDaoData: ClientUser)
}