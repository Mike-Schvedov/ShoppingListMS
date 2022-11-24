package com.mikeschvedov.shoppinglistms.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.util.Logger
import com.mikeschvedov.shoppinglistms.R
import com.mikeschvedov.shoppinglistms.databinding.FragmentLoginBinding
import com.mikeschvedov.shoppinglistms.util.getCurrentListId
import com.mikeschvedov.shoppinglistms.util.logging.LoggerService
import com.mikeschvedov.shoppinglistms.util.logging.LoggerService.logger
import com.mikeschvedov.shoppinglistms.util.setCardFocus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    // ViewModel
    private lateinit var loginViewModel: LoginViewModel

    // Binding
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ----------------------- ViewModel ----------------------- //
        loginViewModel =
            ViewModelProvider(this)[LoginViewModel::class.java]

        // ----------------------- Binding ----------------------- //
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Making sure no user is signed in
        loginViewModel.getAuthentication().signOut()
        println("========================================================")
        LoggerService.debug("This is shared pref at login fragment on create: ${requireContext().getCurrentListId()}")
        val user = loginViewModel.getAuthentication().currentUser
        LoggerService.debug("This the user at login fragment on create: ${user?.email}")
        println("========================================================")


        setListeners()
        setCollectors()

        return root
    }

    private fun setCollectors() {
        // ----------------------- Login Button State ----------------------- //
        lifecycleScope.launch {
            loginViewModel.loginState.collect { loginState: Boolean ->
                // if both fields are valid make the login button enabled
                binding.loginButton.isEnabled = loginState
            }
        }
    }


    private fun setListeners() {
        binding.apply {
            // ----------------------- OnClick Listeners ----------------------- //
            binding.loginButton.setOnClickListener {
                val emailInput = binding.edittextEmail.text.toString()
                val passwordInput = binding.edittextPassword.text.toString()
                loginViewModel.getAuthentication().signInWithEmailAndPassword(emailInput, passwordInput)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            loginIntoApp()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "${task.exception}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            }
            registerGotoLink.setOnClickListener {
                findNavController().navigate(R.id.action_LoginFragment_to_registerFragment)
            }
            // ----------------------- Text Changed Listeners ----------------------- //
            edittextEmail.addTextChangedListener {
                loginViewModel.setEmailInput(it.toString())
            }
            edittextPassword.addTextChangedListener {
                loginViewModel.setPasswordInput(it.toString())
            }
            // ----------------------- Focus Listeners ----------------------- //
            edittextEmail.onFocusChangeListener =
                View.OnFocusChangeListener { _, hasFocus ->
                    cardViewEmail.apply {
                        requireContext().setCardFocus(this, hasFocus)
                    }
                }
            edittextPassword.onFocusChangeListener =
                View.OnFocusChangeListener { _, hasFocus ->
                    cardViewPassword.apply {
                        requireContext().setCardFocus(this, hasFocus)
                    }
                }
        }
    }

    private fun loginIntoApp() {
        findNavController().navigate(R.id.action_LoginFragment_to_HomeFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}