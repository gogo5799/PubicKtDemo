package com.yitinglin.kotlinjetpackdemo.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.yitinglin.kotlinjetpackdemo.R
import com.yitinglin.kotlinjetpackdemo.service.internetConnectivity
import com.yitinglin.kotlinjetpackdemo.service.network.CheckNetworkConnection
import com.yitinglin.kotlinjetpackdemo.service.toastShow
import com.yitinglin.kotlinjetpackdemo.viewmodel.SignInViewModel
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.fragment_weather.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class SignInActivity : AppCompatActivity() {

    private lateinit var signInViewModel:SignInViewModel
    private lateinit var mGoogleSignInClient:GoogleSignInClient

    companion object {
        const val RC_SIGN_IN=1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)



        intiSignInViewModel()
        googleSignInMethod()

        //建立按下返回鍵時不要離開程式要觸發的事件 (主要是配合 的onBackPressed)
        this.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                toastShow("已無上一頁了", 1000)
            }
        })

         googleSignInBtn.setOnClickListener {
             if(internetConnectivity (this)) googleSignIn()
         }

        fbLogin.setOnClickListener {
            this.toastShow("fbLogin",500)
            if(internetConnectivity (this)) this.toastShow("有網路...",500)
        }

//        fbSignInBtn.setOnClickListener {
//            fbSignIn()
//            googleSignIn()
//            Log.i("aaaaa", "aaaaaa ")
//            val EMAIL = "email"
//            fbSignInBtn.setReadPermissions(listOf(EMAIL))
//            Log.i("bbbb", "bbbbb ")
//            var callbackManager : CallbackManager = CallbackManager.Factory.create()
//            fbSignInBtn.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
//                override fun onSuccess(loginResult: LoginResult?) {
//                    Log.i("1", "FnSuccess ")
//                    Log.d("MainActivity", "Facebook token: " + loginResult!!.accessToken.token)
//                    startActivity(
//                        Intent(
//                            applicationContext,
//                            this@SignInActivity::class.java
//                        )
//                    )// App code
//                }
//                override fun onCancel() { // App code
//                    Log.i("2", "onCancel ")
//                }
//                override fun onError(exception: FacebookException) { // App code
//                    Log.i("3", "onError ")
//                }
//            })
//            callbackManager = CallbackManager.Factory.create()
//            LoginManager.getInstance().registerCallback(callbackManager,
//                object : FacebookCallback<LoginResult?> {
//                    override fun onSuccess(loginResult: LoginResult?) { // App code
//                        toastShow("成功=>"+loginResult.toString(),1000)
//                        Log.i("4", "成功 ")
//                        val accessToken = AccessToken.getCurrentAccessToken()
//                        if (accessToken!=null){
//                            Log.i("5", "成功 accessToken!=null ")
//                            toastShow("accessToken=>$accessToken",1000)
//                        }else  {
//                            Log.i("6", "成功 accessToken==null ")
//                            toastShow("accessToken is null=>$accessToken",1000)
//                        }
//
////                accessToken != null && !accessToken.isExpired
//                    }
//
//                    override fun onCancel() { // App code
//                        toastShow("已取消",1000)
//                        Log.i("7", "onCancel ")
//                    }
//
//                    override fun onError(exception: FacebookException) { // App code
//                        toastShow("失敗=>$exception",1000)
//                        Log.i("8", "onError ")
//                    }
//
//
//                })
//        }



    }

    private fun fbSignIn() {
//        var callbackManager: CallbackManager

    }

//    private fun fbSignInMethod() {
//        val callbackManager = CallbackManager.Factory.create()
//        fbSignInBtn.setReadPermissions("email", "public_profile")
//
//        fbSignInBtn.registerCallback(callbackManager,object :FacebookCallback<LoginResult>{
//            override fun onSuccess(loginResult: LoginResult?) {
//               toastShow("成功=>"+loginResult.toString(),1000)
//                if (loginResult != null) {
//                    handleFacebookAccessToken(loginResult.accessToken)
//                }else{
//                    toastShow("取得token失敗!!!",1000)
//                }
//            }
//
//            override fun onCancel() {
//                toastShow("已取消",1000)
//            }
//
//            override fun onError(error: FacebookException?) {
//                toastShow("失敗=>"+error.toString(),1000)
//            }
//        })
//    }

//    private fun handleFacebookAccessToken(token: any) {
//        val credential = FacebookAuthProvider.getCredential(token.token)
//        mGoogleSignInClient= GoogleSignIn.getClient(this, credential);
//        val signInIntent = mGoogleSignInClient.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//
//    }


//    fun printHashKey(pContext: Context) {
//        try {
//            val info: PackageInfo = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES)
//            for (signature in info.signatures) {
//                val md: MessageDigest = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                val hashKey: String = String(Base64.encode(md.digest(), 0))
//                Log.i("printHashKey", "printHashKey() Hash Key: $hashKey")
//                println("printHashKey() Hash Key: $hashKey")
//            }
//        } catch (e: NoSuchAlgorithmException) {
//            Log.e("NoSuchAlgorithmException", "printHashKey()", e)
//        } catch (e: Exception) {
//            Log.e("Exception", "printHashKey()", e)
//        }
//    }

    private fun googleSignInMethod() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        //使用gso指定的選項構建一個GoogleSignInClient。
        mGoogleSignInClient= GoogleSignIn.getClient(this, gso);
    }

    private fun intiSignInViewModel() {
        signInViewModel =ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.application)).get(SignInViewModel::class.java)
    }

    private fun googleSignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                if (account!=null){
                    getGoogleAuthCredential(account)
                }
            } catch (e: ApiException) {
                // Google Sign In failed
                if(e.statusCode==12501){
                    this.toastShow("取消Google登入",300)
                }else{
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //用戶成功GoogleSignInAccount ，從GoogleSignInAccount對象獲取ID令牌，將GoogleSignInAccount交換為Firebase憑據，然後使用Firebase憑據向Firebase進行身份驗證：
    private fun getGoogleAuthCredential(account: GoogleSignInAccount) {
        val googleTokenId: String? =account.idToken
        val authCredential:AuthCredential= GoogleAuthProvider.getCredential(googleTokenId, null)
        signInViewModel.signInWithGoogle(authCredential)
        signInViewModel.authenticationLiveData.observe(this, Observer {
            toastShow("google取得資料成功",1000)

            val intent: Intent = Intent(this@SignInActivity, LoadingActivity::class.java)
            intent.putExtra("root",1)
            startActivity(intent)

            finish()
        })
    }

}
