package com.example.smartlibrary1.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartlibrary1.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginResisterActivities : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_resister)
        }
    }
