package com.romoreno.compraplus.data.network.repository

import com.romoreno.compraplus.chore.LogUtils
import com.romoreno.compraplus.data.network.config.Supermarket
import com.romoreno.compraplus.data.network.mapper.ProductMapper.toProduct
import com.romoreno.compraplus.data.network.pojo.request.MercadonaRequest
import com.romoreno.compraplus.data.network.pojo.response.Result
import com.romoreno.compraplus.data.network.service.DiaApiService
import com.romoreno.compraplus.data.network.service.EroskiApiService
import com.romoreno.compraplus.data.network.service.GooglePlacesApiService
import com.romoreno.compraplus.data.network.service.MercadonaApiService
import com.romoreno.compraplus.domain.model.ProductModel
import javax.inject.Inject

class GooglePlacesRepository @Inject constructor(private val googlePlacesApiService: GooglePlacesApiService) {

    suspend fun get(location: String, radius: Int,
                                     query: String): List<Result> {

        runCatching { googlePlacesApiService.findPlaces(location = location,
            radius = radius,
            query = query) }
            .onSuccess { return it.results.filter { query.equals("Dia") || it.name.equals(query, true)} }
            .onFailure { LogUtils.networkLog(it) }
        return emptyList()

        //TODO "Eroski City"... y otros que se cuelan por algun lugar
    }
}

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