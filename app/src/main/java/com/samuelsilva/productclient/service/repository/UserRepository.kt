package com.samuelsilva.productclient.service.repository

import android.content.Context
import com.samuelsilva.productclient.R
import com.samuelsilva.productclient.service.listener.APIListener
import com.samuelsilva.productclient.service.model.ServerTokenResponseModel
import com.samuelsilva.productclient.service.model.UserModel

class UserRepository(context: Context) : BaseRepository(context) {

    private val remote = RetrofitClient.getService(UserService::class.java)

    fun login(email: String, password: String, listener: APIListener<ServerTokenResponseModel>) {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION), false)
            return
        }

        val user = UserModel().apply {
            this.email = email
            this.password = password
        }
        executeCall(remote.login(user), listener)

    }

}