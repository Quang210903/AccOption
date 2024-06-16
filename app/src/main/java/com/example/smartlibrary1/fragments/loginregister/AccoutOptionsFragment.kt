package com.example.smartlibrary1.fragments.loginregister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartlibrary1.R
import com.example.smartlibrary1.databinding.FragmentAccountOptionsBinding

class AccoutOptionsFragment:Fragment(R.layout.fragment_account_options) {
    private lateinit var binding: FragmentAccountOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountOptionsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonloginaccountoption.setOnClickListener {
            findNavController().navigate(R.id.action_accoutOptionsFragment_to_loginFragment)
        }
        binding.buttonregisteraccoutoption.setOnClickListener {
            findNavController().navigate(R.id.action_accoutOptionsFragment_to_resisterFragments)
        }
    }
}
