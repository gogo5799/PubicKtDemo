package com.yitinglin.kotlinjetpackdemo.view

import android.content.Intent
import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.yitinglin.kotlinjetpackdemo.R
import com.yitinglin.kotlinjetpackdemo.model.ClientUser
import com.yitinglin.kotlinjetpackdemo.model.SignInUser
import com.yitinglin.kotlinjetpackdemo.repository.UserListener
import com.yitinglin.kotlinjetpackdemo.service.*
import com.yitinglin.kotlinjetpackdemo.viewmodel.SignInViewModel
import com.yitinglin.kotlinjetpackdemo.viewmodel.UserDataViewModel
import kotlinx.android.synthetic.main.activity_loading.*
import kotlinx.android.synthetic.main.activity_loading.view.*
import kotlinx.android.synthetic.main.fragment_profile.*

class LoadingActivity : AppCompatActivity(), UserListener {

    lateinit var signInViewModel: SignInViewModel
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var getFirebaseUser: SignInUser = SignInUser()
    private var loginType: Int = 0
    private var changeNum: Int = 0
    private val sendClientUser: ClientUser = ClientUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        getFirebaseUserInfo()

        initGoogleSignInClient()

        intent.extras?.let {
            //1:google login,2:fb login 9:logout
            loginType = it.getInt("root")
            when (loginType) {
                1, 2 -> login()
                9 -> logOut()
                else -> this.toastShow("登入或登出錯誤....", 500)
            }
        }

