package com.romoreno.compraplus.ui.main.grocery_list_detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.romoreno.compraplus.R
import com.romoreno.compraplus.databinding.ActivityGroceryListDetailBinding
import com.romoreno.compraplus.domain.model.ProductGroceryList
import com.romoreno.compraplus.ui.main.grocery_list_detail.dialog_fragment.OnProductSelectedCallback
import com.romoreno.compraplus.ui.main.grocery_list_detail.dialog_fragment.ProductAdderDialogFragment.Companion.NAME
import com.romoreno.compraplus.ui.main.grocery_list_detail.dialog_fragment.ProductAdderDialogFragment.Companion.QUANTITY
import com.romoreno.compraplus.ui.main.grocery_list_detail.dialog_fragment.ProductAdderDialogFragment.Companion.SUPERMARKET
import com.romoreno.compraplus.ui.main.grocery_list_detail.adapter.ProductGroceryListAdapter
import com.romoreno.compraplus.ui.main.grocery_list_detail.dialog_fragment.ProductAdderDialogFragment
import com.romoreno.compraplus.ui.main.grocery_list_detail.pojo.WhenProductGroceryListItemSelected
import com.romoreno.compraplus.ui.main.grocery_list_detail.view_model.GroceryListDetailViewModel
import com.romoreno.compraplus.ui.main.grocery_list_detail.view_model.ProductGroceryListState
import com.romoreno.compraplus.ui.main.product_comparator.ProductComparatorFragment
import com.romoreno.compraplus.ui.main.product_comparator.ProductComparatorFragment.Companion.DIALOG_MODE
import com.romoreno.compraplus.ui.main.product_comparator.pojo.Product
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Actividad dedicada a mostrar los productos contenidos en una lista de la compra
 * Posibilita el añadir nuevos productos (de la API del supermercado o escritos por el usuario),
 * clonar nuevos productos a partir de alguno ya existente en la lista o eliminarlos
 *
 * @author: Roberto Moreno
 */
@AndroidEntryPoint
class GroceryListDetailActivity : AppCompatActivity(), OnProductSelectedCallback {
    private val groceryListDetailViewModel: GroceryListDetailViewModel by viewModels()

    private val args: GroceryListDetailActivityArgs by navArgs()

    private lateinit var productGroceryListAdapter: ProductGroceryListAdapter

    private lateinit var binding: ActivityGroceryListDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroceryListDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setViewCompat()
        initUI()
        groceryListDetailViewModel.getGroceryListDetails(args.idGroceryList)
    }

    private fun initUI() {
        initList()
        initListeners()
        initUIState()
    }

    private fun initList() {

        productGroceryListAdapter = ProductGroceryListAdapter(
            WhenProductGroceryListItemSelected(
                { product -> updateProduct(product) },
                { product, checked -> markAsAdquired(product, checked) },
                { product -> showRemoveAlertDialog(product) })
        )
        binding.rvProductGroceryList.layoutManager = LinearLayoutManager(this)
        binding.rvProductGroceryList.adapter = productGroceryListAdapter
    }

    private fun initListeners() {

        setSupportActionBar(binding.topAppBar)

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.fabAddProduct.setOnClickListener {
            showProductComparatorDialogFragment()
        }
    }

    private fun showProductComparatorDialogFragment() {
        val productComparatorDialogFragment = ProductComparatorFragment()
        productComparatorDialogFragment.arguments = Bundle().apply {
            putBoolean(DIALOG_MODE, true)
        }
        productComparatorDialogFragment.show(supportFragmentManager, ProductComparatorFragment.DIALOG_PRODUCT_ADD)
    }

    private fun initUIState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                groceryListDetailViewModel.state.collect {
                    if (it.loading) {
                        loadingState()
                    } else {
                        successState(it)
                    }
                }
            }
        }
    }

    private fun loadingState() {
        binding.main.isClickable = false
        //binding.rvProductGroceryList.isVisible = false
    }

    private fun successState(state: ProductGroceryListState) {
        binding.topAppBar.title = state.groceryListProductsModel.name
        val list = state.groceryListProductsModel.productsMap.values.flatMap { it }
        productGroceryListAdapter.updateList(list)
        binding.rvProductGroceryList.apply {
            isVisible = true
            scrollToPosition(0)
        }
    }

    private fun showRemoveAlertDialog(productGroceryList: ProductGroceryList) {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.remove_product))
            .setMessage(getString(R.string.remove_confirmation_product_message))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                deleteProduct(productGroceryList)
            }
            .setCancelable(false)
            .setNegativeButton(getString(R.string.not_delete)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteProduct(productGroceryList: ProductGroceryList) {
        lifecycleScope.launch {
            groceryListDetailViewModel.deleteProductInGroceryList(productGroceryList)
        }
    }

    private fun updateProduct(product: ProductGroceryList) {
        val productAdderDialogFragment = ProductAdderDialogFragment()
        productAdderDialogFragment.arguments = Bundle().apply {
            putString(NAME, product.name)
            putString(SUPERMARKET, product.supermarket?.name)
            putString(QUANTITY, product.quantity.toString())
        }
        productAdderDialogFragment.show(supportFragmentManager, ProductAdderDialogFragment.DIALOG_PRODUCT_MODIFY)
    }

    private fun markAsAdquired(productGroceryList: ProductGroceryList, checked: Boolean) {
        groceryListDetailViewModel.markProductAsAdquired(productGroceryList, checked)
    }

    override fun onProductSelected(quantity: Int, product: Product) {
        groceryListDetailViewModel.saveProductLine(args.idGroceryList, quantity, product)
    }

    private fun setViewCompat() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.grocery_list_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.manualProduct -> {
                showDialogProductAdder()
                true
            }

            R.id.supermarketProduct -> {
                showProductComparatorDialogFragment()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDialogProductAdder() {
        val productAdderDialogFragment = ProductAdderDialogFragment()
        productAdderDialogFragment.show(supportFragmentManager, ProductAdderDialogFragment.DIALOG_PRODUCT_ADD_MANUALLY)
    }
}
