package com.example.smartlibrary1.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeViewPagerAdapter(
    private val fragments: List<Fragment>,
    fm: FragmentManager,
    lifecycle: androidx.lifecycle.Lifecycle
): FragmentStateAdapter(fm,lifecycle) {

    override fun getItemCount(): Int {


        return fragments.size
    }
    override fun createFragment(position: Int): Fragment {

        return fragments[position]
    }


}