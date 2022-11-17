package com.mikeschvedov.shoppinglistms.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mikeschvedov.shoppinglistms.R
import com.mikeschvedov.shoppinglistms.databinding.FragmentRegisterBinding
import com.mikeschvedov.shoppinglistms.models.User
import com.mikeschvedov.shoppinglistms.util.logging.LoggerService
import com.mikeschvedov.shoppinglistms.util.setCurrentListId
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RegisterFragment : Fragment() {

    // ViewModel
    private lateinit var registerViewModel: RegisterViewModel

    // Binding
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private var allInviteCodesList = listOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ----------------------- ViewModel ----------------------- //
        registerViewModel =
            ViewModelProvider(this)[RegisterViewModel::class.java]

        // ----------------------- Binding ----------------------- //
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setListeners()

        setCollectors()

        setObservers()

        return root
    }



    private fun setObservers() {
        registerViewModel.inviteCodesList.observe(viewLifecycleOwner) { list ->
            allInviteCodesList = list
            println("This is the list: $allInviteCodesList")
        }
    }

    private fun setCollectors() {
        lifecycleScope.launch {
            registerViewModel.loginState.collect { loginState: Boolean ->
                // if both fields are valid make the login button enabled
                binding.registerButton.isEnabled = loginState
            }
        }

    }

    private fun setListeners() {
        // ----------------------- OnClick Listeners ----------------------- //
        val optionsRadioGroup = binding.groupradio
        var selectedOption = 1
        optionsRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            // selected button
            val radioButton: RadioButton = radioGroup.findViewById(i)
            if (radioButton.id == R.id.radia_id2){
                binding.cardViewJoinlist.visibility = View.VISIBLE
                selectedOption = 2
            }else{
                binding.cardViewJoinlist.visibility = View.GONE
                selectedOption = 1
            }
        }

        binding.registerButton.setOnClickListener {
            val email = binding.edittextEmail.text.toString()
            val password = binding.edittextPassword.text.toString()
            var inviteCode: String
            val isValid: Boolean

            if (selectedOption == 2){
                LoggerService.debug("Option 2 was selected, join another list")
                inviteCode = binding.edittextConfirmJoinlist.text.toString()
                inviteCode = "ShoppingList-${inviteCode}"
                if (inviteCode.isNotEmpty()){
                    isValid = validateInviteCode(inviteCode)
                    if (isValid){
                        LoggerService.info("Invite Code is Valid")
                        performRegistration(email, password, inviteCode)
                    }else{
                        LoggerService.info("Invite Code is Invalid")
                        Toast.makeText(requireContext(), "Invalid invite code", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    LoggerService.info("Invite Code is Empty")
                    Toast.makeText(requireContext(), "Please enter the invite code", Toast.LENGTH_SHORT).show()
                }
            }else{ // selected option is 1
                LoggerService.debug("Option 1 was selected, create a new")
                performRegistration(email, password, null)
            }
        }

        binding.loginGotoLink.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_LoginFragment)
        }

        // ----------------------- Text Changed Listeners ----------------------- //
        binding.edittextEmail.addTextChangedListener {
            registerViewModel.setEmailInput(it.toString())
        }
        binding.edittextPassword.addTextChangedListener {
            registerViewModel.setPasswordInput(it.toString())
        }
        binding.edittextConfirmPassword.addTextChangedListener {
            registerViewModel.setConfirmPasswordInput(it.toString())
        }
    }

    private fun performRegistration(email: String, password: String, inviteCode: String?) {
        registerViewModel.getAuthentication().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(requireContext(), "Registration Complete", Toast.LENGTH_SHORT).show()
                val user = registerViewModel.getAuthentication().currentUser
                val userID = user?.uid
                val userEmail = user?.email

                val shoppingListId: String
               if (inviteCode == null){  // The user decided to create a new list:
                   shoppingListId = generateNewListID(userID)
               }else{ // The user provided a valid invite code:
                   shoppingListId = inviteCode
               }
                // In order to save more data about the user (other than the uid, email and password)
                // we also need to create him in the database
                addUserToDatabase(userID!!,  userEmail!!, shoppingListId)
                // Then we take him to the home page
                takeUserToHomeFragment()
            }else{
                Toast.makeText(requireContext(), "${it.exception}", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun validateInviteCode(inviteCode: String): Boolean {
        LoggerService.debug("Validating inviteCode: $inviteCode")
        // if the inviteCode exists in the list return true
        LoggerService.debug("These are the valid invite codes:$allInviteCodesList")
        val contains = allInviteCodesList.contains(inviteCode)
        println(contains)
        return contains
    }

    private fun generateNewListID(userID: String?): String {
        val trimUserID = userID?.substring(0, 7)
        return "ShoppingList-$trimUserID"
    }

    private fun addUserToDatabase(userID: String, userEmail: String, listID: String) {
        val user = User(userID, userEmail, listID)
        registerViewModel.addUserToDatabase(user)
        // Saving the new assigned Shopping List ID into the sharedPref
        saveAssignedList(listID)
    }

    private fun saveAssignedList(newListID: String) {
        println("SETTINGS THIS ID INTO SERDPREFF: $newListID")
        requireContext().setCurrentListId(newListID)
    }

    private fun takeUserToHomeFragment() {
        findNavController().navigate(R.id.action_registerFragment_to_HomeFragment)
    }

    override fun onResume() {
        super.onResume()
        LoggerService.debug("Getting all valide invite codes")
        registerViewModel.getAllValidInviteCodes()
    }
}