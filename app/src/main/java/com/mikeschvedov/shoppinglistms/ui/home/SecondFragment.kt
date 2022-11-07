package com.mikeschvedov.shoppinglistms.ui.home

import android.app.Dialog
import android.content.Context
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
import com.mikeschvedov.shoppinglistms.R
import com.mikeschvedov.shoppinglistms.databinding.FragmentSecondBinding


class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.addNewItemBtn.setOnClickListener {
            createDialog()
        }

        binding.mylistToolbar.inflateMenu(R.menu.menu_main)
        binding.mylistToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.delete_selected -> {
                   // heartsViewModel.resetCounter()
                    true
                }
                R.id.delete_all -> {
                  //  heartsViewModel.resetCounter()
                    true
                }
                else -> false
            }
        }

        return root
    }


    private fun createDialog(){
        // Creating a dialog
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.dialog_animation

        // Array of Categories
       // val categoryArray = resources.getStringArray(R.array.categories)
        // Setting up the category adapter
       // val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item, categoryArray)

        val inputLayout = dialog.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
       // inputLayout.setAdapter(arrayAdapter)

        var selectedCategory = ""
        // When we select a category
        inputLayout.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, position, p3 ->
                // Store the category selected
               // selectedCategory = categoryArray[position]
            }

        // We want to hide the keyboard when clicking on the drop down menu
        inputLayout.setOnFocusChangeListener { v, hasFocus ->
            if (v != null){
                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }

        val dialogDatePicker = dialog.findViewById<DatePicker>(R.id.datePicker)

       // dialogDatePicker.updateDate(currentYear, currentMonth, currentDay)

        val saveBTN = dialog.findViewById<TextView>(R.id.save_button)
        saveBTN.setOnClickListener {

            val description = dialog.findViewById<EditText>(R.id.description_edittext).text.toString()
            val amount = dialog.findViewById<EditText>(R.id.amount_edittext).text.toString()
            // Getting the category as an Enum
         //   val categoryEnum = getCategoryEnum(selectedCategory)

            val pickerDay = dialogDatePicker.dayOfMonth
            val pickerMonth = dialogDatePicker.month + 1
            val pickerYear = dialogDatePicker.year

           // println("$description | $amount | $categoryEnum")

//            if(!description.isNullOrBlank() && !amount.isNullOrEmpty() && categoryEnum != Category.ERROR){
//                homeViewModel.saveNewExpenseInDB(Expense(
//                    description = description,
//                    amountSpent = amount.toInt(),
//                    category = categoryEnum,
//                    hour = currentHour,
//                    day = pickerDay,
//                    month = pickerMonth,
//                    year = pickerYear
//                ))
//                // Hiding the soft keyboard
//                if (it != null){
//                    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.hideSoftInputFromWindow(it.windowToken, 0)
//                }
//                // Closing the dialog
//                dialog.dismiss()
//            }else{
//                showAlertDialog()
//            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

