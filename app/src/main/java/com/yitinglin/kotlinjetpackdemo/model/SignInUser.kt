package com.yitinglin.kotlinjetpackdemo.model

import java.io.Serializable

data class SignInUser(
    val clientBUD: ClientBUD = ClientBUD(),
    var isAuth: Boolean = false
) : Serializable
