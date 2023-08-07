package com.samuelsilva.productclient.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.samuelsilva.productclient.service.constants.Constants
import com.samuelsilva.productclient.service.helper.BiometricHelper
import com.samuelsilva.productclient.service.listener.APIListener
import com.samuelsilva.productclient.service.model.ServerTokenResponseModel
import com.samuelsilva.productclient.service.model.ValidationModel
import com.samuelsilva.productclient.service.repository.RetrofitClient
import com.samuelsilva.productclient.service.repository.SecurityPreferences
import com.samuelsilva.productclient.service.repository.UserRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _login = MutableLiveData<ValidationModel>()
    val login: LiveData<ValidationModel> = _login

    private val _loggedUser = MutableLiveData<Boolean>()
    val loggedUser: LiveData<Boolean> = _loggedUser

    /**
     * Faz login chamando o servidor
     */
    fun doLogin(ip: String, port: String, user: String, password: String) {

        // Configura ip e porta na requisição
        RetrofitClient.configureBaseUrl(ip, port)

        userRepository.login(user, password, object : APIListener<ServerTokenResponseModel> {
            override fun onSuccess(result: ServerTokenResponseModel) {
                securityPreferences.store(Constants.SHARED.TOKEN_KEY, result.token)
                RetrofitClient.addHeaders(result.token)

                //Armazenar IP e PORTA
                securityPreferences.store(Constants.SHARED.IP_KEY, ip)
                securityPreferences.store(Constants.SHARED.PORT_KEY, port)

                _login.value = ValidationModel()
            }

            override fun onFailure(message: String, expiredLogin: Boolean) {
                _login.value = ValidationModel(message, expiredLogin)
            }

        })
    }

    /**
     * Verifica se usuário está logado
     */
    fun verifyAuthentication() {
        val token = securityPreferences.get(Constants.SHARED.TOKEN_KEY)
        RetrofitClient.addHeaders(token)

        // Se token for diferente de vazio, usuário está logado
        val logged = (token != "")

        if (logged) {
            // Configura ip e porta na requisição
            RetrofitClient.configureBaseUrl(
                securityPreferences.get(Constants.SHARED.IP_KEY),
                securityPreferences.get(Constants.SHARED.PORT_KEY))
        }

        // Se usuário está logado e a autenticação biométrica está disponível
        _loggedUser.value = (logged && BiometricHelper.isBiometricAvailable(getApplication()))

    }

}
