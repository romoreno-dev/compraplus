package com.romoreno.compraplus.data.network.mapper

import com.romoreno.compraplus.data.network.pojo.response.DiaProduct
import com.romoreno.compraplus.data.network.pojo.response.MercadonaProduct
import com.romoreno.compraplus.domain.model.Prices
import com.romoreno.compraplus.domain.model.ProductModel
import com.romoreno.compraplus.data.network.config.Supermarket
import com.romoreno.compraplus.data.network.pojo.response.EroskiProduct

object ProductMapper {

    fun EroskiProduct.toProduct(): ProductModel {
        val prices = Prices(
            price = price,
            unitPrice = price
        )

        return ProductModel(name, prices,
            Supermarket.Eroski.BASE_IMAGE_URL.plus(id).plus(".jpg"),
            brand,
            Supermarket.Eroski)
    }

    fun DiaProduct.toProduct(): ProductModel {
        val prices = Prices(
            prices.price,
            prices.pricePerUnit,
            mapMeasureUnit(prices.measureUnit)
        )

        return ProductModel(displayName, prices, Supermarket.Dia.BASE_IMAGE_URL.plus(image),
            brand, Supermarket.Dia)
    }

    fun MercadonaProduct.toProduct(): ProductModel {
        val prices = Prices(
            priceInstructions.unitPrice,
            priceInstructions.referencePrice,
            mapMeasureUnit( priceInstructions.referenceFormat)
        )

        return ProductModel(displayName, prices, thumbnail, brand, Supermarket.Mercadona)
    }

    private fun mapMeasureUnit(measureUnit: String): String {
        if (measureUnit.isNotBlank()) {
            if (measureUnit.contains("LITRO", true))
                return "L"
            else if (measureUnit.contains("KILO", true)) {
                return "KG"
            }
        }
        return measureUnit
    }


}