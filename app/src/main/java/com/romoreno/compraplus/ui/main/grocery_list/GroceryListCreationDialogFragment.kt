package com.romoreno.compraplus.ui.main.grocery_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.romoreno.compraplus.R
import com.romoreno.compraplus.data.database.repository.DatabaseRepository
import com.romoreno.compraplus.databinding.DialogGroceryListBinding
import com.romoreno.compraplus.ui.login.LoginUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class GroceryListCreationDialogFragment: DialogFragment() {

    @Inject
    lateinit var databaseRepository: DatabaseRepository

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var utils: LoginUtils

    private var _binding: DialogGroceryListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = DialogGroceryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy, E",
            Locale.getDefault())

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.select_shopping_date))
            .build()

        binding.etGroceryListDate.editText?.apply {
            isFocusable = false
            isCursorVisible = false
        }

        datePicker.addOnPositiveButtonClickListener { selectedDate ->
            val dateString = dateFormat.format(Date(selectedDate))
            binding.etGroceryListDate.editText?.setText(dateString)
        }

        binding.etGroceryListDateInput.setOnClickListener {
            datePicker.show(childFragmentManager, "DATE_PICKER")
        }

        binding.btCreateList.setOnClickListener {

            val completedFields =
                utils.validateCompletedFields(requireActivity(), binding.etGroceryListName,
                    binding.etGroceryListDate)

            val date = dateFormat.parse(binding.etGroceryListDate.editText?.text?.toString()!!)
            if (completedFields) {
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {databaseRepository.createGroceryList(
                        binding.etGroceryListName.editText?.text?.toString()!!,
                        date?.time!!,
                        auth.currentUser!!)}
                    dismiss()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}