package com.yitinglin.kotlinjetpackdemo.model

import java.io.Serializable

data class ClientUser(
    var sysType: Int =0, //功能 1 新增user 2根據id換取user 3根據id更新user
    var loginType:Int=0,//1-> google 2-> fb ...
    var errorCode:Int=0,
    var clientBUD: ClientBUD=ClientBUD()
): Serializable