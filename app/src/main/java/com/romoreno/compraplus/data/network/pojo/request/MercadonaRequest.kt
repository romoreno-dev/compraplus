package com.romoreno.compraplus.data.network.pojo.request

import com.google.gson.annotations.SerializedName

data class MercadonaRequest (
    @SerializedName("params") val params: String
)