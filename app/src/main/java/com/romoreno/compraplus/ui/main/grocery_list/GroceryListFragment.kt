package com.romoreno.compraplus.ui.main.grocery_list

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.FirebaseAuth
import com.romoreno.compraplus.AlarmBroadcastReceiver
import com.romoreno.compraplus.AlarmBroadcastReceiver.Companion.CHANNEL_ID
import com.romoreno.compraplus.AlarmBroadcastReceiver.Companion.NOTIFICATION_BODY
import com.romoreno.compraplus.AlarmBroadcastReceiver.Companion.NOTIFICATION_ID
import com.romoreno.compraplus.AlarmBroadcastReceiver.Companion.NOTIFICATION_TITLE
import com.romoreno.compraplus.R
import com.romoreno.compraplus.databinding.FragmentGroceryListBinding
import com.romoreno.compraplus.ui.main.grocery_list.adapter.GroceryListAdapter
import com.romoreno.compraplus.ui.main.grocery_list.pojo.WhenGroceryListItemSelected
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class GroceryListFragment : Fragment() {

    private val groceryListViewModel: GroceryListViewModel by viewModels()

    private lateinit var groceryListAdapter: GroceryListAdapter

    private var _binding: FragmentGroceryListBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        createChannel()
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
                { id, name, date, view -> popupMenuOnGroceryListItem(id, name, date, view) }
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
        groceryListAdapter.updateList(state.groceryListModels)
        binding.rvGroceryList.apply {
            isVisible = true
            scrollToPosition(0)
        }
    }

    private fun errorState() {
        Toast.makeText(
            requireContext(),
            getString(R.string.search_error_information_message),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun initListeners() {
        binding.fabNewList.setOnClickListener { showGroceryListCreationDialogFragment() }
    }

    private fun showGroceryListCreationDialogFragment() {
        val groceryListCreationDialogFragment = GroceryListCreationDialogFragment()
        groceryListCreationDialogFragment.show(childFragmentManager, "LIST_CREATION")
    }

    private fun scheduleReminder(name: String, date: Date) {
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .setHour(12)
            .setMinute(0)
            .setTitleText(getString(R.string.reminder_me_grocery_list_on, name))
            .build()

        timePicker.addOnPositiveButtonClickListener({
            val hour = timePicker.hour
            val minute = timePicker.minute
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)

            scheduleNotification(name, calendar.time)
        })

        timePicker.show(childFragmentManager, "TIME_PICKER")
    }

    private fun showRemoveAlertDialog(groceryListId: Int) {
        MaterialAlertDialogBuilder(requireContext())
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
        Toast.makeText(requireContext(), "Clickado en $idGroceryList", Toast.LENGTH_SHORT).show()
    }

    private fun popupMenuOnGroceryListItem(
        idGroceryList: Int,
        name: String,
        date: Date,
        view: View
    ) {
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

                R.id.scheduleReminder -> {
                    scheduleReminder(name, date)
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
            val groceryListWithProducts =
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

    private fun scheduleNotification(name: String, date: Date) {
        val intent = Intent(requireContext().applicationContext, AlarmBroadcastReceiver::class.java)
        intent.putExtra(NOTIFICATION_TITLE, getString(R.string.notification_title))
        intent.putExtra(NOTIFICATION_BODY, getString(R.string.notification_body, name))

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext().applicationContext,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val message: String

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (alarmManager.canScheduleExactAlarms() &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {


            alarmManager.setExact(AlarmManager.RTC_WAKEUP, date.time, pendingIntent)

            val numberDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                .format(date)
            message = "${getString(R.string.scheduled_reminder)} $numberDate"
        } else {
            message = getString(R.string.scheduling_problem)
        }
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }


    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "GroceryListChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
                .apply {
                    description = "CANAL"
                }

            val notificationManager: NotificationManager = requireContext()
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

}