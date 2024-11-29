package com.romoreno.compraplus.di.module

import com.romoreno.compraplus.data.network.config.Google
import com.romoreno.compraplus.data.network.config.Supermarket
import com.romoreno.compraplus.data.network.interceptor.EroskiScrapperInterceptor
import com.romoreno.compraplus.data.network.repository.SupermarketRepository
import com.romoreno.compraplus.data.network.repository.PlaceRepository
import com.romoreno.compraplus.data.network.repository.implementation.DiaRepository
import com.romoreno.compraplus.data.network.repository.implementation.EroskiRepository
import com.romoreno.compraplus.data.network.repository.implementation.MercadonaRepository
import com.romoreno.compraplus.data.network.repository.implementation.PlaceRepositoryImpl
import com.romoreno.compraplus.data.network.service.DiaApiService
import com.romoreno.compraplus.data.network.service.EroskiApiService
import com.romoreno.compraplus.data.network.service.GooglePlacesApiService
import com.romoreno.compraplus.data.network.service.MercadonaApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Declaracion de la inyeccion de dependencias necesitada para peticiones de red
 *
 * @author Roberto Moreno
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // ----------------------------- RETROFIT ----------------------------------------------------
    // -------------------------------------------------------------------------------------------
    @Provides
    @Singleton
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        // Interceptor para imprimir en el log la request y la response
        // (Lo comento porque solo lo necesito cuando hago debug)
        //val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient
            .Builder()
            //.addInterceptor(interceptor)
            .build()
    }

    // ----------------------------- PLACE SERVICE ------------------------------------------
    // -------------------------------------------------------------------------------------------
    @Provides
    fun provideGooglePlacesApiService(builder: Retrofit.Builder, okHttpClient: OkHttpClient): GooglePlacesApiService {
        return builder
            .baseUrl(Google.BASE_URL)
            .client(okHttpClient)
            .build()
            .create(GooglePlacesApiService::class.java)
    }

    @Provides
    fun providePlaceRepository(googlePlacesApiService: GooglePlacesApiService): PlaceRepository {
        return PlaceRepositoryImpl(googlePlacesApiService)
    }


    // ----------------------------- SUPERMARKETS API SERVICE ------------------------------------
    // -------------------------------------------------------------------------------------------
    @Provides
    fun provideEroskiApiService(
        builder: Retrofit.Builder,
        eroskiScrapperInterceptor: EroskiScrapperInterceptor
    ): EroskiApiService {

        val okHttpWithCustomInterceptor = OkHttpClient
            .Builder()
            .addInterceptor(eroskiScrapperInterceptor)
            .build()

        return builder
            .baseUrl(Supermarket.Eroski.BASE_URL)
            .client(okHttpWithCustomInterceptor)
            .build()
            .create(EroskiApiService::class.java)
    }

    @Provides
    fun provideDiaApiService(builder: Retrofit.Builder, okHttpClient: OkHttpClient): DiaApiService {
        return builder
            .baseUrl(Supermarket.Dia.BASE_URL)
            .client(okHttpClient)
            .build()
            .create(DiaApiService::class.java)
    }

    @Provides
    fun provideMercadonaApiService(builder: Retrofit.Builder, okHttpClient: OkHttpClient): MercadonaApiService {
        return builder
            .baseUrl(Supermarket.Mercadona.BASE_URL)
            .client(okHttpClient)
            .build()
            .create(MercadonaApiService::class.java)
    }

    // ----------------------------- NETWORK REPOSITORY ------------------------------------------
    // -------------------------------------------------------------------------------------------
    @Provides
    @IntoMap
    @StringKey(Supermarket.EROSKI)
    fun provideEroskiRepository(eroskiApiService: EroskiApiService): SupermarketRepository {
        return EroskiRepository(eroskiApiService)
    }

    @Provides
    @IntoMap
    @StringKey(Supermarket.DIA)
    fun provideDiaRepository(diaApiService: DiaApiService): SupermarketRepository {
        return DiaRepository(diaApiService)
    }

    @Provides
    @IntoMap
    @StringKey(Supermarket.MERCADONA)
    fun provideMercadonaRepository(mercadonaApiService: MercadonaApiService): SupermarketRepository {
        return MercadonaRepository(mercadonaApiService)
    }

}