        //註冊發送
        btnRegister.setOnClickListener {
            var checkGoogleOrFbFlag: Boolean = false
            //讀取頁面並建立物件,api register
            sendClientUser.sysType = 2
            sendClientUser.errorCode = 0
            sendClientUser.loginType = loginType
            sendClientUser.clientBUD.clientUserInfo.gender="秘密"
            if (sendClientUser.loginType == 1 && sendClientUser.clientBUD.googleUnique.isNotBlank()) {
                checkGoogleOrFbFlag = true
            }

            if (sendClientUser.loginType == 2 && sendClientUser.clientBUD.fbUnique.isNotBlank()) {
                checkGoogleOrFbFlag = true
            }

            if (getFirebaseUser.clientBUD.clientUserInfo.photo.isNotBlank()) {
                sendClientUser.clientBUD.clientUserInfo.photo =
                    getFirebaseUser.clientBUD.clientUserInfo.photo
            }


            //比對新舊資料: 相同為false=>不發送但顯示資料未異動=取消編輯; 不同為true=>進行下一步格式比對,正確才發送
            val compareValuesFlag: Boolean = checkGoogleOrFbFlag
                    && (sendClientUser.clientBUD.email.isNotBlank())
                    && (sendClientUser.clientBUD.clientUserInfo.name.isNotBlank())

            if (compareValuesFlag) {
                this.toastShow("進行註冊中,請稍後...", 500)
                if(internetConnectivity (this)) sendApi(sendClientUser)
            } else {
                this.toastShow("註冊資料錯誤,停止發送....", 1000)
            }

        }
    }


    private fun sendApi(sendClientUser: ClientUser) {
        val getUserInfo = ViewModelProvider(this).get(UserDataViewModel::class.java)
        getUserInfo.onUserApi(this, sendClientUser)
        getUserInfo.userListener = this

    }

    private fun goRegister() {
        loading_progressBar.hide()
        loading_login_linearLayout.visibility = View.GONE
        loading_register_linearLayout.visibility = View.VISIBLE
    }


    private fun logOut() {
        this.onStarted()
        firebaseSignOut()

        val getUserInfo = ViewModelProvider(this).get(UserDataViewModel::class.java)
        getUserInfo.onDeleteDbUserData(this)
        getUserInfo.userListener = this
        goSignInActivity()
        //登出後若要在按下返回鍵時不要離開程式但不返回這頁(配合登入頁SignInActivity中 onCreate的onBackPressedDispatcher)
        this.onBackPressed()
    }

    private fun firebaseSignOut() {
        firebaseAuth.signOut()
        mGoogleSignInClient.signOut()
    }


    private fun login() {
        this.onStarted()
        loading_progressBar.hide()
        loading_linearLayout.visibility = View.VISIBLE
        loginShow()
    }

    private fun loginShow() {
        loading_login_linearLayout.visibility = View.VISIBLE
        loading_register_linearLayout.visibility = View.GONE
        //啟動loading動畫
        imgViewShow.setBackgroundResource(R.drawable.loading_animation)
        val rocketAnimation = imgViewShow.background
        if (rocketAnimation is Animatable) {
            rocketAnimation.start()
        }
    }

    private fun doLogin() {
        //讀取頁面並建立物件,api login
        if (getFirebaseUser.clientBUD.googleUnique.isNotBlank() && getFirebaseUser.clientBUD.email.isNotBlank() && getFirebaseUser.clientBUD.clientUserInfo.name.isNotBlank()) {
            sendClientUser.errorCode = 0
            sendClientUser.sysType = 1
            sendClientUser.clientBUD.email = getFirebaseUser.clientBUD.email
            sendClientUser.clientBUD.clientUserInfo.name=getFirebaseUser.clientBUD.clientUserInfo.name
            if (loginType != 0) {
                when (loginType) {
                    1 -> {
                        sendClientUser.loginType = loginType
                        sendClientUser.clientBUD.googleUnique = getFirebaseUser.clientBUD.googleUnique
                        sendApi(sendClientUser)
                    }
                    2 -> {
                        sendClientUser.loginType = loginType
                        sendClientUser.clientBUD.fbUnique = getFirebaseUser.clientBUD.fbUnique
                        sendApi(sendClientUser)
                    }
                    else -> this.toastShow("loginType is ${loginType}....", 500)
                }
            } else {
                this.toastShow("loginType is 0....", 500)
            }
        } else {
            if (getFirebaseUser.clientBUD.googleUnique.isBlank()) {
                this.toastShow("googleUnique is empty....", 500)
            } else {
                this.toastShow("email is empty....", 500)
            }

        }
    }


    private fun goSignInActivity() {
        val goSignInIntent: Intent = Intent(this, SignInActivity::class.java)
        startActivity(goSignInIntent)
    }

    private fun goMainActivity() {
        val goMainAInIntent: Intent = Intent(this, MainActivity::class.java)
        startActivity(goMainAInIntent)
    }

    private fun initGoogleSignInClient() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        //使用gso指定的選項構建一個GoogleSignInClient。
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    //從signInViewModel進行關連並取出getFirebaseUserInfo
    private fun getFirebaseUserInfo() {
        signInViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.application)
        ).get(SignInViewModel::class.java)

        signInViewModel.collectUserInfo()
        signInViewModel.collectUserInfoLiveData.observe(this, Observer<SignInUser>() {
            if (it.clientBUD.email.isNotEmpty()) {
                getFirebaseUser = it
                doLogin()
            } else {
                this.toastShow("Firebase userInfo is Empty", 500)

            }
        })
    }

    override fun onStarted() {
        loading_progressBar.show()
        loading_linearLayout.visibility = View.GONE
    }

    override fun onSuccess(userResponse: ClientUser) {
        loading_progressBar.hide()
        loading_linearLayout.visibility = View.VISIBLE

        //關閉loading動畫
        val rocketAnimation = imgViewShow.background
        if (rocketAnimation is Animatable) {
            rocketAnimation.stop()
        }

        if (userResponse.clientBUD.sysId > 0) {
            goMainActivity()
        }
    }

    override fun onSuccessMsg(message: String) {
        loading_progressBar.hide()
        loading_linearLayout.visibility = View.VISIBLE
        this.toastShow(message, 500)
    }

    override fun onFailure(message: String) {
        loading_progressBar.hide()
        loading_linearLayout.visibility = View.VISIBLE
        //關閉loading動畫
        val rocketAnimation = imgViewShow.background
        if (rocketAnimation is Animatable) {
            rocketAnimation.stop()
        }
        this.toastShow(message, 500)
    }

    override fun onFailure(responseCode: Int) {
        //關閉loading動畫
        val rocketAnimation = imgViewShow.background
        if (rocketAnimation is Animatable) {
            rocketAnimation.stop()
        }
        this.toastShow(responseCode(responseCode), 500)
        if (responseCode == -1 || responseCode == -3) {
//            this.toastShow("---------",500)
            goRegister()
        } else {
            goSignInActivity()
        }
    }

    override fun onGetUserDaoSuccess(userDaoData: ClientUser) {
        loading_progressBar.hide()
        loading_linearLayout.visibility = View.VISIBLE
        this.toastShow("onGetUserDaoSuccess", 500)
    }
}