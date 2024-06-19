package com.example.smartlibrary1.fragments.library

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartlibrary1.R
import android.widget.Toast
import androidx.core.content.ContextCompat

import com.example.smartlibrary1.data.BookDetail
import com.example.smartlibrary1.data.Note
import com.example.smartlibrary1.databinding.FragmentLibraryBookDetailBinding
import com.example.smartlibrary1.helper.BookDatabaseHelper
import com.example.smartlibrary1.util.Resource
import com.example.smartlibrary1.util.hideBottomNavigationView
import com.example.smartlibrary1.viewmodel.BookDetailViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class fragment_library_book_detail : Fragment() {
    private val args by navArgs<fragment_library_book_detailArgs>()
    private lateinit var binding: FragmentLibraryBookDetailBinding
    private lateinit var db: BookDatabaseHelper
    private lateinit var bookDetail1: BookDetail
    private lateinit var note: Note
    private lateinit var fragmentContext: Context
    inner class HolderLibraryBook(itemView: View) : RecyclerView.ViewHolder(itemView){

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLibraryBookDetailBinding.inflate(layoutInflater)
        db = BookDatabaseHelper(fragmentContext)

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLibraryBookDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookDetail = args.librarybook

        Glide.with(this)
            .load(bookDetail.image)
            .into(binding.booKImage)

        binding.bookTitle.text = bookDetail.title
        binding.bookAuthor.text = bookDetail.author
        binding.bookGenre.text = bookDetail.genre
        binding.bookDecription.text = bookDetail.description



//        binding.imageClose.setOnClickListener {
//            binding.imageClose.setBackgroundColor(getResources().getColor(R.color.g_gray500))
//            findNavController().popBackStack()
//
//        }


    }

}