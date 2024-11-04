package com.romoreno.compraplus.ui.main.product_comparator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.romoreno.compraplus.databinding.FragmentProductComparatorBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductComparatorFragment : Fragment() {

    private var _binding: FragmentProductComparatorBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initListeners()
        initUIState()
    }

    private fun initUIState() {
        //todo Pendiente de implementar
    }

    private fun initListeners() {
        //todo Pendiente de implementar
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

}