package com.romoreno.compraplus.ui.main.grocery_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.romoreno.compraplus.R
import com.romoreno.compraplus.databinding.FragmentGroceryListBinding
import com.romoreno.compraplus.ui.main.grocery_list.adapter.GroceryListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroceryListFragment : Fragment() {

    private val groceryListViewModel: GroceryListViewModel by viewModels()

    private lateinit var groceryListAdapter : GroceryListAdapter

    private var _binding: FragmentGroceryListBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        groceryListViewModel.getGroceryLists(FirebaseAuth.getInstance().currentUser)
    }

    private fun initUI() {
        initList()
        initListeners()
        initUIState()
    }

    private fun initList() {
        groceryListAdapter = GroceryListAdapter()
        binding.rvGroceryList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = groceryListAdapter
            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0 && binding.fabNewList.isExtended) {
                        binding.fabNewList.shrink()
                    } else if (dy < 0 && !binding.fabNewList.isExtended) {
                        binding.fabNewList.extend()
                    }
                }
            })
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                groceryListViewModel.state.collect {
                    when (it) {
                        is GroceryListState.Success -> successState(it)
                        GroceryListState.Loading -> loadingState()
                        GroceryListState.Error -> errorState()
                    }
                }
            }
        }
    }

    private fun loadingState() {
        binding.rvGroceryList.isVisible = false
    }

    private fun successState(state: GroceryListState.Success) {
        binding.rvGroceryList.isVisible = true
        groceryListAdapter.updateList(state.groceryListModels)
    }

    private fun errorState() {
        Toast.makeText(
            requireContext(),
            getString(R.string.search_error_information_message),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun initListeners() {
        //todo Pendiente de implementar
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroceryListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}