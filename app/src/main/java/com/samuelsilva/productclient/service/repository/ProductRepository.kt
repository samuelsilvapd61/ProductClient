package com.samuelsilva.productclient.service.repository

import android.content.Context
import com.samuelsilva.productclient.R
import com.samuelsilva.productclient.service.listener.APIListener
import com.samuelsilva.productclient.service.model.ProductModel

class ProductRepository(context: Context) : BaseRepository(context) {

    private val remote = RetrofitClient.getService(ProductService::class.java)

    companion object {
        var page: Int = 0
    }

    fun list(listener: APIListener<List<ProductModel>>, nextPage: Boolean) {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION), false)
            return
        }

        if (nextPage) page++ else page = 0

        executeCall(
            remote.getProductsByParameter(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                page,
                20,
                "id,asc"
            ), listener
        )
    }

}