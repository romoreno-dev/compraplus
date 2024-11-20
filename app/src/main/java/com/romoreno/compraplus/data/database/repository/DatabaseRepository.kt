package com.romoreno.compraplus.data.database.repository

import com.google.firebase.auth.FirebaseUser
import com.romoreno.compraplus.domain.model.GroceryListModel
import com.romoreno.compraplus.domain.model.GroceryListProductsModel
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {

    fun getGroceryListsFromUser(firebaseUser: FirebaseUser?): Flow<List<GroceryListModel>>

    suspend fun getGroceryListWithProducts(groceryListId: Int): GroceryListProductsModel?

    suspend fun deleteGroceryList(groceryListId: Int)

    suspend fun insertUserIfDoesntExist(user: FirebaseUser);

}