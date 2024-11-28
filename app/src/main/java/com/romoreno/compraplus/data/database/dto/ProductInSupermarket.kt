package com.romoreno.compraplus.data.database.dto

data class ProductInSupermarket(
    val productId: Int,
    val productName: String,
    val supermarketId: Int,
    val supermarketName: String
)