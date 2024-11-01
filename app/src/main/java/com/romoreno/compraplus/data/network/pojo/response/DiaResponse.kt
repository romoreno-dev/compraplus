package com.romoreno.compraplus.data.network.pojo.response

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class DiaResponse (
    @SerializedName("search_items") val products: List<DiaProduct>
)

data class DiaProduct (
    @SerializedName("display_name") val displayName: String,
    @SerializedName("prices") val prices: DiaPrices,
    @SerializedName("image") val image: String,
    @SerializedName("brand") val brand: String
)

data class DiaPrices (
    @SerializedName("price") val price: BigDecimal,
    @SerializedName("price_per_unit") val pricePerUnit: BigDecimal,
    @SerializedName("measure_unit") val measureUnit: String
)