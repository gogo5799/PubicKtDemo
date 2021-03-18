package com.yitinglin.kotlinjetpackdemo.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yitinglin.kotlinjetpackdemo.model.SignInUser
import com.yitinglin.kotlinjetpackdemo.viewmodel.SignInViewModel

class SplashActivity : AppCompatActivity() {

    private lateinit var signInViewModel:SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSignInViewModel()
        checkIfUserAuthenticate()
    }

    //判定是否登入
    private fun checkIfUserAuthenticate() {
        signInViewModel.checkAuthenticate()
        signInViewModel.checkAuthenticateLiveData.observe(this,Observer<SignInUser>{
            if (!it.isAuth){
                goToSignInActivity()
            }else{
                goToLoadingActivity()
            }
        })
    }

    //啟動頁面
    private fun goToLoadingActivity() {
        val intent:Intent= Intent(this,LoadingActivity::class.java)
        intent.putExtra("root",1)
        startActivity(intent)
        finish()
    }

    //登入頁面
    private fun goToSignInActivity() {
        val intent:Intent= Intent(this,SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initSignInViewModel() {
        signInViewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)).get(SignInViewModel::class.java)
    }
}