package com.romoreno.compraplus.data.network.repository

import com.romoreno.compraplus.domain.model.Product

interface NetworkRepository {

    suspend fun getProducts(productKeyword: String) : List<Product>

}