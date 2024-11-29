package com.romoreno.compraplus.data.network.pojo.request

import com.google.gson.annotations.SerializedName

/**
 * Request a la API de Mercadona
 *
 * @author Roberto Moreno
 */
data class MercadonaRequest(
    @SerializedName("params") val params: String
)
