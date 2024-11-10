package com.romoreno.compraplus.ui.main.product_comparator.pojo

import com.romoreno.compraplus.motherobject.ProductMotherObject
import io.kotlintest.shouldBe
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.math.BigDecimal

class ProductTest {

    @Test
    fun `toProduct should return a Product with correct format`() {
        //Given
        val productModel = ProductMotherObject.anyProductModel(
            price = BigDecimal(2.000),
            unitPrice = BigDecimal(0.7), measureUnit = "L")

        //When
        val product = productModel.toProduct()

        //Then
        assertEquals(productModel.name, product.name)
        assertEquals(productModel.brand, product.brand)
        assertEquals(productModel.image, product.image)
        assertEquals(productModel.supermarket, product.supermarket)
        assertEquals("2.00 €", product.prices.price)
        assertEquals("0.70 €/L", product.prices.unitPrice)
    }

    @Test
    fun `toProduct should return a Product with empty unitPrice if no measureUnit`() {
        //Given
        val productModel = ProductMotherObject.anyProductModel(
            unitPrice = BigDecimal(0.7),
            measureUnit = ""
        )

        //When
        val product = productModel.toProduct()

        //Then
        product.prices.unitPrice shouldBe ""
    }

}