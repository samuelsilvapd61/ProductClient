package com.samuelsilva.productclient.service.model

import com.google.gson.annotations.SerializedName

class UserModel {

    @SerializedName("user")
    lateinit var user: String

    @SerializedName("password")
    lateinit var password: String

}