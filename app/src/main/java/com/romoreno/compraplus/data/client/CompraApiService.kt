package com.romoreno.compraplus.data.client

import com.romoreno.compraplus.data.client.response.ResultResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CompraApiService {

    @GET("search?q=\"{query}\"")
    suspend fun getResults(@Path("query") query:String) : ResultResponse

}