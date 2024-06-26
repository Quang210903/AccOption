package com.example.smartlibrary1.fragments.setting

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.smartlibrary1.R
import com.example.smartlibrary1.activities.LoginResisterActivities
import com.example.smartlibrary1.databinding.FragmentSettingBinding
import com.example.smartlibrary1.util.Resource
import com.example.smartlibrary1.util.showBottomNavigationView
import com.example.smartlibrary1.viewmodel.SettingViewmodel
import com.google.firebase.BuildConfig
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@Suppress("DEPRECATION")
@AndroidEntryPoint
class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private val viewModel: SettingViewmodel by lazy {
        ViewModelProvider(this)[SettingViewmodel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.constraintSettings.setOnClickListener {
                findNavController().navigate(R.id.action_settingFragment_to_fragment_profile)
        }

        binding.linearLogOut.setOnClickListener{
            viewModel.logout()
            val intent = Intent (requireActivity(), LoginResisterActivities::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        binding.tvVersion.text = "Version ${BuildConfig.VERSION_NAME}"

        lifecycleScope.launchWhenStarted {
            viewModel.user.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarSettings.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressbarSettings.visibility = View.GONE
                        Glide.with(requireView()).load(it.data!!.imagePath).error(
                            ColorDrawable(
                            Color.BLACK)
                        ).into(binding.imageUser)
                        binding.tvUserName.text = "${it.data.firstname} ${it.data.lastname}"
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.progressbarSettings.visibility = View.GONE
                    }
                    else -> Unit
                }
            }
        }


    }
    override fun onResume() {
        super.onResume()

        showBottomNavigationView()
    }

}
