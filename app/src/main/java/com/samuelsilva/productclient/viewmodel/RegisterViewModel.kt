package com.samuelsilva.productclient.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.samuelsilva.productclient.service.constants.Constants
import com.samuelsilva.productclient.service.listener.APIListener
import com.samuelsilva.productclient.service.model.ProductRequest
import com.samuelsilva.productclient.service.model.ValidationModel
import com.samuelsilva.productclient.service.repository.ProductRepository
import com.samuelsilva.productclient.service.repository.SecurityPreferences

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val securityPreferences = SecurityPreferences(application.applicationContext)
    private val productRepository = ProductRepository(application.applicationContext)

    private val _status = MutableLiveData<ValidationModel>()
    val status: LiveData<ValidationModel> = _status

    fun saveProduct(product: ProductRequest, isEditMode: Boolean, id: Long) {
        val listener = object : APIListener<Unit> {
            override fun onSuccess(result: Unit) {
                _status.value = ValidationModel()
            }

            override fun onFailure(message: String, expiredLogin: Boolean) {
                _status.value = ValidationModel(message, expiredLogin)
            }
        }

        if (!isEditMode) productRepository.save(product, listener)
        else productRepository.edit(id, product, listener)
    }

    fun logout() {
        securityPreferences.remove(Constants.SHARED.TOKEN_KEY)
    }

}