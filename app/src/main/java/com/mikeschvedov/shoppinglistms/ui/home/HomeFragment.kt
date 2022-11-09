package com.mikeschvedov.shoppinglistms.ui.home

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.mikeschvedov.shoppinglistms.R
import com.mikeschvedov.shoppinglistms.databinding.FragmentHomeBinding
import com.mikeschvedov.shoppinglistms.models.GroceryItem
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    // ViewModel
    private lateinit var homeViewModel: HomeViewModel

    // View Binding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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

        val mAuth = FirebaseAuth.getInstance()
        val mUser = mAuth.currentUser!!

        binding.textviewtoken.text = mUser.uid

        binding.addNewItemBtn.setOnClickListener {
            openAddEntryDialog()
        }

        binding.mylistToolbar.inflateMenu(R.menu.menu_main)
        binding.mylistToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.delete_selected -> {
                   homeViewModel.deleteMarkedItems()
                    true
                }
                R.id.delete_all -> {
                    homeViewModel.deleteAll()
                    true
                }
                else -> false
            }
        }

        return root
    }


    private fun openAddEntryDialog(){
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

            if(nameInput.isNotBlank()){
                homeViewModel.saveNewEntry(
                    GroceryItem ( name = nameInput, amount = amountInput ))
                // Hiding the soft keyboard
                if (it != null){
                    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(it.windowToken, 0)
                }
                // Closing the dialog
                dialog.dismiss()
            }else{
                showAlertDialog()
            }
        }

        val cancelBTN = dialog.findViewById<TextView>(R.id.cancel_button)
        cancelBTN.setOnClickListener {
            // Hiding the soft keyboard
            if (it != null){
                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)
            }
            // Closing the dialog
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showAlertDialog() {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(requireContext())

        // set message of alert dialog
        dialogBuilder.setMessage("No item provided!")
            .setCancelable(false)
            .setNegativeButton("Ok", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })
        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Alert")
        // show alert dialog
        alert.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

