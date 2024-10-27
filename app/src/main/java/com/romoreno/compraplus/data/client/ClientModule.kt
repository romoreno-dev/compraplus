package com.romoreno.compraplus.data.client

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ClientModule {

    @Provides
    @Singleton
    fun retrofitProvider() : Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("http://www.google.es")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}