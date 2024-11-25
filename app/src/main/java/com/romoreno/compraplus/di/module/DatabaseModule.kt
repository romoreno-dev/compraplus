package com.romoreno.compraplus.di.module

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.romoreno.compraplus.data.database.ApplicationDatabase
import com.romoreno.compraplus.data.database.dao.GroceryListDao
import com.romoreno.compraplus.data.database.dao.ProductDao
import com.romoreno.compraplus.data.database.dao.ProductLineDao
import com.romoreno.compraplus.data.database.dao.SupermarketDao
import com.romoreno.compraplus.data.database.dao.UserDao
import com.romoreno.compraplus.data.database.repository.DatabaseRepository
import com.romoreno.compraplus.data.database.repository.DatabaseRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val COMPRA_PLUS_DATABASE = "compra_plus_database"

    // ----------------------------- DATABASE ----------------------------------------------------
    // -------------------------------------------------------------------------------------------
    @Singleton
    @Provides
    fun providesCompraPlusDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ApplicationDatabase::class.java, COMPRA_PLUS_DATABASE)
            .addCallback(object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                populateDatabase(db)
            }
        }
    }).build()

    private suspend fun populateDatabase(db: SupportSQLiteDatabase) {
        // Aquí puedes usar SQL para insertar datos predefinidos
        val insertUser = "INSERT INTO user (id, name) VALUES ('FVZfEOLsjVQu6THd3zBADkZUBQo1', 'luis.esposible@gmail.com')"
        val insertGroceryList1 = "INSERT INTO grocery_list (id, user_id, name, date) VALUES (1, 'FVZfEOLsjVQu6THd3zBADkZUBQo1', 'Compras cena Halloween', 1730293790000)"
        val insertGroceryList2 = "INSERT INTO grocery_list (id, user_id, name, date) VALUES (2, 'FVZfEOLsjVQu6THd3zBADkZUBQo1', 'Compras almuerzo sábado', 1731071390000)"
        val insertGroceryList3 = "INSERT INTO grocery_list (id, user_id, name, date) VALUES (3, 'FVZfEOLsjVQu6THd3zBADkZUBQo1', 'Compras almuerzo domingo', 1731071390000)"
        val insertGroceryList4 = "INSERT INTO grocery_list (id, user_id, name, date) VALUES (4, 'FVZfEOLsjVQu6THd3zBADkZUBQo1', 'Compras almuerzo lunes', 1731071390000)"
        val insertGroceryList5 = "INSERT INTO grocery_list (id, user_id, name, date) VALUES (5, 'FVZfEOLsjVQu6THd3zBADkZUBQo1', 'Compras almuerzo martes', 1731071390000)"
        val insertGroceryList6 = "INSERT INTO grocery_list (id, user_id, name, date) VALUES (6, 'FVZfEOLsjVQu6THd3zBADkZUBQo1', 'Compras almuerzo miércoles', 1731071390000)"
        val insertGroceryList7 = "INSERT INTO grocery_list (id, user_id, name, date) VALUES (7, 'FVZfEOLsjVQu6THd3zBADkZUBQo1', 'Compras almuerzo jueves', 1731071390000)"
        val insertGroceryList8 = "INSERT INTO grocery_list (id, user_id, name, date) VALUES (8, 'FVZfEOLsjVQu6THd3zBADkZUBQo1', 'Compras almuerzo viernes', 1731071390000)"
        val insertGroceryList9 = "INSERT INTO grocery_list (id, user_id, name, date) VALUES (9, 'FVZfEOLsjVQu6THd3zBADkZUBQo1', 'Compras cumpleaños', 1731071390000)"
        val insertSupermarket = "INSERT INTO supermarket (id, name) VALUES (1, 'dia')"
        val insertSupermarket2 = "INSERT INTO supermarket (id, name) VALUES (2, 'eroski')"
        val insertSupermarket3 = "INSERT INTO supermarket (id, name) VALUES (3, 'mercadona')"
        val insertProduct1 = "INSERT INTO product (id, name, supermarket_id, image, brand) VALUES (1, 'Producto 1', 3, 'url_imagen', 'Marca A')"
        val insertProduct2 = "INSERT INTO product  (id, name, supermarket_id, image, brand) VALUES (2, 'Producto 2', 1, 'url_imagen', 'Marca A')"
        val insertProduct3 = "INSERT INTO product  (id, name, supermarket_id, image, brand) VALUES (3, 'Producto 3', 2, 'url_imagen', 'Marca A')"
        val insertProduct4 = "INSERT INTO product  (id, name, supermarket_id, image, brand) VALUES (4, 'Producto 4', 3, 'url_imagen', 'Marca A')"
        val insertProductLine1 = "INSERT INTO product_line (grocery_list_id, product_id, quantity, adquired) VALUES (1, 1, 2, 0)"
        val insertProductLine2 = "INSERT INTO product_line (grocery_list_id, product_id, quantity, adquired) VALUES (1, 2, 1, 0)"
        val insertProductLine3 = "INSERT INTO product_line (grocery_list_id, product_id, quantity, adquired) VALUES (2, 1, 2, 0)"
        val insertProductLine4 = "INSERT INTO product_line (grocery_list_id, product_id, quantity, adquired) VALUES (1, 3, 4, 0)"
        val insertProductLine5 = "INSERT INTO product_line (grocery_list_id, product_id, quantity, adquired) VALUES (1, 4, 4, 0)"

        // Ejecutar las inserciones
        db.execSQL(insertUser)
        db.execSQL(insertGroceryList1)
        db.execSQL(insertGroceryList2)
        db.execSQL(insertGroceryList3)
        db.execSQL(insertGroceryList4)
        db.execSQL(insertGroceryList5)
        db.execSQL(insertGroceryList6)
        db.execSQL(insertGroceryList7)
        db.execSQL(insertGroceryList8)
        db.execSQL(insertGroceryList9)
        db.execSQL(insertSupermarket)
        db.execSQL(insertSupermarket2)
        db.execSQL(insertSupermarket3)
        db.execSQL(insertProduct1)
        db.execSQL(insertProduct2)
        db.execSQL(insertProduct3)
        db.execSQL(insertProduct4)
        db.execSQL(insertProductLine1)
        db.execSQL(insertProductLine2)
        db.execSQL(insertProductLine3)
        db.execSQL(insertProductLine4)
        db.execSQL(insertProductLine5)
    }


    // ----------------------------- DAO ---------------------------------------------------------
    // -------------------------------------------------------------------------------------------
    @Singleton
    @Provides
    fun providesGroceryListDao(db: ApplicationDatabase) = db.getGroceryListDao()

    @Singleton
    @Provides
    fun providesProductDao(db: ApplicationDatabase) = db.getProductDao()

    @Singleton
    @Provides
    fun providesProductLineDao(db: ApplicationDatabase) = db.getProductLineDao()

    @Singleton
    @Provides
    fun providesUserDao(db: ApplicationDatabase) = db.getUserDao()

    @Singleton
    @Provides
    fun providesSupermarketDao(db: ApplicationDatabase) = db.getSupermarketDao()

    // ----------------------------- DAO REPOSITORY ----------------------------------------------
    // -------------------------------------------------------------------------------------------
    @Singleton
    @Provides
    fun provideDatabaseRepository(
        groceryListDao: GroceryListDao,
        productDao: ProductDao,
        supermarketDao: SupermarketDao,
        productLineDao: ProductLineDao,
        userDao: UserDao
    ): DatabaseRepository {
        return DatabaseRepositoryImpl(groceryListDao, productDao, supermarketDao, productLineDao, userDao)
    }
}