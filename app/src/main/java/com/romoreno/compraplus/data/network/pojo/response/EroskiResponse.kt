package com.romoreno.compraplus.data.network.pojo.response

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class EroskiResponse(
    @SerializedName("products") val products: List<EroskiProduct>
)

data class EroskiProduct (
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: String,
    @SerializedName("price") val price: BigDecimal,
    @SerializedName("brand") val brand: String
)