package com.romoreno.compraplus.data.client.response

import com.google.gson.annotations.SerializedName

data class ResultResponse(
    @SerializedName("response") val response: String
)
