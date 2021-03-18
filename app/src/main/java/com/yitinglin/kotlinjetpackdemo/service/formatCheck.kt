package com.yitinglin.kotlinjetpackdemo.service

import android.widget.EditText

//是否為空或空字串 是:true ,否:false
public fun EditText.checkBlank(inputStr:String):Boolean{
    return inputStr.isBlank()
}

//是否為超過長度 是:true ,否:false
public fun EditText.checkLength(inputStr:String,maxLength:Int):Boolean{
    return inputStr.length >= maxLength
}