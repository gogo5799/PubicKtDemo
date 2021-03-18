package com.yitinglin.kotlinjetpackdemo.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
//import com.bumptech.glide.Glide
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
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.gender_female
import kotlinx.android.synthetic.main.fragment_profile.gender_male
import kotlinx.android.synthetic.main.fragment_profile.gender_other

class ProfileFragment : Fragment(), UserListener {


    lateinit var signInViewModel: SignInViewModel
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    //20210209
    private var clientUser: ClientUser = ClientUser()
    private var userGender: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profile_progressBar.visibility = View.VISIBLE
        profile_page.visibility = View.GONE

        getUserInfo()

        initGoogleSignInClient()

        //登出
        signOutBtn.setOnClickListener {
            if (internetConnectivity(requireContext())) signOut()
        }

        //開啟編輯
        profileEditBtn.setOnClickListener {
            enableAllEditText()
            editProfile()
            profileName.setTextColor(resources.getColor(R.color.black))
            profilePhone.setTextColor(resources.getColor(R.color.black))
        }

        //修改完畢
        profileSaveBtn.setOnClickListener {
//            val testFlag = testTextField()

            userGender = when {
                gender_male.isChecked -> {
                    "男"
                }
                gender_female.isChecked -> {
                    "女"
                }
                else -> {
                    "秘密"
                }
            }


            //比對新舊資料: 相同為false=>不發送但顯示資料未異動=取消編輯; 不同為true=>進行下一步格式比對,正確才發送
            val compareValuesFlag: Boolean =
                (this.clientUser.clientBUD.clientUserInfo.name == profileName.text.toString())
                        && (this.clientUser.clientBUD.clientUserInfo.tel == profilePhone.text.toString())
                        && (this.clientUser.clientBUD.clientUserInfo.gender == userGender)

            if (compareValuesFlag) {
                displayProfile()
                disableAllEditText()
                hideErrorText()
                profileName.setTextColor(resources.getColor(R.color.loginPageColorLayerDark))
                profilePhone.setTextColor(resources.getColor(R.color.loginPageColorLayerDark))
                context?.toastShow("資料未異動", 1000)
            } else {
                val formatCheckFlag = testNameTextField() && testPhoneTextField()
                //true=>符合格式=>修改完畢  false=>不符合格式
                if (formatCheckFlag) {
                    //呼叫api發送並組物件
                    if (internetConnectivity(requireContext())) callApi()
                } else {
                    context?.toastShow("輸入資料有誤,請重新輸入..", 1000)
                }
            }
        }


