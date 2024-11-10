package com.romoreno.compraplus.domain.model

import com.romoreno.compraplus.data.network.config.Supermarket
import java.math.BigDecimal

data class ProductModel (
    val name: String,
    val prices: Prices,
    val image: String,
    val brand: String,
    val supermarket: Supermarket
)

data class Prices (
    val price: BigDecimal,
    val unitPrice: BigDecimal,
    val measureUnit: String = ""
)