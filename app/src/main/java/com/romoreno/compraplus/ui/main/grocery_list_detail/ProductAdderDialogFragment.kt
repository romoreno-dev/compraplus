package com.romoreno.compraplus.ui.main.grocery_list_detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.romoreno.compraplus.R
import com.romoreno.compraplus.data.network.config.Supermarket
import com.romoreno.compraplus.databinding.DialogProductAdderBinding
import com.romoreno.compraplus.ui.login.LoginUtils
import com.romoreno.compraplus.ui.main.grocery_list.OnProductSelectedCallback
import com.romoreno.compraplus.ui.main.product_comparator.ProductComparatorFragment.Companion.DIALOG_MODE
import com.romoreno.compraplus.ui.main.product_comparator.pojo.Prices
import com.romoreno.compraplus.ui.main.product_comparator.pojo.Product
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductAdderDialogFragment : DialogFragment() {

    @Inject
    lateinit var utils: LoginUtils

    private var onProductSelectedCallback: OnProductSelectedCallback? = null

    private var _binding: DialogProductAdderBinding? = null
    private val binding get() = _binding!!

    companion object {
        val NAME = "NAME"
        val SUPERMARKET = "SUPERMARKET"
        val QUANTITY = "QUANTITY"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            binding.etProductName.editText?.setText(it.getString(NAME, ""))
            binding.etQuantity.editText?.setText(it.getString(QUANTITY, "1"))
        }
        initList()

        binding.btCreateList.setOnClickListener {

            val completedFields =
                utils.validateCompletedFields(
                    requireActivity(), binding.etProductName,
                    binding.etQuantity
                )

            if (completedFields) {

                val selectedName = binding.spinnerAutocompleteSupermarkets.text.toString()
                val selectedSupermarket = Supermarket.fromString(selectedName)

                if (selectedSupermarket != null) {
                    val product = Product(
                        name = binding.etProductName.editText?.text.toString(),
                        prices = Prices("", ""),
                        image = "",
                        brand = "",
                        supermarket = selectedSupermarket
                    )

                    onProductSelectedCallback
                        ?.onProductSelected(
                            tryParseContentToInt(binding.etQuantity.editText),
                            product
                        )
                    dismiss()
                }
            }
        }
    }

    private fun initList() {
        val items = Supermarket.valuesNames.toTypedArray()
        (binding.spinnerSupermarkets.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(
            items
        )

        arguments?.let {
            val supermarket = it.getString(SUPERMARKET, "")
            val index = items.indexOf(supermarket)
            if (index != -1) {
                binding.spinnerAutocompleteSupermarkets.setText(items[index], false)
                binding.alertDialogTitle.setText(R.string.clone_product)
                binding.btCreateList.setText(R.string.clone)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogProductAdderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnProductSelectedCallback) {
            onProductSelectedCallback = context
        }
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

}