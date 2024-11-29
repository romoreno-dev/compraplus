package com.romoreno.compraplus.ui.main.product_comparator

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.romoreno.compraplus.R
import com.romoreno.compraplus.databinding.FragmentProductComparatorBinding
import com.romoreno.compraplus.ui.main.MainUtils
import com.romoreno.compraplus.ui.main.MainUtils.haveInternetConnection
import com.romoreno.compraplus.ui.main.grocery_list_detail.dialog_fragment.OnProductSelectedCallback
import com.romoreno.compraplus.ui.main.product_comparator.adapter.ProductComparatorAdapter
import com.romoreno.compraplus.ui.main.product_comparator.pojo.Product
import com.romoreno.compraplus.ui.main.product_comparator.pojo.WhenProductItemSelected
import com.romoreno.compraplus.ui.main.product_comparator.view_model.ProductComparatorState
import com.romoreno.compraplus.ui.main.product_comparator.view_model.ProductComparatorViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Fragmento del comparador de precios de productos
 * Este fragmento hereda de DialogFragment porque puede iniciarse de dos formas diferentes:
 * - Como un fragmento mas en el que el usuario puede comparar el precio de los productos
 * - Como un cuadro de dialogo (valor isDialogMode = true) para poder elegir el producto del
 *   supermercado para incluirlo en la lista de la compra.
 *   Gracias a la callback OnProductSelectedCallback este valor es devuelto a la GroceryListDetailActivity
 *
 * @author: Roberto Moreno
 */
@AndroidEntryPoint
class ProductComparatorFragment : DialogFragment() {
    private val productComparatorViewModel: ProductComparatorViewModel by viewModels()

    private lateinit var productComparatorAdapter: ProductComparatorAdapter

    private var isDialogMode: Boolean = false
    private var onProductSelectedCallback: OnProductSelectedCallback? = null

    private var _binding: FragmentProductComparatorBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val DIALOG_MODE = "DIALOG_MODE"
        const val PIXELS_DIALOG_MODE_REDUCTION_PERCENTAGE = 0.75
        const val DIALOG_MODE_PADDING = 24
        const val DIALOG_PRODUCT_ADD = "ADD_PRODUCT"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            isDialogMode = it.getBoolean(DIALOG_MODE, false)
        }
        initUI()
    }

    private fun initUI() {

        if (isDialogMode) {
            dialog?.window?.apply {
                setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.dialog_rounded
                    )
                )
                setLayout(
                    (resources.displayMetrics.widthPixels * PIXELS_DIALOG_MODE_REDUCTION_PERCENTAGE).toInt(),
                    (resources.displayMetrics.heightPixels * PIXELS_DIALOG_MODE_REDUCTION_PERCENTAGE).toInt()
                )
            }
            dialog?.window?.decorView
                ?.setPadding(
                    DIALOG_MODE_PADDING,
                    DIALOG_MODE_PADDING,
                    DIALOG_MODE_PADDING,
                    DIALOG_MODE_PADDING
                )

            binding.etQuantity.apply {
                visibility = View.VISIBLE
                editText?.setText("1")
            }
        }

        initList()
        initListeners()
        initUIState()
    }

    private fun initList() {

        binding.swipeRefresh.setColorSchemeResources(
            R.color.md_theme_primary,
            R.color.md_theme_secondary,
            R.color.md_theme_tertiary
        )

        binding.swipeRefresh.setOnRefreshListener {
            if (requireContext().haveInternetConnection()) {
                productComparatorViewModel.searchProduct(
                    binding.searchViewProduct.query.toString(),
                    true
                )
            } else {
                productComparatorViewModel.toSuccessState()
                Toast.makeText(requireContext(), R.string.network_exception, Toast.LENGTH_SHORT).show()
            }
        }

        if (isDialogMode) {
            productComparatorAdapter = ProductComparatorAdapter(
                WhenProductItemSelected(
                    onCardViewSelected = { addProductToGroceryList(it) },
                    onProductImageSelected = { showExtendedImage(it) },
                )
            )
        } else {
            productComparatorAdapter = ProductComparatorAdapter(
                WhenProductItemSelected(onCardViewSelected = { shareProduct(it) },
                    onProductImageSelected = { showExtendedImage(it) })
            )
        }


        binding.rvProduct.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productComparatorAdapter
        }
    }

    private fun showExtendedImage(product: Product) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_product)
        val extendedImageView = dialog.findViewById<ImageView>(R.id.extendedImageViewProduct)

        Glide.with(requireContext())
            .load(product.image)
            .into(extendedImageView)

        dialog.show()
    }

    private fun addProductToGroceryList(product: Product) {
        onProductSelectedCallback
            ?.onProductSelected(MainUtils.tryParseContentToInt(binding.etQuantity.editText), product)
        dismiss()
    }

    private fun shareProduct(product: Product) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT, getString(
                    R.string.share_message,
                    product.name,
                    product.prices.price,
                    product.prices.unitPrice,
                    product.supermarket,
                    product.image
                )
            )
            type = MainUtils.INTENT_MIMETYPE
        }

        val shareIntent =
            Intent.createChooser(intent, getString(R.string.share_title, product.name))
        startActivity(shareIntent)
    }


    private fun initListeners() {
        binding.searchViewProduct.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    if (requireContext().haveInternetConnection()) {
                        productComparatorViewModel.searchProduct(query)
                    } else {
                        Toast.makeText(requireContext(), R.string.network_exception, Toast.LENGTH_SHORT).show()
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun initUIState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                productComparatorViewModel.state.collect {
                    when (it) {
                        is ProductComparatorState.Success -> successState(it)
                        ProductComparatorState.Loading -> loadingState()
                        ProductComparatorState.Swipping -> swipeState()
                        ProductComparatorState.Error -> errorState()
                    }
                }
            }
        }
    }

    private fun swipeState() {
        binding.swipeRefresh.isRefreshing = true
        binding.swipeRefresh.isEnabled = false
    }

    private fun loadingState() {
        binding.progressBar.isVisible = true
        binding.rvProduct.isVisible = false
        binding.swipeRefresh.isEnabled = false
    }

    private fun successState(state: ProductComparatorState.Success) {
        binding.progressBar.isVisible = false
        binding.swipeRefresh.isEnabled = true
        binding.swipeRefresh.isRefreshing = false

        binding.rvProduct.isVisible = true
        productComparatorAdapter.updateList(state.products)
    }

    private fun errorState() {
        Toast.makeText(
            requireContext(),
            getString(R.string.search_error_information_message),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductComparatorBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnProductSelectedCallback) {
            onProductSelectedCallback = context
        }
    }
}
