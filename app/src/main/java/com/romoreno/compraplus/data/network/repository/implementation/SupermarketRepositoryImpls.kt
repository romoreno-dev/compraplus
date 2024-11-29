package com.romoreno.compraplus.data.network.repository.implementation

import com.romoreno.compraplus.data.network.config.Supermarket
import com.romoreno.compraplus.data.network.mapper.ProductMapper.toProduct
import com.romoreno.compraplus.data.network.pojo.request.MercadonaRequest
import com.romoreno.compraplus.data.network.repository.SupermarketRepository
import com.romoreno.compraplus.data.network.service.DiaApiService
import com.romoreno.compraplus.data.network.service.EroskiApiService
import com.romoreno.compraplus.data.network.service.MercadonaApiService
import com.romoreno.compraplus.domain.model.ProductModel
import javax.inject.Inject

/**
 * Implementaciones de repositorios de supermercados para realizar las peticiones HTTP de
 * consulta de productos
 *
 * @author Roberto Moreno
 */
class DiaRepository @Inject constructor(private val diaApiService: DiaApiService) :
    SupermarketRepository {

    override suspend fun getProducts(productKeyword: String): List<ProductModel> {
        runCatching { diaApiService.findProducts(productKeyword).products }
            .onSuccess { diaProduct -> return diaProduct.map { it.toProduct() } }
        return emptyList()
    }
}

class EroskiRepository @Inject constructor(private val eroskiApiService: EroskiApiService) :
    SupermarketRepository {

    override suspend fun getProducts(productKeyword: String): List<ProductModel> {
        runCatching { eroskiApiService.findProducts(productKeyword).products }
            .onSuccess { eroskiProduct -> return eroskiProduct.map { it.toProduct() } }
        return emptyList()
    }
}

class MercadonaRepository @Inject constructor(private val mercadonaApiService: MercadonaApiService) :
    SupermarketRepository {

    override suspend fun getProducts(productKeyword: String): List<ProductModel> {
        val params = Supermarket.Mercadona.BODY_BEFORE.plus(productKeyword)
            .plus(Supermarket.Mercadona.BODY_AFTER)
        val mercadonaRequest = MercadonaRequest(params)
        runCatching { mercadonaApiService.findProducts(mercadonaRequest).products }
            .onSuccess { mercadonaProduct -> return mercadonaProduct.map { it.toProduct() } }
        return emptyList()
    }
}
