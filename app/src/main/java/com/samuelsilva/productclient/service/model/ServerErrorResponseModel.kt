package com.samuelsilva.productclient.service.model

import com.google.gson.annotations.SerializedName

class ServerErrorResponseModel {

    @SerializedName("title")
    lateinit var title: String

    @SerializedName("detail")
    lateinit var detail: String

    @SerializedName("code")
    lateinit var code: String

    @SerializedName("status")
    var status: Int = 0

    @SerializedName("timestamp")
    lateinit var timestamp: String

}