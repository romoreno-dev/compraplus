package com.romoreno.compraplus.ui.main.grocery_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.romoreno.compraplus.data.network.config.Supermarket
import com.romoreno.compraplus.databinding.DialogProductAdderBinding
import com.romoreno.compraplus.ui.login.LoginUtils
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding.etQuantity.editText?.setText("1")
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
            return editText?.text.toString().toInt()
        } catch (e: NumberFormatException) {
            return 1
        }
    }

}