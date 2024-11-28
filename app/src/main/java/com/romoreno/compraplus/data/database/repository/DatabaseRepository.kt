package com.romoreno.compraplus.data.database.repository

import com.google.firebase.auth.FirebaseUser
import com.romoreno.compraplus.data.database.entities.ProductLineEntity
import com.romoreno.compraplus.domain.model.GroceryListModel
import com.romoreno.compraplus.domain.model.GroceryListProductsModel
import com.romoreno.compraplus.ui.main.product_comparator.pojo.Product
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {

    fun getGroceryListsFromUser(firebaseUser: FirebaseUser?): Flow<List<GroceryListModel>>

    fun getGroceryListWithProductsFlow(groceryListId: Int): Flow<GroceryListProductsModel>

    suspend fun getGroceryListWithProducts(groceryListId: Int): GroceryListProductsModel?

    suspend fun deleteGroceryList(groceryListId: Int)

    suspend fun markProductAsAdquired(groceryListId: Int, idProduct: Int, checked: Boolean)

    suspend fun deleteProduct(groceryListId: Int, idProduct: Int)

    suspend fun insertUserIfNotExist(user: FirebaseUser)
    suspend fun insertProductIfNotExist(product: Product): Int
    suspend fun insertProductLine(productLine: ProductLineEntity)
    suspend fun createGroceryList(name: String, date: Long, userFirebaseUser: FirebaseUser)
}