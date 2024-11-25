package com.romoreno.compraplus.ui.main

import com.romoreno.compraplus.R
import com.romoreno.compraplus.data.network.config.Supermarket

class Utils {

    companion object {
        fun getSupermarketImageResource(supermarket: Supermarket?) =
            when (supermarket) {
                is Supermarket.Eroski -> R.drawable.ic_eroski
                is Supermarket.Dia -> R.drawable.ic_dia
                is Supermarket.Mercadona -> R.drawable.ic_mercadona
                null -> 0
            }
    }

}