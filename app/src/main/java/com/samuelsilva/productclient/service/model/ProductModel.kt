package com.samuelsilva.productclient.service.model

import com.google.gson.annotations.SerializedName

class ProductModel {

    @SerializedName("id")
    var id: Long? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("category")
    var category: String? = null

    @SerializedName("productBrand")
    var productBrand: String? = null

    @SerializedName("provider")
    var provider: String? = null

    @SerializedName("quantity")
    var quantity: Long? = null

    @SerializedName("barCode")
    var barCode: String? = null

    @SerializedName("fabricationDate")
    var fabricationDate: String? = null

    @SerializedName("expirationDate")
    var expirationDate: String? = null

    @SerializedName("inclusionDate")
    var inclusionDate: String? = null

}