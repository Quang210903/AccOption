package com.example.smartlibrary1.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import androidx.navigation.findNavController
import com.example.smartlibrary1.R
import com.example.smartlibrary1.databinding.ActivityMylibraryBinding

import androidx.navigation.ui.setupWithNavController


class mylibraryActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityMylibraryBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val navController  = findNavController(R.id.libraryHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)

    }
}