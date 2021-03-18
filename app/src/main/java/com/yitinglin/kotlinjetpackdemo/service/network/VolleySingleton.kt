package com.yitinglin.kotlinjetpackdemo.service.network

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleySingleton(context: Context){
    companion object{
        private var INSTANCE : VolleySingleton?=null
        fun getInstance(context: Context)=
                VolleySingleton(context).also {
                        INSTANCE =it

                }
    }

    val requestQueue:RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }
}