package com.romoreno.compraplus.data.network.repository.implementation

import com.romoreno.compraplus.chore.LogUtils
import com.romoreno.compraplus.data.network.config.Supermarket
import com.romoreno.compraplus.data.network.mapper.ProductMapper.toProduct
import com.romoreno.compraplus.data.network.pojo.request.MercadonaRequest
import com.romoreno.compraplus.data.network.repository.NetworkRepository
import com.romoreno.compraplus.data.network.service.DiaApiService
import com.romoreno.compraplus.data.network.service.EroskiApiService
import com.romoreno.compraplus.data.network.service.MercadonaApiService
import com.romoreno.compraplus.domain.model.ProductModel
import javax.inject.Inject

class DiaRepository @Inject constructor(private val diaApiService: DiaApiService) :
    NetworkRepository {

    override suspend fun getProducts(productKeyword: String): List<ProductModel> {
        runCatching { diaApiService.findProducts(productKeyword).products }
            .onSuccess { diaProduct -> return diaProduct.map { it.toProduct() } }
            .onFailure { LogUtils.networkLog(it) }
        return emptyList()
    }
}

class EroskiRepository @Inject constructor(private val eroskiApiService: EroskiApiService) :
    NetworkRepository {

    override suspend fun getProducts(productKeyword: String): List<ProductModel> {
        runCatching { eroskiApiService.findProducts(productKeyword).products }
            .onSuccess { eroskiProduct -> return eroskiProduct.map { it.toProduct() } }
            .onFailure { LogUtils.networkLog(it) }
        return emptyList()
    }
}

class MercadonaRepository @Inject constructor(private val mercadonaApiService: MercadonaApiService) :
    NetworkRepository {

    override suspend fun getProducts(productKeyword: String): List<ProductModel> {
        val params = Supermarket.Mercadona.BODY_BEFORE.plus(productKeyword)
            .plus(Supermarket.Mercadona.BODY_AFTER)
        val mercadonaRequest = MercadonaRequest(params)
        runCatching { mercadonaApiService.findProducts(mercadonaRequest).products }
            .onSuccess { mercadonaProduct -> return mercadonaProduct.map { it.toProduct() } }
            .onFailure { LogUtils.networkLog(it) }
        return emptyList()
    }
}