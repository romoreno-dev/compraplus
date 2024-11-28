package com.romoreno.compraplus.domain

import com.romoreno.compraplus.data.database.entities.ProductLineEntity
import com.romoreno.compraplus.data.database.repository.DatabaseRepository
import com.romoreno.compraplus.data.network.repository.NetworkRepository
import com.romoreno.compraplus.domain.model.ProductModel
import com.romoreno.compraplus.ui.main.product_comparator.pojo.Product
import com.romoreno.compraplus.ui.main.product_comparator.pojo.toProduct
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.text.Normalizer
import javax.inject.Inject

class ProductMiddleware @Inject constructor(private val repositories: Map<String, @JvmSuppressWildcards NetworkRepository>,
    private val databaseRepository: DatabaseRepository) {

    suspend fun getProducts(productKeyword: String): List<Product> {
        return coroutineScope {

            val normalizedKeyword = Normalizer.normalize(productKeyword, Normalizer.Form.NFD)
                .replace("\u0301", "")
                .replace("ñ","n")
                .replace("ç","c")
            val query =  Normalizer.normalize(normalizedKeyword, Normalizer.Form.NFC)

            val productsDeferred = mutableListOf<Deferred<List<ProductModel>>>()

            for ((_, repository) in repositories) {
                val supermakerProducts = async {
                    runCatching { repository.getProducts(query) }
                        .getOrElse { emptyList() }
                }
                productsDeferred.add(supermakerProducts)
            }

            val products = productsDeferred.map { it.await() }.flatten()
            products
                .sortedBy { it.prices.price }
                .map { it.toProduct() }
        }
    }

    suspend fun insertOrUpdateProductLine(groceryListId: Int, quantity: Int, product: Product) {
        val idProductEntity = databaseRepository.insertProductIfNotExist(product)
        databaseRepository.insertProductLineOrUpdate(ProductLineEntity(groceryListId = groceryListId,
            quantity = quantity,
            productId = idProductEntity,
            adquired = false))
    }

}