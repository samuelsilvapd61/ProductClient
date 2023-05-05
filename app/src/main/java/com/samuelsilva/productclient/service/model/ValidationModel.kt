package com.samuelsilva.productclient.service.model

class ValidationModel(message: String = "", expiredToken: Boolean = false) {

    private var validationMessage: String = ""
    private var status: Boolean = true
    private var expiredLogin: Boolean = true


    init {
        if (message != "") {
            validationMessage = message
            status = false
            expiredLogin = expiredToken
        }
    }

    fun status() = status
    fun message() = validationMessage
    fun expiredLogin() = expiredLogin

}