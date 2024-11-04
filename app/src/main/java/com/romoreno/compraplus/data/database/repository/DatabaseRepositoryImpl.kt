package com.romoreno.compraplus.data.database.repository

import com.google.firebase.auth.FirebaseUser
import com.romoreno.compraplus.data.database.dao.GroceryListDao
import com.romoreno.compraplus.data.database.dao.ProductDao
import com.romoreno.compraplus.data.database.dao.SupermarketDao
import com.romoreno.compraplus.data.database.dao.UserDao
import com.romoreno.compraplus.data.database.mapper.ProductMapper.toUser
import com.romoreno.compraplus.domain.model.GroceryList
import com.romoreno.compraplus.domain.model.Product
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val groceryListDao: GroceryListDao,
    private val productDao: ProductDao,
    private val supermarketDao: SupermarketDao,
    private val userDao: UserDao
) :
    DatabaseRepository {

    // todo Entran y salen de aqui OBJETOS DE NEGOCIO (¡¡NO ENTIDADES!!)

    suspend fun insertUserIfDoesntExist(user: FirebaseUser) {
        if (userDao.getUserByUid(user.uid) == null) {
            userDao.insert(user.toUser())
        }
    }

    suspend fun getGroceryListsFromUser(user: FirebaseUser) {
//        return groceryListDao.getGroceryListsFromUser(user.uid)
//            .map {it.toGroceryList()}
    }

    suspend fun insertProductIfDoesntExist(product: Product) {
    }

    suspend fun insertGroceryList(groceryList: GroceryList) {
    }

    suspend fun deleteGroceryList(groceryList: GroceryList) {
    }

    suspend fun addProductToGroceryList() {
    }

    suspend fun insertSupermarket() {
    }

    suspend fun deleteSupermarket() {
    }

    suspend fun changeStateProductLine() {
    }

}