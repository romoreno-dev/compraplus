package com.romoreno.compraplus.data.database

import android.util.Log
import com.romoreno.compraplus.data.database.dao.ProductDao
import com.romoreno.compraplus.data.database.dao.mapper.ProductMapper.toProduct
import com.romoreno.compraplus.data.network.DATA_LOG
import com.romoreno.compraplus.domain.Repository
import com.romoreno.compraplus.domain.model.Product
import javax.inject.Inject

class RepositoryDatabaseImpl @Inject constructor(private val productDao: ProductDao):
    Repository {
    override suspend fun getProducts(productKeyword: String): List<Product> {
        runCatching { productDao.getProducts(productKeyword) }
            .onSuccess {
                return it.map { it.toProduct() }
            }
            .onFailure { Log.i(DATA_LOG, "ERROR ${it.message}") }
        return emptyList()
    }
}