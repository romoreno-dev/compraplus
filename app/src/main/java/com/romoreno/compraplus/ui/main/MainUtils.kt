package com.romoreno.compraplus.ui.main

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.EditText
import com.romoreno.compraplus.R
import com.romoreno.compraplus.data.network.config.Supermarket

/**
 * Utilidades del modulo main
 *
 * @author: Roberto Moreno
 */
object MainUtils {

    const val INTENT_MIMETYPE = "text/plain"

    fun getSupermarketImageResource(supermarket: Supermarket?) =
        when (supermarket) {
            is Supermarket.Eroski -> R.drawable.ic_eroski
            is Supermarket.Dia -> R.drawable.ic_dia
            is Supermarket.Mercadona -> R.drawable.ic_mercadona
            null -> 0
        }

    fun tryParseContentToInt(editText: EditText?): Int {
        try {
            val number = editText?.text.toString().toInt()
            return if (number > 0) {
                number
            } else {
                1
            }
        } catch (e: NumberFormatException) {
            return 1
        }
    }

    fun Context.haveInternetConnection(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }

}
