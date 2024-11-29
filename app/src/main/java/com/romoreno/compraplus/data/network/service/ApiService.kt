package com.romoreno.compraplus.data.network.service

import com.romoreno.compraplus.BuildConfig
import com.romoreno.compraplus.data.network.config.Google
import com.romoreno.compraplus.data.network.config.Supermarket
import com.romoreno.compraplus.data.network.pojo.request.MercadonaRequest
import com.romoreno.compraplus.data.network.pojo.response.DiaResponse
import com.romoreno.compraplus.data.network.pojo.response.EroskiResponse
import com.romoreno.compraplus.data.network.pojo.response.GooglePlacesResponse
import com.romoreno.compraplus.data.network.pojo.response.MercadonaResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Interfaces API Services para realizar las diversas llamadas HTTP con Retrofit
 *
 * @author Roberto Moreno
 */
interface GooglePlacesApiService {

    @GET(Google.PATH)
    suspend fun findPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("key") apiKey: String = BuildConfig.MAPS_API_KEY,
        @Query("keyword") query: String
    ): GooglePlacesResponse

}

interface DiaApiService {
    @GET(Supermarket.Dia.PATH)
    suspend fun findProducts(@Query("q") q: String, @Query("page") page: String = "1"): DiaResponse

}


interface EroskiApiService {
    @GET(Supermarket.Eroski.PATH)
    suspend fun findProducts(
        @Query("q") q: String,
        @Query("suggestionsFilter") suggestionsFilter: Boolean = false
    ): EroskiResponse

}


interface MercadonaApiService {
    @POST(Supermarket.Mercadona.PATH)
    suspend fun findProducts(
        @Body mercadonaRequest: MercadonaRequest,
        @Query("x-algolia-application-id") applicationId: String = "7UZJKL1DJ0",
        @Query("x-algolia-api-key") apiKey: String = "9d8f2e39e90df472b4f2e559a116fe17"
    ): MercadonaResponse
}
