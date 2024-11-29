package com.romoreno.compraplus.data.database.repository

import com.google.firebase.auth.FirebaseUser
import com.romoreno.compraplus.data.database.entities.ProductLineEntity
import com.romoreno.compraplus.domain.model.GroceryListModel
import com.romoreno.compraplus.domain.model.GroceryListProductsModel
import com.romoreno.compraplus.ui.main.product_comparator.pojo.Product
import kotlinx.coroutines.flow.Flow

/**
 * Implementacion del repositorio de la base de datos
 *
 * @author Roberto Moreno
 */
interface DatabaseRepository {

    // GroceryList
    fun getGroceryListsFromUser(firebaseUser: FirebaseUser?): Flow<List<GroceryListModel>>
    fun getGroceryListWithProductsFlow(groceryListId: Int): Flow<GroceryListProductsModel>
    suspend fun getGroceryListWithProducts(groceryListId: Int): GroceryListProductsModel?
    suspend fun createGroceryList(name: String, date: Long, userFirebaseUser: FirebaseUser)
    suspend fun deleteGroceryList(groceryListId: Int)

    // Product
    suspend fun markProductAsAdquired(groceryListId: Int, idProduct: Int, checked: Boolean)
    suspend fun insertProductIfNotExist(product: Product): Int
    suspend fun deleteProduct(groceryListId: Int, idProduct: Int)

    // ProductLine
    suspend fun insertProductLineOrUpdate(productLine: ProductLineEntity)

    // User
    suspend fun insertUserIfNotExist(user: FirebaseUser)

}
