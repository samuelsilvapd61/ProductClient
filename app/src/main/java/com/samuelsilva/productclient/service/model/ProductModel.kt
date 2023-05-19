package com.samuelsilva.productclient.service.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class ProductModel() : Parcelable {


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

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Long::class.java.classLoader) as? Long
        name = parcel.readString()
        description = parcel.readString()
        category = parcel.readString()
        productBrand = parcel.readString()
        provider = parcel.readString()
        quantity = parcel.readValue(Long::class.java.classLoader) as? Long
        barCode = parcel.readString()
        fabricationDate = parcel.readString()
        expirationDate = parcel.readString()
        inclusionDate = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(category)
        parcel.writeString(productBrand)
        parcel.writeString(provider)
        parcel.writeValue(quantity)
        parcel.writeString(barCode)
        parcel.writeString(fabricationDate)
        parcel.writeString(expirationDate)
        parcel.writeString(inclusionDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductModel> {

        lateinit var productFilter: ProductModel
        override fun createFromParcel(parcel: Parcel): ProductModel {
            return ProductModel(parcel)
        }

        override fun newArray(size: Int): Array<ProductModel?> {
            return arrayOfNulls(size)
        }
    }

}