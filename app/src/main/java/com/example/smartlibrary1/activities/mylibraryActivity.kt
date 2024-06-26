package com.example.smartlibrary1.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.smartlibrary1.R
import com.example.smartlibrary1.databinding.ActivityMylibraryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class mylibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMylibraryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMylibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.libraryHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)
    }
}
