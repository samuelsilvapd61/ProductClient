package com.samuelsilva.productclient.service.listener

interface APIListener<T> {
    fun onSuccess(result: T)
    fun onFailure(message: String, expiredLogin: Boolean)
}