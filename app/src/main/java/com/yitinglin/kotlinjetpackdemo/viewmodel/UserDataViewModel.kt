package com.yitinglin.kotlinjetpackdemo.viewmodel

import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.yitinglin.kotlinjetpackdemo.model.ClientUser
import com.yitinglin.kotlinjetpackdemo.repository.UserListener
import com.yitinglin.kotlinjetpackdemo.repository.UserRepository
import com.yitinglin.kotlinjetpackdemo.service.Coroutines
import com.yitinglin.kotlinjetpackdemo.service.db.AppDatabase
import com.yitinglin.kotlinjetpackdemo.service.db.AppDatabase.Companion.getDatabase
import com.yitinglin.kotlinjetpackdemo.service.db.entities.User
import com.yitinglin.kotlinjetpackdemo.service.network.ApiException
import com.yitinglin.kotlinjetpackdemo.service.network.AppApi
import com.yitinglin.kotlinjetpackdemo.service.network.NoInternetException
import com.yitinglin.kotlinjetpackdemo.service.responseCode
import com.yitinglin.kotlinjetpackdemo.service.toastShow
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class UserDataViewModel : ViewModel() {
    var sysType = 0
    var loginType: Int = 0 //1-> google 2-> fb ...
    var googleUnique: String = ""
    var fbUnique: String = ""
    var userListener: UserListener? = null
    private val _userData: MutableLiveData<ClientUser> by lazy {
        MutableLiveData<ClientUser>()
    }

    val userData: LiveData<ClientUser> = _userData

    lateinit var db: AppDatabase

    fun onUserApi(context: Context, clientUser: ClientUser) {
        userListener?.onStarted()
        sysType = clientUser.sysType
        loginType = clientUser.loginType
        googleUnique = clientUser.clientBUD.googleUnique
        fbUnique = clientUser.clientBUD.fbUnique

        when (loginType) {
            0 -> {
                userListener?.onFailure("loginType is 0....")
                return
            }
            1 -> {
                if (googleUnique.isEmpty()) {
                    userListener?.onFailure("googleUnique is empty!!")
                    return
                }
            }
            2 -> {
                if (fbUnique.isEmpty()) {
                    userListener?.onFailure("fbUnique is empty!!")
                    return
                }
            }
            else -> {
                userListener?.onFailure("loginType is ?....$loginType")
                return
            }
        }

        //success
        Coroutines().main {
            try {
                val userRepository = UserRepository(AppApi.userClient)
                val userResponse = userRepository.userLiveData(clientUser)
                if (userResponse != null) {
                    if (userResponse.clientBUD.sysId > 0 && userResponse.errorCode >= 0) {
                        _userData.value = userResponse
                        val user: User = User()
                        user.sysId = userResponse.clientBUD.sysId
                        user.email = userResponse.clientBUD.email
                        user.loginType = userResponse.loginType
                        when (userResponse.loginType) {
                            1 -> {
                                user.googleUnique = userResponse.clientBUD.googleUnique
                            }
                            else -> {
                                user.fbUnique = userResponse.clientBUD.fbUnique
                            }
                        }
                        user.name = userResponse.clientBUD.clientUserInfo.name
                        user.gender = userResponse.clientBUD.clientUserInfo.gender
                        user.tel = userResponse.clientBUD.clientUserInfo.tel
                        user.photo = userResponse.clientBUD.clientUserInfo.photo
                        db = getDatabase(context)
                        when (userResponse.sysType) {
                            1 -> {
                                if (userResponse.errorCode > 0) {
                                    db.getUserDao().save(user = user)
                                    userListener?.onSuccess(userResponse)
//                                    context.toastShow("登入成功",500)
                                    context.toastShow(responseCode(userResponse.errorCode), 500)
                                } else userListener?.onFailure(userResponse.errorCode)

//                                    context.toastShow(responseCode(userResponse.errorCode),500)
                            }
                            2 -> {
                                db.getUserDao().save(user = user)
                                userListener?.onSuccess(userResponse)
//                                context.toastShow("註冊成功",500)
                                context.toastShow(responseCode(userResponse.errorCode), 500)
                            }
                            3 -> {
                                val getUserDataDao = db.getUserDao().getUser()
                                if (getUserDataDao.sysId.toInt() > 0) {
                                    val getClientUser: ClientUser = ClientUser()
                                    getClientUser.loginType = getUserDataDao.loginType
                                    getClientUser.clientBUD.sysId = getUserDataDao.sysId
                                    getClientUser.clientBUD.googleUnique =
                                        getUserDataDao.googleUnique
                                    getClientUser.clientBUD.fbUnique = getUserDataDao.fbUnique
                                    getClientUser.clientBUD.email = getUserDataDao.email
                                    getClientUser.clientBUD.clientUserInfo.name =
                                        getUserDataDao.name
                                    getClientUser.clientBUD.clientUserInfo.gender =
                                        getUserDataDao.gender
                                    getClientUser.clientBUD.clientUserInfo.tel = getUserDataDao.tel
                                    getClientUser.clientBUD.clientUserInfo.photo =
                                        getUserDataDao.photo
                                    userListener?.onSuccess(getClientUser)
                                    context.toastShow(responseCode(userResponse.errorCode), 500)
                                } else {
                                    userListener?.onFailure("查無資料")
                                }
                            }
                            4 -> {
                                db.getUserDao().updateUsers(users = user)
                                userListener?.onSuccess(userResponse)
                                context.toastShow(responseCode(userResponse.errorCode), 500)
                            }
                            else -> {
                                userListener?.onFailure(responseCode(userResponse.errorCode))
                            }
                        }
                        return@main
                    } else {
                        userListener?.onFailure(userResponse.errorCode)
                    }
                } else
                    userListener?.onFailure("伺服器發生錯誤!!")
            } catch (e: ApiException) {
                userListener?.onFailure(e.message.toString())
            } catch (e: NoInternetException) {
                userListener?.onFailure(e.message.toString())
            }
        }

    }

    fun onGetDbUserData(context: Context) {
        userListener?.onStarted()
        Coroutines().main {
            db = getDatabase(context)
            try {
                val getUserDataDao = db.getUserDao().getUser()
                if (getUserDataDao.sysId.toInt() != 0) {
                    val getClientUser: ClientUser = ClientUser()
                    getClientUser.loginType = getUserDataDao.loginType
                    getClientUser.clientBUD.sysId = getUserDataDao.sysId
                    getClientUser.clientBUD.googleUnique = getUserDataDao.googleUnique
                    getClientUser.clientBUD.fbUnique = getUserDataDao.fbUnique
                    getClientUser.clientBUD.email = getUserDataDao.email
                    getClientUser.clientBUD.clientUserInfo.name = getUserDataDao.name
                    getClientUser.clientBUD.clientUserInfo.gender = getUserDataDao.gender
                    getClientUser.clientBUD.clientUserInfo.tel = getUserDataDao.tel
                    getClientUser.clientBUD.clientUserInfo.photo = getUserDataDao.photo
                    _userData.value = getClientUser
                    userListener?.onGetUserDaoSuccess(getClientUser)
                } else {
                    userListener?.onFailure("查無資料")
                }

            } catch (e: Exception) {
                userListener?.onFailure("db 查無資料")
            }
        }
    }

    fun onDeleteDbUserData(context: Context) {
        Coroutines().main {
            try {
                db = getDatabase(context)
                val getUserDataDao = db.getUserDao().getUser()
                db.getUserDao().deleteUsers(getUserDataDao)
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                userListener?.onSuccessMsg("登出成功!!!")
                Looper.loop();
            } catch (e: NullPointerException) {
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                userListener?.onFailure("db is NullPointerException!!!")
                Looper.loop();
            } catch (e: Exception) {
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                userListener?.onFailure("db is Exception!!!")
                Looper.loop();
            }
        }
    }


}
