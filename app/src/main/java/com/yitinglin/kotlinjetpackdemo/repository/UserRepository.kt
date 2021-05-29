package com.yitinglin.kotlinjetpackdemo.repository

import com.yitinglin.kotlinjetpa.SafeApiRequests
import com.yitinglin.kotlinjetpackdemo.model.ClientUser
import com.yitinglin.kotlinjetpackdemo.service.db.AppDatabase
import com.yitinglin.kotlinjetpackdemo.service.db.entities.User
import com.yitinglin.kotlinjetpackdemo.service.network.AppApi
import okhttp3.Credentials

class UserRepository(
    private val api:AppApi,
) : SafeApiRequests() {
    var basic: String = Credentials.basic("root", "root")
    suspend fun userLiveData(getUserData: ClientUser) : ClientUser? {
        return apiUserRequests {  api.getUser(basic,getUserData)}
    }
}




