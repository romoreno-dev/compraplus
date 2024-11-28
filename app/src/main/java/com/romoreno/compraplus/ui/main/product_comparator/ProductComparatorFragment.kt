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
import com.romoreno.compraplus.ui.main.grocery_list.OnProductSelectedCallback
import com.romoreno.compraplus.ui.main.product_comparator.adapter.ProductComparatorAdapter
import com.romoreno.compraplus.ui.main.product_comparator.pojo.Product
import com.romoreno.compraplus.ui.main.product_comparator.pojo.WhenProductItemSelected
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductComparatorFragment : DialogFragment() {

    private val productComparatorViewModel: ProductComparatorViewModel by viewModels()

    private lateinit var productComparatorAdapter: ProductComparatorAdapter

    private var isDialogMode: Boolean = false
    private var onProductSelectedCallback: OnProductSelectedCallback? = null

    private var _binding: FragmentProductComparatorBinding? = null
    private val binding get() = _binding!!

    companion object {
        val DIALOG_MODE = "DIALOG_MODE"
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
                    (resources.displayMetrics.widthPixels * 0.75).toInt(),
                    (resources.displayMetrics.heightPixels * 0.75).toInt()
                )
            }
            dialog?.window?.decorView?.setPadding(24, 24, 24, 24)

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
            productComparatorViewModel.searchProduct(
                binding.searchViewProduct.query.toString(),
                true
            )
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
            ?.onProductSelected(tryParseContentToInt(binding.etQuantity.editText), product)
        dismiss()
    }

    private fun tryParseContentToInt(editText: EditText?): Int {
        try {
            val number = editText?.text.toString().toInt()
            return if (number > 0) {
                number
            } else {
                1
            }
        } catch (e: NumberFormatException) {
            return 1
        }
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
            type = "text/plain"
        }

        val shareIntent =
            Intent.createChooser(intent, getString(R.string.share_title, product.name))
        startActivity(shareIntent)
    }


    private fun initListeners() {
        binding.searchViewProduct.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    productComparatorViewModel.searchProduct(query)
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