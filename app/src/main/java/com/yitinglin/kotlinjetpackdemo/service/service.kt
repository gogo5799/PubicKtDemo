package com.yitinglin.kotlinjetpackdemo.service

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.yitinglin.kotlinjetpackdemo.service.network.CheckNetworkConnection

public fun Context.toastShow(message: String, duration: Int) {
    val toast= Toast.makeText(this,message,duration)
    toast.setGravity(Gravity.CENTER,0,0)
    toast.show()
}

public fun ProgressBar.show(){
    visibility=View.VISIBLE
}

public fun ProgressBar.hide(){
    visibility=View.GONE
}

//測試連接網路狀態
public fun  internetConnectivity(context: Context):Boolean{
    return if(CheckNetworkConnection.checkInternetConnection(context)){
        true
    }else{
        context.toastShow("你已離線,請檢查網路連線...",500)
        false
    }
}