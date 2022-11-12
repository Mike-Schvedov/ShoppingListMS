package com.mikeschvedov.shoppinglistms.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mikeschvedov.shoppinglistms.R
import com.mikeschvedov.shoppinglistms.databinding.FragmentRegisterBinding
import com.mikeschvedov.shoppinglistms.models.User
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

        return root
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
        binding.registerButton.setOnClickListener {
            val email = binding.edittextEmail.text.toString()
            val password = binding.edittextPassword.text.toString()

            registerViewModel.getAuthentication().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(requireContext(), "Registration Complete", Toast.LENGTH_SHORT).show()
                    val user = registerViewModel.getAuthentication().currentUser
                    val userID = user?.uid
                    val userEmail = user?.email
                    // The user decided to create a new list, or join an existing one:
                    // mock decided to create a new one:
                    val newListID = generateNewListID(userID)

                    // In order to save more data about the user (other than the uid, email and password)
                    // we also need to create him in the database

                    addUserToDatabase(userID!!,  userEmail!!, newListID)
                    // Then we take him to the home page
                    takeUserToHomeFragment()
                }else{
                    Toast.makeText(requireContext(), "${it.exception}", Toast.LENGTH_SHORT).show()
                }
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

    private fun generateNewListID(userID: String?): String {
        val trimUserID = userID?.substring(0, 7)
        return "ShoppingList-$trimUserID"
    }

    private fun addUserToDatabase(userID: String, userEmail: String, newListID: String) {
        val user = User(userID, userEmail, newListID)
        registerViewModel.addUserToDatabase(user)
        // Saving the new assigned Shopping List ID into the sharedPref
        saveAssignedList(newListID)
    }

    private fun saveAssignedList(newListID: String) {
        requireContext().setCurrentListId(newListID)
    }

    private fun takeUserToHomeFragment() {
        findNavController().navigate(R.id.action_registerFragment_to_HomeFragment)
    }
}