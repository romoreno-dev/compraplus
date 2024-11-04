package com.romoreno.compraplus.ui.main.supermarket_locator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.romoreno.compraplus.databinding.FragmentSupermarketLocatorBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SupermarketLocatorFragment : Fragment() {

    private var _binding: FragmentSupermarketLocatorBinding? = null
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
        _binding = FragmentSupermarketLocatorBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}