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
import com.romoreno.compraplus.data.network.config.Supermarket
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

/**
 * Declaracion de la inyeccion de dependencias necesitada por el modulo de base de datos
 *
 * @author Roberto Moreno
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val COMPRA_PLUS_DATABASE = "compra_plus_database"

    // ----------------------------- DATABASE ------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    @Singleton
    @Provides
    fun providesCompraPlusDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ApplicationDatabase::class.java, COMPRA_PLUS_DATABASE)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // En la primera ejecucion lanzamos una corrutina para llenar la BBDD
                    // con los supermercados disponibles en la aplicacion
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(db)
                    }
                }
            }).build()

    private fun populateDatabase(db: SupportSQLiteDatabase) {
        val insertSupermarketsScript = """
            INSERT INTO supermarket (id, name) VALUES (1, '${Supermarket.DIA}');
            INSERT INTO supermarket (id, name) VALUES (2, '${Supermarket.EROSKI}');
            INSERT INTO supermarket (id, name) VALUES (3, '${Supermarket.MERCADONA}');
        """.trimIndent()
        db.execSQL(insertSupermarketsScript)
    }

    // ----------------------------- DAO -----------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
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

    // ----------------------------- DATABASE REPOSITORY -------------------------------------------
    // ---------------------------------------------------------------------------------------------
    @Singleton
    @Provides
    fun provideDatabaseRepository(
        groceryListDao: GroceryListDao,
        productDao: ProductDao,
        supermarketDao: SupermarketDao,
        productLineDao: ProductLineDao,
        userDao: UserDao
    ): DatabaseRepository {
        return DatabaseRepositoryImpl(
            groceryListDao,
            productDao,
            supermarketDao,
            productLineDao,
            userDao
        )
    }
}
