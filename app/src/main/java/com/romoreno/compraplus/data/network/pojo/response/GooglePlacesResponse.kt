package com.romoreno.compraplus.data.network.pojo.response

import com.google.gson.annotations.SerializedName

/**
 * Response de la API de Google Places
 *
 * @author Roberto Moreno
 */
data class GooglePlacesResponse(
    @SerializedName("results") val results: List<Result>
)

data class Result(
    @SerializedName("geometry") val geometry: Geometry,
    @SerializedName("name") val name: String,
    @SerializedName("opening_hours") val openingHours: OpeningHours
)

data class Geometry(
    @SerializedName("location") val location: Location
)

data class Location(
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lng") val longitude: Double
)

data class OpeningHours(
    @SerializedName("open_now") val openNow: Boolean
)
