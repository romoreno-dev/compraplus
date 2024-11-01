package com.romoreno.compraplus.domain

import com.romoreno.compraplus.domain.model.Product
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class ProductMiddleware @Inject constructor(private val repositories: Map<String, @JvmSuppressWildcards Repository>) {

//    suspend fun getProducts(productKeyword: String): List<Product> {
//
//        val products: MutableList<Product> = mutableListOf()
//
////        for ((supermarket, repository) in repositories) {
////            products.addAll(repository.getProducts(productKeyword))
////        }
//
//         products.addAll(repositories["eroski"]!!.getProducts(productKeyword))
//
//
//        return products.sortedBy { it.prices.price }
//    }

    suspend fun getProducts(productKeyword: String): List<Product> {
        return coroutineScope {
            val productsDeferred = mutableListOf<Deferred<List<Product>>>()

            for ((supermarket, repository) in repositories) {
                val supermakerProducts = async {
                    runCatching { repository.getProducts(productKeyword) }
                        .getOrElse { emptyList() }
                }
                productsDeferred.add(supermakerProducts)
            }

            val products = productsDeferred.map { it.await() }.flatten()
            products.sortedBy { it.prices.price }
        }
    }

}