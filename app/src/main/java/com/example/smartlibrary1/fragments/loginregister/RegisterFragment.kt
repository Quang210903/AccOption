@file:Suppress("DEPRECATION")

package com.example.smartlibrary1.fragments.loginregister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.smartlibrary1.R
import com.example.smartlibrary1.data.User
import com.example.smartlibrary1.databinding.FragmentRegisterBinding
import com.example.smartlibrary1.util.RegisterVadiation
import com.example.smartlibrary1.util.Resource
import com.example.smartlibrary1.viewmodel.RegisterViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val TAG = "RegisterFragment"
@AndroidEntryPoint
class  RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
//    private val viewModel: RegisterViewmodel by viewModels()
    private lateinit var viewModel: RegisterViewmodel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[RegisterViewmodel::class.java]
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDoyouhavehaveaccount.setOnClickListener{
            findNavController().navigate(R.id.action_resisterFragments_to_loginFragment)
        }

        binding.apply {
            buttonRegisterRegister.setOnClickListener {
                val user = User(
                    edFirstNamRegister.text.toString().trim(),
                    edLastNameRegister.text.toString().trim(),
                    edEmailRegister.text.toString().trim()
                )
                val password = edPasswordRegister.text.toString()
                viewModel.createAccountWiththeEmailAndPassword(user, password)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.register.collect {
                    when (it) {
                        is Resource.Loading -> {
                            binding.buttonRegisterRegister.startAnimation()
                        }

                        is Resource.Success -> {
                            Log.d("test ", it.message.toString())
                            binding.buttonRegisterRegister.revertAnimation()
                        }

                        is Resource.Error -> {
                            Log.e(TAG, it.message.toString())
                            binding.buttonRegisterRegister.revertAnimation()
                        }

                        else -> Unit
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.vadiation.collect { validation ->
                if (validation.email is RegisterVadiation.Failure) {
                    withContext(Dispatchers.Main) {
                        binding.edEmailRegister.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }
                if (validation.password is RegisterVadiation.Failure) {
                    withContext(Dispatchers.Main) {
                        binding.edPasswordRegister.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                    }

                }
            }
        }
    }
}
