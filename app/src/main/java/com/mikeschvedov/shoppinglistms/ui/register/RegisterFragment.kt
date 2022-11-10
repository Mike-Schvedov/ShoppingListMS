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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mikeschvedov.shoppinglistms.R
import com.mikeschvedov.shoppinglistms.databinding.FragmentRegisterBinding
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

    private fun takeUserToHomeFragment() {
        findNavController().navigate(R.id.action_registerFragment_to_HomeFragment)
    }
}