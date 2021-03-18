package com.yitinglin.kotlinjetpackdemo.service

import com.yitinglin.kotlinjetpackdemo.R
import java.net.URI
import java.util.*


fun whenState(getState:Int): Int {
    when (getState) {
        42 -> {
            //下雪
            return R.drawable.ic_snowflake
        }
         in 1..4 -> {
            //晴天
            return R.drawable.ic_sun
         }
        in 5..7 -> {
            //陰天
            return R.drawable.ic_cloud
        }
        in 8..22 -> {
            //雨天
            return R.drawable.ic_rain
        }
        23 -> {
            //陰有雨或雪
            return R.drawable.ic_rain//
        }
        in 24..32 -> {
            //有霧
            return R.drawable.ic_rain//
        }
        in 33..41 -> {
            //短暫陣雨或雷雨有霧
            return R.drawable.ic_rain//
        }
        else -> {
            return -99
        }
    }
}





