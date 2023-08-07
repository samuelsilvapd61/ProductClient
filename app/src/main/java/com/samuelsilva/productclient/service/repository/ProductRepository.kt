package com.samuelsilva.productclient.service.repository

import android.content.Context
import com.samuelsilva.productclient.R
import com.samuelsilva.productclient.service.listener.APIListener
import com.samuelsilva.productclient.service.model.ProductModel
import com.samuelsilva.productclient.service.model.ProductRequest

class ProductRepository(context: Context) : BaseRepository(context) {

    companion object {
        var page: Int = 0
    }

    fun list(listener: APIListener<List<ProductModel>>, productFilter: ProductModel, nextPage: Boolean) {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION), false)
            return
        }

        if (nextPage) page++ else page = 0

        var remote = RetrofitClient.getService(ProductService::class.java)

        executeCall(
            remote.getProductsByParameter(
                productFilter.id,
                productFilter.name,
                productFilter.description,
                productFilter.category,
                productFilter.productBrand,
                productFilter.provider,
                productFilter.quantity,
                productFilter.barCode,
                productFilter.fabricationDate,
                productFilter.expirationDate,
                productFilter.inclusionDate,
                page,
                20,
                "id,asc"
            ), listener
        )
    }

    fun save(product: ProductRequest, listener: APIListener<Unit>) {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION), false)
            return
        }
        var remote = RetrofitClient.getService(ProductService::class.java)
        executeCall(
            remote.createProduct(product), listener
        )
    }

    fun delete(id: Long, listener: APIListener<Unit>) {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION), false)
            return
        }
        var remote = RetrofitClient.getService(ProductService::class.java)
        executeCall(
            remote.deleteProduct(id), listener
        )
    }

    fun edit(id: Long, product: ProductRequest, listener: APIListener<Unit>) {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION), false)
            return
        }
        var remote = RetrofitClient.getService(ProductService::class.java)
        executeCall(
            remote.editProduct(id, product), listener
        )
    }

}