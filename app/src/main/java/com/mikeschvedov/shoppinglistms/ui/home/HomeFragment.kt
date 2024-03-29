package com.mikeschvedov.shoppinglistms.ui.home

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikeschvedov.shoppinglistms.R
import com.mikeschvedov.shoppinglistms.data.network.models.NotificationData
import com.mikeschvedov.shoppinglistms.data.network.models.PushNotification
import com.mikeschvedov.shoppinglistms.databinding.FragmentHomeBinding
import com.mikeschvedov.shoppinglistms.models.GroceryItem
import com.mikeschvedov.shoppinglistms.ui.adapters.GroceryListAdapter
import com.mikeschvedov.shoppinglistms.util.getCurrentListId
import com.mikeschvedov.shoppinglistms.util.logging.LoggerService
import com.mikeschvedov.shoppinglistms.util.setCurrentListId
import com.mikeschvedov.shoppinglistms.util.setMessageLockState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    // ViewModel
    private lateinit var homeViewModel: HomeViewModel

    // View Binding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Adapter
    lateinit var adapter: GroceryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // ----------------------- ViewModel ----------------------- //
        homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]
        // ----------------------- View Binding ----------------------- //
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // ----------------------- Adapter ----------------------- //
        adapter = homeViewModel.getAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter


        println("========================================================")
        LoggerService.debug("This is shared pref at home fragment on create: ${requireContext().getCurrentListId()}")
        val user = homeViewModel.getCurrentUser()
        LoggerService.debug("This the user at home fragment on create: ${user?.email}")
        println("========================================================")
        // Making sure the lock state is false, so we can receive messages
        requireContext().setMessageLockState(false)

        // Save current user's connected shop list id - into the shared pref, so the firebase manager
        // can use it to get the full list (the observer will save the data into the sharedpref)
        homeViewModel.getUserConnectedShoppingListID()

        observers()

        binding.addNewItemBtn.setOnClickListener {
            openAddEntryDialog()
        }

        binding.mylistToolbar.inflateMenu(R.menu.menu_main)

        binding.mylistToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.options_delete_selected -> {
                    showWarningDialog("Delete selected entries?") {
                        homeViewModel.deleteMarkedItems()
                    }
                    true
                }
                R.id.options_delete_all -> {
                    showWarningDialog("This will delete all entries.\nAre you sure?") {
                        homeViewModel.deleteAll()
                    }
                    true
                }
                R.id.options_settings -> {
                    openSettingsMenu()
                    true
                }
                R.id.options_logout -> {
                    logoutFromApp()
                    true
                }
                else -> false
            }
        }

        return root
    }


    private fun logoutFromApp() {
        findNavController().navigate(R.id.action_HomeFragment_to_LoginFragment)
        homeViewModel.signOutUser()
    }

    private fun observers() {
        homeViewModel.groceryListLiveData.observe(viewLifecycleOwner) { items: List<GroceryItem> ->
            adapter.setNewData(items)
        }
        homeViewModel.shoppingListID.observe(viewLifecycleOwner) { listID ->
            LoggerService.debug("OBSERVING THE CURRENT LIST ID IN HOME FRAGMENT: $listID")
            // Saving connected list into the sharedPref
            requireContext().setCurrentListId(listID)
            LoggerService.debug("Subscribing device to notification topic")
            // Subscribing to the topic as the listID
            homeViewModel.subscribeDeviceToTopic(listID)
            LoggerService.debug("This is shared pref at home fragment after getting the id form database and saving in sharedpref : ${requireContext().getCurrentListId()}")
            homeViewModel.fetchGroceryData()
        }
    }

    private fun openSettingsMenu() {
        findNavController().navigate(R.id.action_HomeFragment_to_settingsFragment)
    }

    private fun openAddEntryDialog() {
        // Creating a dialog
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.dialog_animation

        val saveBTN = dialog.findViewById<TextView>(R.id.save_button)
        saveBTN.setOnClickListener {

            val nameInput = dialog.findViewById<EditText>(R.id.description_edittext).text.toString()
            val amountInput = dialog.findViewById<EditText>(R.id.amount_edittext).text.toString()

            println("$nameInput | $amountInput")

            if (nameInput.isNotBlank()) {
                homeViewModel.saveNewEntry(
                    GroceryItem(name = nameInput, amount = amountInput, marked = false)
                )

                // Sending notification
                sendNotificationOnItemAddition(nameInput, amountInput)

                // Hiding the soft keyboard
                if (it != null) {
                    val imm =
                        activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(it.windowToken, 0)
                }
                // Closing the dialog
                dialog.dismiss()
            } else {
                showAlertDialog()
            }
        }

        val cancelBTN = dialog.findViewById<TextView>(R.id.cancel_button)
        cancelBTN.setOnClickListener {
            // Hiding the soft keyboard
            if (it != null) {
                val imm =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)
            }
            // Closing the dialog
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun sendNotificationOnItemAddition(nameInput: String, amountInput: String) {
        // Getting the topic (is the same as the listId)
        val topic = requireContext().getCurrentListId()

        val itemTitle = "פריט חדש נוסף לרשימה"
        if (itemTitle.isNotEmpty()) {
            PushNotification(
                NotificationData(itemTitle, "$amountInput $nameInput"),
                "/topics/$topic"
            ).also {
                //Trigger Lock State
                requireContext().setMessageLockState(true)
                LoggerService.info("Sent message to topic: /topics/$topic")
                //Sending the Notification
                homeViewModel.sendNotification(it)
            }
        }
    }

    private fun showAlertDialog() {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(requireContext())

        // set message of alert dialog
        dialogBuilder.setMessage("No item provided!")
            .setCancelable(false)
            .setNegativeButton("Ok", DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
            })
        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Alert")
        // Set text color
        alert.setOnShowListener(OnShowListener { dialog -> //
            val buttonNegative: Button = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
            buttonNegative.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange_strong))
        })
        // show alert dialog
        alert.show()
    }


    private fun showWarningDialog(msg: String, method: () -> Unit) {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(requireContext())

        // set message of alert dialog
        dialogBuilder.setMessage(msg)
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, _ ->
                method.invoke()
                dialog.cancel()
            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
            })
        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Warning")
        // Set text color
        alert.setOnShowListener(OnShowListener { dialog -> //
            val buttonPositive: Button = alert.getButton(DialogInterface.BUTTON_POSITIVE)
            buttonPositive.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange_strong))
            val buttonNegative: Button = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
            buttonNegative.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange_strong))
        })
        // show alert dialog
        alert.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        LoggerService.info("")
    }

    override fun onDestroy() {
        super.onDestroy()
        LoggerService.info("")
    }

    override fun onResume() {
        super.onResume()
        LoggerService.info("")
    }
}

