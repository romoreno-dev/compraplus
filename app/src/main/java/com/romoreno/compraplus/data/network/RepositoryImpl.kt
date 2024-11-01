package com.romoreno.compraplus.data.network

import android.util.Log
import com.romoreno.compraplus.data.network.config.Supermarket
import com.romoreno.compraplus.data.network.mapper.ProductMapper.toProduct
import com.romoreno.compraplus.data.network.pojo.request.MercadonaRequest
import com.romoreno.compraplus.data.network.service.DiaApiService
import com.romoreno.compraplus.data.network.service.EroskiApiService
import com.romoreno.compraplus.data.network.service.MercadonaApiService
import com.romoreno.compraplus.domain.Repository
import com.romoreno.compraplus.domain.model.Product
import javax.inject.Inject

//todo intentar no duplicar el codigo
//todo cuidado con los it...

const val DATA_LOG = "dataLog"

class EroskiRepository @Inject constructor(private val eroskiApiService: EroskiApiService): Repository {
    override suspend fun getProducts(productKeyword: String): List<Product> {
        runCatching { eroskiApiService.findProducts(productKeyword) }
            .onSuccess {
                return it.products
                    .map { it.toProduct() }
            }
            .onFailure { Log.i(DATA_LOG, "ERROR ${it.message}") }
        return emptyList()
    }
}

class DiaRepository @Inject constructor(private val diaApiService: DiaApiService) : Repository {

    override suspend fun getProducts(productKeyword: String): List<Product> {
        runCatching { diaApiService.findProducts(productKeyword) }
            .onSuccess {
                return it.products
                    .map { it.toProduct() }
            }
            .onFailure { Log.i(DATA_LOG, "ERROR ${it.message}") }
        return emptyList()
    }

}

class MercadonaRepository @Inject constructor(private val mercadonaApiService: MercadonaApiService) : Repository {

    override suspend fun getProducts(productKeyword: String): List<Product> {

        val params = Supermarket.Mercadona.BODY_BEFORE.plus(productKeyword).plus(Supermarket.Mercadona.BODY_AFTER)
        val mercadonaRequest = MercadonaRequest(params)
        runCatching { mercadonaApiService.findProducts(mercadonaRequest) }
            .onSuccess {
                return it.products
                    .map {it.toProduct()}
            }
            .onFailure { Log.i(DATA_LOG, "ERROR ${it.message}") }
        return emptyList()
    }

}