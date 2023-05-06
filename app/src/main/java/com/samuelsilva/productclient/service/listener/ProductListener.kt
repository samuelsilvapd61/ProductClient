package com.samuelsilva.productclient.service.listener

import com.samuelsilva.productclient.service.model.ProductModel

interface ProductListener {

    /**
     * Click para edição
     */
    fun onListClick(product: ProductModel)

}