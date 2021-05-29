package com.yitinglin.kotlinjetpackdemo.service

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.yitinglin.kotlinjetpackdemo.service.network.CheckNetworkConnection
import java.util.*
import java.text.SimpleDateFormat

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

//轉換日期格式 字符串日期
public fun Date.getTime4String(time: String):String{
    //輸出格式.format(輸入格式.parse(輸入字串))
    return SimpleDateFormat("yyyy/MM/dd HH:mm").format(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time))


}