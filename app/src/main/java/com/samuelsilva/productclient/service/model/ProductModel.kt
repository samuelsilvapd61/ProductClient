package com.samuelsilva.productclient.service.model

import com.google.gson.annotations.SerializedName

class ProductModel {

    @SerializedName("id")
    private var id: Long? = null

    @SerializedName("name")
    private val name: String? = null

    @SerializedName("description")
    private val description: String? = null

    @SerializedName("category")
    private val category: String? = null

    @SerializedName("productBrand")
    private val productBrand: String? = null

    @SerializedName("provider")
    private val provider: String? = null

    @SerializedName("quantity")
    private val quantity: Long? = null

    @SerializedName("barCode")
    private val barCode: String? = null

    @SerializedName("fabricationDate")
    private val fabricationDate: String? = null

    @SerializedName("expirationDate")
    private val expirationDate: String? = null

    @SerializedName("inclusionDate")
    private val inclusionDate: String? = null

}