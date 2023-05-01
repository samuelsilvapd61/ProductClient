package com.samuelsilva.productclient.service.model

import com.google.gson.annotations.SerializedName

class ServerTokenResponseModel {

    @SerializedName("token")
    lateinit var token: String

}