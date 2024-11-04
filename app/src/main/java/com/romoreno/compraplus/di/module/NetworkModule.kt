package com.romoreno.compraplus.di.module

import com.romoreno.compraplus.data.network.repository.DiaRepository
import com.romoreno.compraplus.data.network.repository.EroskiRepository
import com.romoreno.compraplus.data.network.repository.MercadonaRepository
import com.romoreno.compraplus.data.network.config.Supermarket
import com.romoreno.compraplus.data.network.interceptor.EroskiScrapperInterceptor
import com.romoreno.compraplus.data.network.service.DiaApiService
import com.romoreno.compraplus.data.network.service.EroskiApiService
import com.romoreno.compraplus.data.network.service.MercadonaApiService
import com.romoreno.compraplus.data.network.repository.NetworkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

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
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .build()
    }

    // ----------------------------- API SERVICE -------------------------------------------------
    // -------------------------------------------------------------------------------------------
    @Provides
    fun provideEroskiApiService(builder: Retrofit.Builder,
                                eroskiScrapperInterceptor: EroskiScrapperInterceptor
    ): EroskiApiService {

        //todo Quitar interceptor HTTP luego
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttp = OkHttpClient
            .Builder()
//            .connectTimeout(150, TimeUnit.MILLISECONDS)  // Tiempo de espera de conexión
            .addInterceptor(interceptor)
            .addInterceptor(eroskiScrapperInterceptor)
            .build()

        return builder
            .baseUrl(Supermarket.Eroski.BASE_URL)
            .client(okHttp)
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
    fun provideEroskiRepository(eroskiApiService: EroskiApiService): NetworkRepository {
        return EroskiRepository(eroskiApiService)
    }

    @Provides
    @IntoMap
    @StringKey(Supermarket.DIA)
    fun provideDiaRepository(diaApiService: DiaApiService): NetworkRepository {
        return DiaRepository(diaApiService)
    }

    @Provides
    @IntoMap
    @StringKey(Supermarket.MERCADONA)
    fun provideMercadonaRepository(mercadonaApiService: MercadonaApiService): NetworkRepository {
        return MercadonaRepository(mercadonaApiService)
    }

}