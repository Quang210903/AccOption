package com.example.smartlibrary1.fragments.loginregister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.smartlibrary1.R
import com.example.smartlibrary1.activities.mylibraryActivity
import com.example.smartlibrary1.databinding.FragmentLoginBinding
import com.example.smartlibrary1.dialog.setupBottomSheetsDialog
import com.example.smartlibrary1.util.Resource
import com.example.smartlibrary1.viewmodel.LoginViewmodel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewmodel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[LoginViewmodel::class.java]
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDonthaveaccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_resisterFragments)
        }

        binding.apply {
            buttonloginlogin.setOnClickListener {
                val email = edemaillogin.text.toString().trim()
                val password = edpasswordlogin.text.toString().trim()

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(requireContext(), "Please enter both email and password", Toast.LENGTH_LONG).show()
                } else {
                    viewModel.login(email, password)
                }
            }
        }

        binding.tvForgotpasswordlogin.setOnClickListener {
            setupBottomSheetsDialog { email ->
                viewModel.resetPassword(email)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.resetPassword.collect {
                when (it) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        Snackbar.make(requireView(), "Reset password link sent to your email", Snackbar.LENGTH_LONG).show()
                    }
                    is Resource.Error -> {
                        Snackbar.make(requireView(), "Error: ${it.message}", Snackbar.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.login.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.buttonloginlogin.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.buttonloginlogin.revertAnimation()
                        Intent(requireActivity(), mylibraryActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        binding.buttonloginlogin.revertAnimation()
                    }
                    else -> Unit
                }
            }
        }
    }
}
