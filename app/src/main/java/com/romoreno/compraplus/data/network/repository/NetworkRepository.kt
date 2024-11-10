package com.romoreno.compraplus.data.network.repository

import com.romoreno.compraplus.domain.model.ProductModel

interface NetworkRepository {

    suspend fun getProducts(productKeyword: String) : List<ProductModel>

}