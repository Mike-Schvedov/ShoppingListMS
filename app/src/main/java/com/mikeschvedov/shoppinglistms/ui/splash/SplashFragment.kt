package com.mikeschvedov.shoppinglistms.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mikeschvedov.shoppinglistms.R
import com.mikeschvedov.shoppinglistms.databinding.FragmentSplashBinding
import com.mikeschvedov.shoppinglistms.util.logging.LoggerService
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashFragment : Fragment() {
    // ViewModel
    private lateinit var splashViewModel: SplashViewModel

    // Binding
    private var _binding:  FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ----------------------- ViewModel ----------------------- //
        splashViewModel =
            ViewModelProvider(this)[SplashViewModel::class.java]

        // ----------------------- Binding ----------------------- //
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val user = splashViewModel.getAuth().currentUser

        LoggerService.info("This is the current user, at the splash screen: ${user?.email}")

        if (user != null) {
            findNavController().navigate(R.id.action_splashFragment_to_HomeFragment,)
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_LoginFragment)
        }
        return root
    }


}