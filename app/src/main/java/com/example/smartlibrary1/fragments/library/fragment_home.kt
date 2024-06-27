package com.example.smartlibrary1.fragments.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlibrary1.R
import com.example.smartlibrary1.adapters.HomeViewPagerAdapter
import com.example.smartlibrary1.adapters.SpecialBookAdapter
import com.example.smartlibrary1.databinding.FragmentHomeBinding
import com.example.smartlibrary1.fragments.categories.fragment_main_categories
import com.example.smartlibrary1.fragments.categories.fragment_manga
import com.example.smartlibrary1.util.Resource
import com.example.smartlibrary1.viewmodel.SearchViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint


class fragment_home : Fragment() {



    private lateinit var binding: FragmentHomeBinding
    private lateinit var specialBookAdapter: SpecialBookAdapter
    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val editText = binding.root.findViewById<EditText>(R.id.edittext)

        binding.buttonSearch.setOnClickListener {

            SearchViewModel.searchBookList.clear()
            val enteredtext = editText.text.toString()
            val linkSearchBook = constructSearchUrl(enteredtext)
            if (enteredtext != "") {
                viewModel.fetchSpecialBook(linkSearchBook,2)


            }
            else {
                binding.rvSearchResult.visibility = View.GONE
            }

        }
        binding.buttonImageSearch.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_home_to_fragment_search)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchRv()
        val categoriesFragment = arrayListOf<Fragment>(
            fragment_main_categories(),
            fragment_manga()
        )
        specialBookAdapter.onClick = {
            val b = Bundle().apply {
                putParcelable("book",it)
                putInt("number",1)//để chọn viewmodel seach
            }
            findNavController().navigate(R.id.action_fragment_home_to_fragment_book_detail,b)
        }

        lifecycleScope.launch {
            viewModel.specialBook.collectLatest {

                when (it) {
                    is Resource.Loading ->{

                    }

                    is Resource.Error -> TODO()
                    is Resource.Success -> {
                        specialBookAdapter.differ.submitList(it.data)
                        binding.rvSearchResult.visibility = View.VISIBLE
                    }
                    is Resource.Unspecified -> TODO()
                }
            }
        }



        binding.viewpagerHome.isUserInputEnabled = false


        binding.viewpagerHome.isUserInputEnabled = false

        val viewPager2Adapter =
            HomeViewPagerAdapter(categoriesFragment, childFragmentManager, lifecycle)
        binding.viewpagerHome.adapter = viewPager2Adapter
//        TabLayoutMediator(binding.tabLayout, binding.viewpagerHome) { tab, position ->
//            when (position) {
//                0 -> tab.text = "Main"
//                1 -> tab.text = "Manga"
//
//            }
//        }.attach()
    }
    private fun constructSearchUrl(query: String): String {
        val baseUrl = "https://nhatrangbooks.com/?s="
        val modifiedQuery = query.replace(" ", "+")
        return "$baseUrl$modifiedQuery&post_type=product"
    }
    private fun setupSearchRv() {

        specialBookAdapter = SpecialBookAdapter()
        binding.rvSearchResult.apply {
            layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL,false)
            adapter = specialBookAdapter
        }

    }


}