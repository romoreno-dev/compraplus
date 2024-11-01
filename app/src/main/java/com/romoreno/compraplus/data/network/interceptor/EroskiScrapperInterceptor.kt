package com.romoreno.compraplus.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class EroskiScrapperInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())
        var responseBody = response.body?.string()
        val startImpressionsWord = responseBody?.indexOf("impressions") ?: 0
        val startJson = startImpressionsWord + "impressions".length + 3
        responseBody = responseBody?.substring(startJson)
        val endJson = responseBody?.indexOf("]") ?: 0
        responseBody = responseBody?.substring(0, endJson + 1)
        responseBody = responseBody?.replace("\\", "")
        responseBody = "{\"products\":" + responseBody + "}"

        return response.newBuilder()
            .body(okhttp3.ResponseBody.create(response.body?.contentType(), responseBody))
            .build()

    }
}