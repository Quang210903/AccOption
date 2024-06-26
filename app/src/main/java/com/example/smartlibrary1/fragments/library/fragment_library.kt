package com.example.smartlibrary1.fragments.library

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlibrary1.R
import com.example.smartlibrary1.adapters.LibraryAdapter
import com.example.smartlibrary1.databinding.FragmentLibraryBinding
import com.example.smartlibrary1.helper.BookDatabaseHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint


class fragment_library : Fragment() {
    private lateinit var binding: FragmentLibraryBinding
    private lateinit var libraryAdapter: LibraryAdapter

    private lateinit var db: BookDatabaseHelper
    private lateinit var fragmentContext: Context



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

        binding = FragmentLibraryBinding.inflate(inflater)
        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpLibraryBook()

        libraryAdapter.onClick = {
            val b = Bundle().apply {
                putParcelable("librarybook",it)

            }
            findNavController().navigate(R.id.action_fragment_library_to_fragment_library_book_detail,b)
        }

        val bookList = db.getAllBooks()
        libraryAdapter.differ.submitList(bookList)

        binding.morebtn.setOnClickListener {
            showPopupMenu(it)
        }



    }

    private fun showPopupMenu(view: View?) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_library_options, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sortA2Z -> {
                    handleOption1()
                    true
                }
                R.id.sortZ2A -> {
                    handleOption2()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun handleOption1() {
        val bookList = db.getAllBooks()
        val sortedBookList = bookList.sortedBy { it.title }
        libraryAdapter.differ.submitList(sortedBookList)
    }

    private fun handleOption2() {
        val bookList = db.getAllBooks()
        val sortedBookList = bookList.sortedByDescending { it.title }
        libraryAdapter.differ.submitList(sortedBookList)
    }

    private fun setUpLibraryBook() {

        val books = db.getAllBooks()  // Lấy danh sách sách từ cơ sở dữ liệu
        libraryAdapter = LibraryAdapter(requireContext(),books, db) // Khởi tạo adapter với db để xóa sách
        binding.rvLibrary.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = libraryAdapter
        }
    }



}