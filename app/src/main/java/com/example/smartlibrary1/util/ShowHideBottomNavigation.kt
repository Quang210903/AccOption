package com.example.smartlibrary1.util

import androidx.fragment.app.Fragment
import com.example.smartlibrary1.activities.mylibraryActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNavigationView(){
    val bottomNavigationView =
        (activity as mylibraryActivity).findViewById<BottomNavigationView>(
            com.example.smartlibrary1.R.id.bottomNavigation
        )
    bottomNavigationView.visibility = android.view.View.GONE
}

fun Fragment.showBottomNavigationView(){
    val bottomNavigationView =
        (activity as mylibraryActivity).findViewById<BottomNavigationView>(
            com.example.smartlibrary1.R.id.bottomNavigation
        )
    bottomNavigationView.visibility = android.view.View.VISIBLE
}