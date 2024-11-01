package com.romoreno.compraplus.domain

import com.romoreno.compraplus.domain.model.Product

interface Repository {

    suspend fun getProducts(productKeyword: String) : List<Product>//: List<ProductModel>

}