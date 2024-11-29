package com.romoreno.compraplus.ui.main.grocery_list_detail.dialog_fragment

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
import com.romoreno.compraplus.ui.main.MainUtils
import com.romoreno.compraplus.ui.main.product_comparator.pojo.Prices
import com.romoreno.compraplus.ui.main.product_comparator.pojo.Product
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Cuadro de diñalogo para añadir productos de forma manual por el usuario (nombre y supermercado
 * elegidos por él)
 * Después, le pasa el valor a GroceryListDetailActivity en forma de callBack gracias a
 * OnProductSelectedCallback
 * Como funcionalidad adicional también recibe valores por parámetro para que el usuario pueda
 * clonar el producto en la lista de la compra cambiando algún pequeño valor
 *
 * @author: Roberto Moreno
 */
@AndroidEntryPoint
class ProductAdderDialogFragment : DialogFragment() {

    @Inject
    lateinit var utils: LoginUtils

    private var onProductSelectedCallback: OnProductSelectedCallback? = null

    private var _binding: DialogProductAdderBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val NAME = "NAME"
        const val SUPERMARKET = "SUPERMARKET"
        const val QUANTITY = "QUANTITY"
        const val DIALOG_PRODUCT_ADD_MANUALLY = "ADD_PRODUCT_MANUALLY"
        const val DIALOG_PRODUCT_MODIFY = "MODIFY_PRODUCT_MANUALLY"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding.etQuantity.editText?.setText("1")
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
                            MainUtils.tryParseContentToInt(binding.etQuantity.editText),
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

}
