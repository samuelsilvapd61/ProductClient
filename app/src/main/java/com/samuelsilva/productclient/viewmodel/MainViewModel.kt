package com.samuelsilva.productclient.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.samuelsilva.productclient.service.constants.Constants
import com.samuelsilva.productclient.service.listener.APIListener
import com.samuelsilva.productclient.service.model.ProductModel
import com.samuelsilva.productclient.service.model.ValidationModel
import com.samuelsilva.productclient.service.repository.ProductRepository
import com.samuelsilva.productclient.service.repository.SecurityPreferences

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val securityPreferences = SecurityPreferences(application.applicationContext)
    private val productRepository = ProductRepository(application.applicationContext)

    private val _products = MutableLiveData<List<ProductModel>>()
    val products: LiveData<List<ProductModel>> = _products

    private val _status = MutableLiveData<ValidationModel>()
    val status: LiveData<ValidationModel> = _status

    fun list(nextPage: Boolean) {
        val listener = object : APIListener<List<ProductModel>> {
            override fun onSuccess(result: List<ProductModel>) {
                _products.value = result
            }

            override fun onFailure(message: String, expiredLogin: Boolean) {
                _status.value = ValidationModel(message, expiredLogin)
            }
        }

        productRepository.list(listener, nextPage)

    }

    fun logout() {
        securityPreferences.remove(Constants.SHARED.TOKEN_KEY)
    }

}