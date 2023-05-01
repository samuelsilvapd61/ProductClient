package com.samuelsilva.productclient.service.repository

import com.samuelsilva.productclient.service.model.ServerTokenResponseModel
import com.samuelsilva.productclient.service.model.UserModel
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(
        @Body user: UserModel
    ): Call<ServerTokenResponseModel>

}