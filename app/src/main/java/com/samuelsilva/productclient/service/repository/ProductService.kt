package com.samuelsilva.productclient.service.repository

import com.samuelsilva.productclient.service.model.ProductModel
import com.samuelsilva.productclient.service.model.ProductRequest
import retrofit2.Call
import retrofit2.http.*

interface ProductService {

    @Headers("Content-Type: application/json")
    @GET("products")
    fun getProductsByParameter(
        @Query("id") id: Long?,
        @Query("name") name: String?,
        @Query("description") description: String?,
        @Query("category") category: String?,
        @Query("productBrand") productBrand: String?,
        @Query("provider") provider: String?,
        @Query("quantity") quantity: Long?,
        @Query("barCode") barCode: String?,
        @Query("fabricationDate") fabricationDate: String?,
        @Query("expirationDate") expirationDate: String?,
        @Query("inclusionDate") inclusionDate: String?,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("sort") sort: String?
    ): Call<List<ProductModel>>

    @Headers("Content-Type: application/json")
    @POST("products")
    fun createProduct(
        @Body product: ProductRequest
    ): Call<Unit>

    @Headers("Content-Type: application/json")
    @DELETE("products")
    fun deleteProduct(
        @Query("id") id: Long
    ): Call<Unit>

}