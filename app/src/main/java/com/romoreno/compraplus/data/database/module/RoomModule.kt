package com.romoreno.compraplus.data.database.module

import android.content.Context
import androidx.room.Room
import com.romoreno.compraplus.data.database.CompraPlusDatabase
import com.romoreno.compraplus.data.database.RepositoryDatabaseImpl
import com.romoreno.compraplus.data.database.dao.ProductDao
import com.romoreno.compraplus.data.network.DiaRepository
import com.romoreno.compraplus.data.network.config.Supermarket
import com.romoreno.compraplus.data.network.service.DiaApiService
import com.romoreno.compraplus.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val COMPRA_PLUS_DATABASE = "compra_plus_database"

    @Singleton
    @Provides
    fun providesCompraPlusDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, CompraPlusDatabase::class.java, COMPRA_PLUS_DATABASE).build()

    @Singleton
    @Provides
    fun providesGroceryListDao(db: CompraPlusDatabase) = db.getGroceryListDao()

    @Singleton
    @Provides
    fun providesProductDao(db: CompraPlusDatabase) = db.getProductDao()

    @Singleton
    @Provides
    fun providesUserDao(db: CompraPlusDatabase) = db.getUserDao()

    @Provides
    @IntoMap
    @StringKey("database")
    fun provideDatabaseRepository(productDao: ProductDao): Repository {
        return RepositoryDatabaseImpl(productDao)
    }
}