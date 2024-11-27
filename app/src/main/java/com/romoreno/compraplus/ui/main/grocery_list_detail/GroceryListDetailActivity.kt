package com.romoreno.compraplus.ui.main.grocery_list_detail

import android.os.Bundle
import android.widget.Toast
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
import com.romoreno.compraplus.ui.main.grocery_list_detail.adapter.ProductGroceryListAdapter
import com.romoreno.compraplus.ui.main.grocery_list_detail.pojo.WhenProductGroceryListItemSelected
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroceryListDetailActivity : AppCompatActivity() {

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
//        initToolbar()
        initList()
        initListeners()
        initUIState()
    }

//    private fun initToolbar() {
//        setSupportActionBar(binding.topDetailsGroceryListBar)
//    }

    private fun initList() {
        //  registerForContextMenu(binding.rvProducts)
        // groceryListAdapter = GroceryListAdapter(
        //     WhenGroceryListItemSelected(
        //         { id -> toGroceryListDetails(id) },
        //          { id, name, date, view -> popupMenuOnGroceryListItem(id, name, date, view) }
        //      )
        //  )

        productGroceryListAdapter = ProductGroceryListAdapter(WhenProductGroceryListItemSelected(
            {product, checked -> markAsAdquired(product, checked)},
            {product -> showRemoveAlertDialog(product)}))
        binding.rvProductGroceryList.layoutManager = LinearLayoutManager(this)
        binding.rvProductGroceryList.adapter = productGroceryListAdapter
    }

    private fun initListeners() {

        setSupportActionBar(binding.topAppBar)

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.fabAddProduct.setOnClickListener {
            Toast.makeText(this, "Añadir producto pulsado", Toast.LENGTH_LONG).show()
        }
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

    private fun errorState() {
        Toast.makeText(
            this,
            getString(R.string.search_error_information_message),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun editProduct() {
        // TODO Implementar
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

    private fun markAsAdquired(productGroceryList: ProductGroceryList, checked: Boolean) {
        groceryListDetailViewModel.markProductAsAdquired(productGroceryList, checked)
    }

    private fun setViewCompat() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }
}