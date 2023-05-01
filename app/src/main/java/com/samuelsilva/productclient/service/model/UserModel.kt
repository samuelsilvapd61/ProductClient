package com.samuelsilva.productclient.service.model

import com.google.gson.annotations.SerializedName

class UserModel {

    @SerializedName("email")
    lateinit var email: String

    @SerializedName("password")
    lateinit var password: String

}