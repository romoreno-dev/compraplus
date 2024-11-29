package com.romoreno.compraplus.data.network.repository

import com.romoreno.compraplus.domain.model.ProductModel

/**
 * Repositorio de supermercados para obtener productos
 *
 * @author Roberto Moreno
 */
fun interface SupermarketRepository {

    suspend fun getProducts(productKeyword: String): List<ProductModel>

}
