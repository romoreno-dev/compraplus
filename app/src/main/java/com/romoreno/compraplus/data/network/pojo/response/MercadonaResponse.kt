package com.romoreno.compraplus.data.network.pojo.response

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class MercadonaResponse (
    @SerializedName("hits") val products: List<MercadonaProduct>
)

data class MercadonaProduct (
    @SerializedName("display_name") val displayName: String,
    @SerializedName("price_instructions") val priceInstructions: MercadonaPrices,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("brand") val brand: String
)

data class MercadonaPrices (
    @SerializedName("unit_price") val unitPrice: BigDecimal,
    @SerializedName("reference_price") val referencePrice: BigDecimal,
    @SerializedName("reference_format") val referenceFormat: String
)

