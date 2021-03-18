package com.yitinglin.kotlinjetpackdemo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.firebase.auth.AuthCredential
import com.yitinglin.kotlinjetpackdemo.model.SignInUser
import com.yitinglin.kotlinjetpackdemo.repository.SignInRepository

class SignInViewModel(application: Application) : AndroidViewModel(application) {
    private var signInRepository:SignInRepository=SignInRepository()
    public lateinit var checkAuthenticateLiveData : LiveData<SignInUser>
    public lateinit var authenticationLiveData : LiveData<String>
    public lateinit var collectUserInfoLiveData : LiveData<SignInUser>

    //將Firebase上google Sig In 的使用者資料相關資料取出
    public fun signInWithGoogle(authCredential:AuthCredential){
        authenticationLiveData = signInRepository.firebaseSigInWithGoogle(authCredential)
    }

    //將Firebase上auth的相關資料實例化與取出
    public fun checkAuthenticate(){
        checkAuthenticateLiveData=signInRepository.checkAuthenticationInFirebase()
    }

    //將signInRepository中collectUserData資料帶入
    public fun collectUserInfo(){
        collectUserInfoLiveData=signInRepository.collectUserData()
    }
}