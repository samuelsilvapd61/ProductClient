package com.samuelsilva.productclient.service.repository

import android.content.Context
import com.samuelsilva.productclient.R
import com.samuelsilva.productclient.service.listener.APIListener
import com.samuelsilva.productclient.service.model.ServerTokenResponseModel
import com.samuelsilva.productclient.service.model.UserModel

class UserRepository(context: Context) : BaseRepository(context) {

    fun login(user: String, password: String, listener: APIListener<ServerTokenResponseModel>) {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION), false)
            return
        }

        // Setando o token como null, para não adicionar o header no momento de login
        RetrofitClient.addHeaders(null)

        var remote = RetrofitClient.getService(UserService::class.java)

        // Executando a chamada
        val user = UserModel().apply {
            this.user = user
            this.password = password
        }
        executeCall(remote.login(user), listener)

    }

}