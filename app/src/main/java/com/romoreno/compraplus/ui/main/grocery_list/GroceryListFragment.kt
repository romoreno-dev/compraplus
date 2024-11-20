package com.romoreno.compraplus.ui.main.grocery_list

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
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
import com.romoreno.compraplus.ui.main.grocery_list.pojo.WhenGroceryListItemSelected
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroceryListFragment : Fragment() {

    private val groceryListViewModel: GroceryListViewModel by viewModels()

    private lateinit var groceryListAdapter: GroceryListAdapter

    private var _binding: FragmentGroceryListBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        groceryListViewModel.getGroceryLists(FirebaseAuth.getInstance().currentUser)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.grocery_list_menu, menu)
    }


    private fun initUI() {
        initList()
        initListeners()
        initUIState()
    }

    private fun initList() {
        registerForContextMenu(binding.rvGroceryList)
        groceryListAdapter = GroceryListAdapter(
            WhenGroceryListItemSelected(
                { id, view -> toGroceryListDetails(id, view) },
                { id, view -> popupMenuOnGroceryListItem(id, view) }
            )
        )
        binding.rvGroceryList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = groceryListAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

    private fun showRemoveAlertDialog(groceryListId: Int) {//TODO String...
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.remove_grocery_list))
            .setMessage(getString(R.string.remove_confirmation_message))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                deleteGroceryList(groceryListId)
            }
            .setCancelable(false)
            .setNegativeButton(getString(R.string.not_delete)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun toGroceryListDetails(idGroceryList: Int, view: View) {
        //TODO ... Implementar
        // fixme... Dedos gordos impide darle al menu y marcan este metodo por defecto (solucionar)
        Toast.makeText(requireContext(), "Clickado en $idGroceryList", Toast.LENGTH_SHORT).show()
    }

    private fun popupMenuOnGroceryListItem(idGroceryList: Int, view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.grocery_list_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.shareGroceryList -> {
                    shareGroceryList(idGroceryList)
                    true
                }

                R.id.removeGroceryList -> {
                    showRemoveAlertDialog(idGroceryList)
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    private fun deleteGroceryList(groceryListId: Int) {
        lifecycleScope.launch {
            groceryListViewModel.removeGroceryList(groceryListId)
        }
    }

    private fun shareGroceryList(groceryListId: Int) {
        lifecycleScope.launch {
            var groceryListWithProducts =
                groceryListViewModel.getGroceryListsWithProducts(groceryListId)

            if (groceryListWithProducts != null) {

                val message = StringBuilder("${groceryListWithProducts.name}\n")

                groceryListWithProducts.productsMap
                    .forEach { (name, products) ->
                        message.append("---- *$name* ----\n")
                        products
                            .forEach {
                                message.append("- (${it.quantity}) _${it.name}_ ")
                                if (it.adquired) {
                                    message.append(" ☑ \n")
                                } else {
                                    message.append(" ☐ \n")
                                }
                            }
                    }
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT, message.toString()
                    )
                    type = "text/plain"
                }
                val shareIntent =
                    Intent.createChooser(
                        intent,
                        getString(R.string.share_title, groceryListWithProducts.name)
                    )
                startActivity(shareIntent)
            }
        }
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