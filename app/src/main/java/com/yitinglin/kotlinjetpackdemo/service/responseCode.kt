package com.yitinglin.kotlinjetpackdemo.service

fun responseCode(errorCode:Int):String {
    return when(errorCode){
        1->{
            "註冊成功"
        }
        2->{
            "更新成功"
        }
        3->{
            "取得資料成功"
        }
        4->{
            "登入成功"
        }
        -1->{
             "此Google帳號尚未在此app註冊!!"
        }
        -3->{
             "此Facebook帳號尚未在此app註冊!!"
        }
        -8->{
            "無法判定sysType"
        }
        -9->{
            "註冊失敗"
        }
        -10->{
            "已註冊過"
        }
        -11->{
            "更新失敗"
        }
        -12->{
            "連接失敗"
        }
        -13->{
            "連接伺服器失敗，請聯絡系統管理員!"
        }
        -14->{
            "連接發生異常"
        }
       else->{
            "111111"
       }
    }
}