package com.romoreno.compraplus.data.network.config

sealed class Supermarket(val name: String) {
    companion object {
        const val EROSKI = "eroski"
        const val DIA = "dia"
        const val MERCADONA = "mercadona"
        val values = listOf(Eroski, Dia, Mercadona)
        val valuesNames = listOf(EROSKI, DIA, MERCADONA)

        fun fromString(name: String?): Supermarket? {
            return when (name) {
                EROSKI -> Eroski
                DIA -> Dia
                MERCADONA -> Mercadona
                else -> null
            }
        }
    }

    data object Eroski:Supermarket(EROSKI) {
        const val BASE_URL = "https://supermercado.eroski.es/"
        const val PATH = "es/search/results/"
        const val BASE_IMAGE_URL = "https://supermercado.eroski.es/images/"
    }

    data object Dia:Supermarket(DIA) {
        const val BASE_URL = "https://www.dia.es/api/"
        const val PATH = "v1/search-back/search/reduced"
        const val BASE_IMAGE_URL = "https://www.dia.es"
    }

    data object Mercadona:Supermarket(MERCADONA) {
        const val BASE_URL = "https://7uzjkl1dj0-dsn.algolia.net/"
        const val PATH = "1/indexes/products_prod_4315_es/query"
        const val BODY_BEFORE = "query="
        const val BODY_AFTER = "&clickAnalytics=true&analyticsTags=%5B%22web%22%5D&getRankingInfo=true"
    }

}