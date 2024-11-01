package com.romoreno.compraplus.data.network.mapper

import com.romoreno.compraplus.data.network.pojo.response.DiaProduct
import com.romoreno.compraplus.data.network.pojo.response.MercadonaProduct
import com.romoreno.compraplus.domain.model.Prices
import com.romoreno.compraplus.domain.model.Product
import com.romoreno.compraplus.data.network.config.Supermarket
import com.romoreno.compraplus.data.network.pojo.response.EroskiProduct

object ProductMapper {

    fun EroskiProduct.toProduct(): Product {
        val prices = Prices(
            price = price,
            unitPrice = price
        )

        return Product(name, prices,
            Supermarket.Eroski.BASE_IMAGE_URL.plus(id).plus(".jpg"),
            brand,
            Supermarket.Eroski)
    }

    fun DiaProduct.toProduct(): Product {
        val prices = Prices(
            prices.price,
            prices.pricePerUnit,
            prices.measureUnit
        )

        return Product(displayName, prices, Supermarket.Dia.BASE_IMAGE_URL.plus(image),
            brand, Supermarket.Dia)
    }

    fun MercadonaProduct.toProduct(): Product {
        val prices = Prices(
            priceInstructions.unitPrice,
            priceInstructions.referencePrice,
            priceInstructions.referenceFormat
        )

        return Product(displayName, prices, thumbnail, brand, Supermarket.Mercadona)
    }


}