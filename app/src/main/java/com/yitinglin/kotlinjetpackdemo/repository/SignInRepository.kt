package com.yitinglin.kotlinjetpackdemo.repository

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yitinglin.kotlinjetpackdemo.model.ClientBUD
import com.yitinglin.kotlinjetpackdemo.model.ClientUserInfo
import com.yitinglin.kotlinjetpackdemo.model.SignInUser
import java.lang.Exception

class SignInRepository {
    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val user:SignInUser= SignInUser()

    public fun checkAuthenticationInFirebase() :MutableLiveData<SignInUser> {
        val isAuthenticateLiveData : MutableLiveData<SignInUser> = MutableLiveData()

        //獲取當前登錄的用戶
        val currentUser: FirebaseUser? = firebaseAuth.currentUser
        if (currentUser==null){
            user.isAuth=false
            isAuthenticateLiveData.value=user
        }else{
            user.clientBUD.googleUnique =currentUser.uid
            user.isAuth=true
            //獲取用戶的個人資料
            isAuthenticateLiveData.value=user
        }
        return isAuthenticateLiveData
    }


    public fun firebaseSigInWithGoogle(authCredential: AuthCredential):MutableLiveData<String>{
        val authMutableLiveData:MutableLiveData<String> = MutableLiveData()
        firebaseAuth.signInWithCredential(authCredential).addOnSuccessListener(OnSuccessListener<AuthResult>{
            val currentUser:FirebaseUser?=firebaseAuth.currentUser
            val uid:String= currentUser?.uid.toString()
            authMutableLiveData.value=uid

        }).addOnFailureListener(OnFailureListener {
            authMutableLiveData.value=it.toString()
        })

        return authMutableLiveData
    }

    //蒐集user info
    public fun collectUserData():MutableLiveData<SignInUser>{
        val collectMutableLiveData : MutableLiveData<SignInUser> = MutableLiveData<SignInUser>()
        val currentUser:FirebaseUser?=firebaseAuth.currentUser

        if (currentUser!=null){
            val uId:String= currentUser.uid
            val name: String = currentUser.displayName.toString()
            val email:String= currentUser.email.toString()
            val getImageUrl: Uri? =currentUser.photoUrl
            val imageUrl:String=getImageUrl.toString()
            val clientUserInfo: ClientUserInfo= ClientUserInfo(name,gender = "",tel = "",imageUrl)
            val clientBUD: ClientBUD=ClientBUD(0,email,uId,"",clientUserInfo)
            val user:SignInUser= SignInUser(clientBUD)
            collectMutableLiveData.value=user
        }

        return collectMutableLiveData
    }

}