package com.romoreno.compraplus.data.database.dao.mapper

import com.romoreno.compraplus.data.network.config.Supermarket
import com.romoreno.compraplus.domain.model.Prices
import com.romoreno.compraplus.domain.model.Product

object ProductMapper {

    fun com.romoreno.compraplus.data.database.entities.Product.toProduct(): Product {

        val prices = Prices(
            price = price.toBigDecimal(),
            unitPrice = price.toBigDecimal()
        )

        return Product(
            name, prices,
            Supermarket.Eroski.BASE_IMAGE_URL.plus(id).plus(".jpg"),
            brand ?: "",
            Supermarket.Eroski
        )
    }

}