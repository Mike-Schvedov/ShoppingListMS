package com.mikeschvedov.shoppinglistms.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.mikeschvedov.shoppinglistms.R
import com.mikeschvedov.shoppinglistms.databinding.FragmentRegisterBinding
import com.mikeschvedov.shoppinglistms.databinding.FragmentSettingsBinding
import com.mikeschvedov.shoppinglistms.ui.register.RegisterViewModel
import com.mikeschvedov.shoppinglistms.util.getCurrentListId
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    // ViewModel
    private lateinit var settingsViewModel: SettingsViewModel

    // Binding
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ----------------------- ViewModel ----------------------- //
        settingsViewModel =
            ViewModelProvider(this)[SettingsViewModel::class.java]

        // ----------------------- Binding ----------------------- //
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // We want to remove the "ShoppingList-" part from the code
        var rawToken = requireContext().getCurrentListId()
        rawToken = rawToken.substring(13,20)
        binding.invideCodeTextView.text = rawToken

        return root
    }


}