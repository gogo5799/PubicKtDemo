package com.yitinglin.kotlinjetpa

import com.yitinglin.kotlinjetpackdemo.model.ClientUser
import retrofit2.Response
import java.net.SocketTimeoutException

abstract class SafeApiRequests{
     suspend fun apiRequests(call:suspend()->Response<ClientUser>): ClientUser? {
         val getClientUser:ClientUser= ClientUser()
          try {
//             val userResponse = UserRepository().userLiveData(sendClientUser)
             val userResponse=call.invoke()
              return if (userResponse.isSuccessful) {
                  userResponse.body()
              }else{
                  userResponse.body()?.errorCode =-12
                  userResponse.body()
              }
         }catch (es: SocketTimeoutException){
             getClientUser.errorCode =-13
              return getClientUser
         }catch (et: Throwable){
             getClientUser.errorCode =-14
              return getClientUser
         }
     }

}
