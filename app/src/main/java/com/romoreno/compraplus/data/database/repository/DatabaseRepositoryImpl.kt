package com.romoreno.compraplus.data.database.repository

import com.google.firebase.auth.FirebaseUser
import com.romoreno.compraplus.data.database.dao.GroceryListDao
import com.romoreno.compraplus.data.database.dao.ProductDao
import com.romoreno.compraplus.data.database.dao.SupermarketDao
import com.romoreno.compraplus.data.database.dao.UserDao
import com.romoreno.compraplus.data.database.mapper.ProductMapper.toUser
import com.romoreno.compraplus.domain.model.GroceryListModel
import com.romoreno.compraplus.domain.model.GroceryListProductsModel
import com.romoreno.compraplus.domain.model.ProductModel
import com.romoreno.compraplus.domain.model.toGroceryListModel
import com.romoreno.compraplus.domain.model.toGroceryListProductsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val groceryListDao: GroceryListDao,
    private val productDao: ProductDao,
    private val supermarketDao: SupermarketDao,
    private val userDao: UserDao
) :
    DatabaseRepository {

    // todo Entran y salen de aqui OBJETOS DE NEGOCIO (¡¡NO ENTIDADES!!)

    //todo Lo recupero de Firebase y lo inserto si no existe....
    suspend fun insertUserIfDoesntExist(user: FirebaseUser) {
        if (userDao.getUserByUid(user.uid) == null) {
            userDao.insert(user.toUser())
        }
    }

    override fun getGroceryListsFromUser(firebaseUser: FirebaseUser?): Flow<List<GroceryListModel>> {
        //TODO Arreglalo
        return groceryListDao.getGroceryListsFromUser(1)
            .map { list ->
                list.map { groceryList -> groceryList.toGroceryListModel() }
            }
    }

    override suspend fun getGroceryListWithProducts(groceryListId: Int): GroceryListProductsModel? {
        return groceryListDao.getGroceryListWithDetails(groceryListId)
            .firstOrNull()?.toGroceryListProductsModel()
    }

    suspend fun insertProductIfDoesntExist(productModel: ProductModel) {
    }

    suspend fun insertGroceryList(groceryList: GroceryListModel) {
    }

    override suspend fun deleteGroceryList(groceryListId: Int) {

        return groceryListDao.deleteGroceryListWithId(groceryListId)
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