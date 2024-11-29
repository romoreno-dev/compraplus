package com.romoreno.compraplus.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

/**
 * Interceptor de la respuesta HTTP recibida de la web de Eroski para poder obtener el JSON
 *
 * @author Roberto Moreno
 */
class EroskiScrapperInterceptor @Inject constructor() : Interceptor {

    companion object {
        const val CHARACTERS_NUMBER_THAT_MUST_SKIP = 3
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())
        var responseBody = response.body?.string()
        val startImpressionsWord = responseBody?.indexOf("impressions") ?: 0
        val startJson =
            startImpressionsWord + "impressions".length + CHARACTERS_NUMBER_THAT_MUST_SKIP
        responseBody = responseBody?.substring(startJson)
        val endJson = responseBody?.indexOf("]") ?: 0
        responseBody = responseBody?.substring(0, endJson + 1)
        responseBody = responseBody?.replace("\\", "")
        responseBody = "{\"products\":" + responseBody + "}"

        return response.newBuilder()
            .body(responseBody.toResponseBody(response.body?.contentType()))
            .build()

    }
}
