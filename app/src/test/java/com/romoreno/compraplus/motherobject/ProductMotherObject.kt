package com.romoreno.compraplus.motherobject

import com.romoreno.compraplus.data.network.config.Supermarket
import com.romoreno.compraplus.domain.model.ProductModel
import com.romoreno.compraplus.ui.main.product_comparator.pojo.Prices
import com.romoreno.compraplus.ui.main.product_comparator.pojo.Product
import java.math.BigDecimal

object ProductMotherObject {

    val productsList = listOf(
        anyProduct(),
        anyProduct(),
        anyProduct()
    )

    fun anyProductModel(
        name: String = "Leche",
        price: BigDecimal = BigDecimal("0.7"),
        unitPrice: BigDecimal = BigDecimal("0.35"),
        measureUnit: String = "L",
        image: String = "https://example.com/leche.jpg",
        brand: String = "Hacendado",
        supermarket: Supermarket = Supermarket.Mercadona) : ProductModel {
        val prices = com.romoreno.compraplus.domain.model.Prices(price, unitPrice,measureUnit)
        return ProductModel(name, prices, image,
            brand, Supermarket.Mercadona)
    }

    fun anyProduct() : Product {
        val pricesExpected = Prices("2.00 €", "0.70 €/L")
        return Product("Leche", pricesExpected, "http://www.example.com",
            "HACENDADO", Supermarket.Mercadona)
    }

}