        //取消編輯
        profileCancelBtn.setOnClickListener {
            displayProfile()
            disableAllEditText()
            hideErrorText()
            restoreData()
            profileName.setTextColor(resources.getColor(R.color.loginPageColorLayerDark))
            profilePhone.setTextColor(resources.getColor(R.color.loginPageColorLayerDark))
            context?.toastShow("取消編輯", 1000)
        }

    }


    //資料還原
    private fun restoreData() {
        getUserInfo()

    }

    private fun testNameTextField(): Boolean {
        val blankProfileName = profileName.checkBlank(profileName.text.toString())
        return if (blankProfileName) {
            tvNameErrorText.setText(getString(R.string.profile_blank_name))
            tvNameErrorText.visibility = View.VISIBLE
            false
        } else {
            val maxLengthName = 20
            val profileNameLength =
                profileName.checkLength(profileName.text.toString(), maxLengthName)
            if (profileNameLength) {
                tvNameErrorText.setText(getString(R.string.profile_Name_length_error) + maxLengthName + "字元")
                tvNameErrorText.visibility = View.VISIBLE
                false
            } else {
                tvNameErrorText.text = ""
                tvNameErrorText.visibility = View.GONE
                true
            }
        }
    }


    private fun testPhoneTextField(): Boolean {
        val blankProfilePhone = profilePhone.checkBlank(profilePhone.text.toString())
        if (blankProfilePhone) {
            return true
        } else {
            val maxLengthName = 10
            val profilePhoneLength =
                profilePhone.checkLength(profilePhone.text.toString(), maxLengthName)
            return if (profilePhoneLength) {
                tvTelErrorText.setText(getString(R.string.profile_Phone_length_error) + maxLengthName + "字元")
                tvTelErrorText.visibility = View.VISIBLE
                false
            } else {
                tvTelErrorText.text = ""
                tvTelErrorText.visibility = View.GONE
                if (profilePhone.text.toString().equals("未填寫")) {
                    context?.toastShow("電話未填", 500)
                    true
                } else true

            }
        }
    }

    private fun getUserInfo() {
        getUserVmData()
    }

    private fun getUserVmData() {
        val userDataViewModel =
            ViewModelProvider(requireActivity()).get(UserDataViewModel::class.java)
        profile_progressBar.show()
        if (userDataViewModel.userData.value != null) {
            userDataViewModel.userData.value?.let { setUserProfile(it) }
        } else {
            userDataViewModel.onGetDbUserData(requireActivity())
        }
        profile_progressBar.hide()
        userDataViewModel.userListener = this
    }

    private fun callApi() {
        val sendClientUser: ClientUser = ClientUser()
        sendClientUser.sysType = 4
        sendClientUser.loginType = this.clientUser.loginType
        sendClientUser.clientBUD = this.clientUser.clientBUD
        sendClientUser.clientBUD.clientUserInfo.photo =
            this.clientUser.clientBUD.clientUserInfo.photo
        sendClientUser.clientBUD.clientUserInfo.name = profileName.text.toString()
        sendClientUser.clientBUD.clientUserInfo.tel = profilePhone.text.toString()
        sendClientUser.clientBUD.clientUserInfo.gender = userGender

        val getUserInfo = ViewModelProvider(requireActivity()).get(UserDataViewModel::class.java)
        getUserInfo.onUserApi(requireContext(), sendClientUser)
        getUserInfo.userListener = this
    }

    private fun setUserProfile(clientUser: ClientUser) {
        if (clientUser.clientBUD.sysId.toInt() != 0 && clientUser.loginType > 0) {
            if (clientUser.clientBUD.email.isNotEmpty()) {
                profile_progressBar.visibility = View.GONE
                profile_page.visibility = View.VISIBLE
                if (clientUser.clientBUD.clientUserInfo.photo.isNotEmpty()) {
                    Glide.with(this).load(clientUser.clientBUD.clientUserInfo.photo).centerCrop()
                        .placeholder(R.drawable.pre_profile_image).into(profileImage)
                }
                profileName.setText(clientUser.clientBUD.clientUserInfo.name)
                profileName.setTextColor(resources.getColor(R.color.loginPageColorLayerDark))
                profileEmail.text = clientUser.clientBUD.email
                profilePhone.setText(clientUser.clientBUD.clientUserInfo.tel)
                profilePhone.setTextColor(resources.getColor(R.color.loginPageColorLayerDark))
                when (clientUser.clientBUD.clientUserInfo.gender) {
                    "男" -> {
                        gender_male.isChecked = true
                    }
                    "女" -> {
                        gender_female.isChecked = true
                    }
                    else -> {
                        gender_other.isChecked = true
                    }
                }
                this.clientUser = clientUser

                //成功
                displayProfile()
                disableAllEditText()
                hideErrorText()
            } else {
                context?.toastShow("setUserProfile clientUser.clientBUD.email.is Empty...", 500)
                profile_progressBar.visibility = View.VISIBLE
                profile_page.visibility = View.GONE
                linearLayout_profileEdit.visibility = View.GONE
                linearLayout_profileSave.visibility = View.GONE
            }
        } else {
            context?.toastShow("44444", 500)
            if (clientUser.clientBUD.sysId.toInt() == 0) {
                context?.toastShow("Success,clientUser.clientBUD.sysId.toInt()==0", 500)
            } else {
                context?.toastShow("Success,clientUser.loginType==0", 500)
            }

        }
    }


    private fun initGoogleSignInClient() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        //使用gso指定的選項構建一個GoogleSignInClient。
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
    }


    private fun signOut() {
        val intent: Intent = Intent(activity, LoadingActivity::class.java)
        intent.putExtra("root", 9)
        startActivity(intent)

        //登出後若要在按下返回鍵時不要離開程式但不返回這頁(配合登入頁SignInActivity中 onCreate的onBackPressedDispatcher)
        requireActivity().onBackPressed()
    }

    //將FirebaseUserInfo設定置頁面並將相關元件設定
    private fun setFirebaseProfile(signInUser: SignInUser) {

        if (signInUser.clientBUD.email.isNotEmpty()) {
            profile_progressBar.visibility = View.GONE
            profile_page.visibility = View.VISIBLE
            if (signInUser.clientBUD.clientUserInfo.photo.isNotEmpty()) {
                Glide.with(this).load(signInUser.clientBUD.clientUserInfo.photo).centerCrop()
                    .placeholder(R.drawable.pre_profile_image).into(profileImage)
            }
            profileName.setText(signInUser.clientBUD.clientUserInfo.name)
            profileName.setTextColor(resources.getColor(R.color.loginPageColorLayerDark))
            profileEmail.text = signInUser.clientBUD.email
            profilePhone.setText(getString(R.string.profile_blank))
            profilePhone.setTextColor(resources.getColor(R.color.loginPageColorLayerDark))
            displayProfile()
            disableAllEditText()
            hideErrorText()
        } else {
            profile_progressBar.visibility = View.VISIBLE
            profile_page.visibility = View.GONE
            linearLayout_profileEdit.visibility = View.GONE
            linearLayout_profileSave.visibility = View.GONE
        }

    }

    //關閉所有編輯欄位
    private fun disableAllEditText() {
        profileName.isEnabled = false
//        genderRg.isEnabled=false
        gender_male.isEnabled = false
        gender_female.isEnabled = false
        gender_other.isEnabled = false
        profilePhone.isEnabled = false
    }

    //開啟所有編輯欄位
    private fun enableAllEditText() {
        profileName.isEnabled = true
        gender_male.isEnabled = true
        gender_female.isEnabled = true
        gender_other.isEnabled = true
        profilePhone.isEnabled = true
    }

    //清除並隱藏錯誤題示
    private fun hideErrorText() {
        tvNameErrorText.text = ""
        tvNameErrorText.visibility = View.GONE
        tvTelErrorText.text = ""
        tvTelErrorText.visibility = View.GONE
    }

    //編輯時狀態
    private fun editProfile() {
        linearLayout_profileEdit.visibility = View.GONE
        linearLayout_profileSave.visibility = View.VISIBLE
    }

    //顯示時狀態
    private fun displayProfile() {
        linearLayout_profileEdit.visibility = View.VISIBLE
        linearLayout_profileSave.visibility = View.GONE
    }

    override fun onStarted() {
        profile_progressBar.show()
    }

    override fun onSuccess(userResponse: ClientUser) {
        profile_progressBar.hide()
        setUserProfile(userResponse)
    }

    override fun onSuccessMsg(message: String) {
        profile_progressBar.hide()
        context?.toastShow(message, 500)
    }

    override fun onFailure(message: String) {
        profile_progressBar.hide()
        context?.toastShow(message, 500)
    }

    override fun onFailure(responseCode: Int) {
        context?.toastShow(responseCode(responseCode), 500)
    }

    override fun onGetUserDaoSuccess(userDaoData: ClientUser) {
        profile_progressBar.hide()
        setUserProfile(userDaoData)
    }

}

