package com.romoreno.compraplus.ui.main.product_comparator

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.romoreno.compraplus.domain.ProductMiddleware
import com.romoreno.compraplus.motherobject.ProductMotherObject.productsList
import com.romoreno.compraplus.ui.main.product_comparator.pojo.Product
import com.romoreno.compraplus.ui.main.product_comparator.view_model.ProductComparatorState
import com.romoreno.compraplus.ui.main.product_comparator.view_model.ProductComparatorViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertTrue
import java.lang.Thread.sleep

@OptIn(ExperimentalCoroutinesApi::class) // Anotación para usar el test con corutinas
class ProductComparatorViewModelTest {

    @RelaxedMockK
    private lateinit var productMiddleware: ProductMiddleware

    private lateinit var viewModel: ProductComparatorViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    // Esto deberia conseguir que no haya que usar sleep pero no lo esta consiguiendo
    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = ProductComparatorViewModel(productMiddleware)
    }

    @Test
    fun `when ProductComparatorViewModel is created it should emit Success with emptyList`() =
        runTest {
            // When
            val state = viewModel.state.value
            // Then
            assertTrue(state is ProductComparatorState.Success)
        }

    @Test
    fun `searchProduct should emit Success with products when find results`() = runTest {
        // Given
        val mockProductList = productsList
        val keyword = "keyword"
        coEvery { productMiddleware.getProducts(keyword) } returns mockProductList

        // When
        viewModel.searchProduct(keyword)
        sleep(100) // fixme Por el momento no encontre otra forma de conseguir que el test coja el mockeo
        val state = viewModel.state.value

        // Then
        assertTrue(state is ProductComparatorState.Success)
        assertTrue((state as ProductComparatorState.Success).products == mockProductList)
    }

    @Test
    fun `searchProduct with swipe should emit Success with products when find results`() = runTest {
        // Given
        val mockProductList = productsList
        val keyword = "keyword"
        coEvery { productMiddleware.getProducts(keyword) } returns mockProductList

        // When
        viewModel.searchProduct(keyword, true)
        sleep(100)
        val state = viewModel.state.value

        // Then
        assertTrue(state is ProductComparatorState.Success)
        assertTrue((state as ProductComparatorState.Success).products == mockProductList)
    }

    @Test
    fun `successState should set value as Success`() = runTest {
        // When
        viewModel.toSuccessState()
        sleep(100)
        val state = viewModel.state.value

        // Then
        assertTrue(state is ProductComparatorState.Success)
        assertTrue((state as ProductComparatorState.Success).products == emptyList<Product>())

    }
}

