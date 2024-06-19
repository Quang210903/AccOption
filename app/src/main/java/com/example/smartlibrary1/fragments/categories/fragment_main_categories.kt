package com.example.smartlibrary1.fragments.categories

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlibrary1.R
import com.example.smartlibrary1.adapters.BestBookAdapter
import com.example.smartlibrary1.adapters.SpecialBookAdapter
import com.example.smartlibrary1.databinding.FragmentMainCategoriesBinding
import com.example.smartlibrary1.fragments.library.fragment_home
import com.example.smartlibrary1.helper.BookDatabaseHelper
import com.example.smartlibrary1.util.Resource
import com.example.smartlibrary1.util.showBottomNavigationView
import com.example.smartlibrary1.viewmodel.MainCategoriesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class fragment_main_categories : Fragment() {
    private lateinit var binding: FragmentMainCategoriesBinding
    private lateinit var specialBookAdapter: SpecialBookAdapter
    private lateinit var bestBookAdapter: BestBookAdapter
    private lateinit var db: BookDatabaseHelper
    private lateinit var fragmentContext: Context
    private val viewModel by viewModels<MainCategoriesViewModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = BookDatabaseHelper(fragmentContext)


    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
        db = BookDatabaseHelper(fragmentContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainCategoriesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupSpecialBookRv()
        setupBestBookRv()

        specialBookAdapter.onClick = {
            val b = Bundle().apply {
                putParcelable("book",it)
                putInt("number",0)//để chọn viewmodel
            }
            findNavController().navigate(R.id.action_fragment_home_to_fragment_book_detail,b)
        }
        bestBookAdapter.onClick = {
            val b = Bundle().apply {
                putParcelable("book",it)
                putInt("number",0)//để chọn viewmodel
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
                    }
                    is Resource.Unspecified -> TODO()
                }
            }
        }
       lifecycleScope.launch {
            viewModel.bestBook.collectLatest {
                when (it) {
                    is Resource.Loading ->{

                    }

                    is Resource.Error -> TODO()
                    is Resource.Success -> {
                        bestBookAdapter.differ.submitList(it.data)
                    }
                    is Resource.Unspecified -> TODO()
                }
            }
        }

    }

    private fun setupSpecialBookRv() {
        specialBookAdapter = SpecialBookAdapter()
        binding.rvSpecialProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = specialBookAdapter
        }

    }

    private fun setupBestBookRv() {
        bestBookAdapter = BestBookAdapter()
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestBookAdapter
        }
    }

    override fun onResume() {

        super.onResume()
        showBottomNavigationView()
    }

}