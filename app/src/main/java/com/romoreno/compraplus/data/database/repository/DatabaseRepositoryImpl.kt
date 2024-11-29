package com.romoreno.compraplus.data.database.repository

import com.google.firebase.auth.FirebaseUser
import com.romoreno.compraplus.data.database.dao.GroceryListDao
import com.romoreno.compraplus.data.database.dao.ProductDao
import com.romoreno.compraplus.data.database.dao.ProductLineDao
import com.romoreno.compraplus.data.database.dao.SupermarketDao
import com.romoreno.compraplus.data.database.dao.UserDao
import com.romoreno.compraplus.data.database.entities.GroceryListEntity
import com.romoreno.compraplus.data.database.entities.ProductLineEntity
import com.romoreno.compraplus.data.database.mapper.UserMapper.toUser
import com.romoreno.compraplus.domain.model.GroceryListModel
import com.romoreno.compraplus.domain.model.GroceryListProductsModel
import com.romoreno.compraplus.domain.model.toGroceryListModel
import com.romoreno.compraplus.domain.model.toGroceryListProductsModel
import com.romoreno.compraplus.ui.main.product_comparator.pojo.Product
import com.romoreno.compraplus.ui.main.product_comparator.pojo.toProductEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Repositorio de la base de datos
 *
 * @author Roberto Moreno
 */
class DatabaseRepositoryImpl @Inject constructor(
    private val groceryListDao: GroceryListDao,
    private val productDao: ProductDao,
    private val supermarketDao: SupermarketDao,
    private val productLineDao: ProductLineDao,
    private val userDao: UserDao
) :
    DatabaseRepository {

    override fun getGroceryListsFromUser(firebaseUser: FirebaseUser?): Flow<List<GroceryListModel>> {
        return groceryListDao.getGroceryListsFromUserId(firebaseUser?.uid!!)
            .map { list ->
                list.map { groceryList -> groceryList.toGroceryListModel() }
            }
    }

    override fun getGroceryListWithProductsFlow(groceryListId: Int): Flow<GroceryListProductsModel> {
        return groceryListDao.getGroceryListWithProductLines(groceryListId)
            .map { it.toGroceryListProductsModel() }
    }

    override suspend fun getGroceryListWithProducts(groceryListId: Int): GroceryListProductsModel? {
        return groceryListDao.getGroceryListWithProductLines(groceryListId)
            .firstOrNull()?.toGroceryListProductsModel()
    }

    override suspend fun createGroceryList(
        name: String,
        date: Long,
        userFirebaseUser: FirebaseUser
    ) {
        val groceryListEntity = GroceryListEntity(
            name = name, date = date,
            userId = userFirebaseUser.uid
        )
        groceryListDao.insert(groceryListEntity)
    }

    override suspend fun deleteGroceryList(groceryListId: Int) {
        return groceryListDao.deleteGroceryListWithId(groceryListId)
    }

    override suspend fun markProductAsAdquired(
        groceryListId: Int,
        idProduct: Int,
        checked: Boolean
    ) {
        val productLine = productLineDao.getProductLine(groceryListId, idProduct)
        productLine?.adquired = checked
        if (productLine != null) {
            productLineDao.update(productLine)
        }
    }

    override suspend fun insertProductIfNotExist(product: Product): Int {
        var productEntity = productDao.getProduct(product.name, product.supermarket.name)
        return if (productEntity == null) {
            val supermarketId = supermarketDao.getIdSupermarketFromName(product.supermarket.name)
            productDao.insert(product.toProductEntity(supermarketId)).toInt()
        } else {
            productEntity.id
        }
    }

    override suspend fun deleteProduct(groceryListId: Int, idProduct: Int) {
        return productLineDao.deleteProductLine(groceryListId, idProduct)
    }

    override suspend fun insertProductLineOrUpdate(productLine: ProductLineEntity) {
        var productLineEntity =
            productLineDao.getProductLine(productLine.groceryListId, productLine.productId)
        if (productLineEntity == null) {
            productLineDao.insert(productLine)
        } else {
            productLineDao.update(productLine)
        }
    }

    override suspend fun insertUserIfNotExist(user: FirebaseUser) {
        if (userDao.getUserByUid(user.uid) == null) {
            userDao.insert(user.toUser())
        }
    }

}
