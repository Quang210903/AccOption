package com.example.smartlibrary1.fragments.library

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.smartlibrary1.R
import com.example.smartlibrary1.data.BookDetail
import com.example.smartlibrary1.databinding.FragmentBookDetailBinding
import com.example.smartlibrary1.helper.BookDatabaseHelper
import com.example.smartlibrary1.util.Resource
import com.example.smartlibrary1.util.hideBottomNavigationView
import com.example.smartlibrary1.viewmodel.BookDetailViewModel
import com.example.smartlibrary1.viewmodel.SearchBookDetailViewmodel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class fragment_book_detail : Fragment() {
    private val args by navArgs<fragment_book_detailArgs>()
    private lateinit var binding: FragmentBookDetailBinding
    private lateinit var db: BookDatabaseHelper
    private lateinit var bookDetail1: BookDetail
    private lateinit var fragmentContext: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        // Inflate the layout for this fragment

        hideBottomNavigationView()
        binding = FragmentBookDetailBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val book = args.book
        val number = args.number








        if(number == 0){
            val viewModel = BookDetailViewModel(book)
            lifecycleScope.launch {
                viewModel.bookDetail.collectLatest {
                    when (it) {
                        is Resource.Loading -> {

                        }

                        is Resource.Error -> TODO()
                        is Resource.Success -> {
                            val bookDetail = viewModel.getBookDetail()
                            bookDetail1 = bookDetail

                            binding.apply {
                                bookTitle.text = bookDetail.title
                                getView()?.let { it1 ->
                                    Glide.with(it1).load(book.image).into(booKImage)
                                }
                                bookGenre.text = bookDetail.genre
                                bookDecription.text = bookDetail.description
                                bookAuthor.text = bookDetail.author


                            }

                        }


                        is Resource.Unspecified -> TODO()
                        is Resource.Error -> TODO()
                        is Resource.Loading -> TODO()
                        is Resource.Success -> TODO()
                        is Resource.Unspecified -> TODO()
                        is Resource.Error -> TODO()
                        is Resource.Loading -> TODO()
                        is Resource.Success -> TODO()
                        is Resource.Unspecified -> TODO()
                    }

                }
            }
        }else {
            val viewModel = SearchBookDetailViewmodel(book)
            lifecycleScope.launch {
                viewModel.bookDetail.collectLatest {
                    when (it) {
                        is Resource.Loading -> {

                        }

                        is Resource.Error -> TODO()
                        is Resource.Success -> {
                            val bookDetail = viewModel.getBookDetail()
                            bookDetail1 = bookDetail

                            binding.apply {
                                bookTitle.text = bookDetail.title
                                getView()?.let { it1 ->
                                    Glide.with(it1).load(book.image).into(booKImage)
                                }
                                bookGenre.text = bookDetail.genre
                                bookDecription.text = bookDetail.description
                                bookAuthor.text = bookDetail.author


                            }

                        }


                        is Resource.Unspecified -> TODO()
                        is Resource.Error -> TODO()
                        is Resource.Loading -> TODO()
                        is Resource.Success -> TODO()
                        is Resource.Unspecified -> TODO()
                        is Resource.Error -> TODO()
                        is Resource.Loading -> TODO()
                        is Resource.Success -> TODO()
                        is Resource.Unspecified -> TODO()
                    }

                }
            }
        }

        binding.buttonAddBook.setOnClickListener {

//            db.insertBook(bookDetail1.author, bookDetail1.genre,bookDetail1.description, bookDetail1.title, book.image, book.url)
//            binding.buttonAddBook.setBackgroundColor(R.color.black)

            val result = db.insertBook(bookDetail1.author, bookDetail1.genre, bookDetail1.description, bookDetail1.title, book.image, book.url)

            if (result != -1L) { // assuming insertBook returns -1 if the insert fails
                Toast.makeText(requireContext(), "Book added", Toast.LENGTH_SHORT).show()
                binding.buttonAddBook.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.g_gray500))
            } else {
                Toast.makeText(requireContext(), "Failed to add book", Toast.LENGTH_SHORT).show()
            }
        }


    }
}

