package com.example.smartlibrary1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.engine.Resource
import com.example.smartlibrary1.data.Book
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class MainCategoriesViewModel:ViewModel() {
    private val _specialBooks = MutableStateFlow<com.example.smartlibrary1.util.Resource<List<Book>>>(com.example.smartlibrary1.util.Resource.Unspecified())
    private val _bestBooks = MutableStateFlow<com.example.smartlibrary1.util.Resource<List<Book>>>(com.example.smartlibrary1.util.Resource.Unspecified())
    val specialBook : MutableStateFlow<com.example.smartlibrary1.util.Resource<List<Book>>> = _specialBooks
    val bestBook : MutableStateFlow<com.example.smartlibrary1.util.Resource<List<Book>>> = _bestBooks

    init{
        fetchSpecialBook()
        fetchBestBook()

    }
    fun fetchSpecialBook() {

        viewModelScope.launch {
            _specialBooks.emit(com.example.smartlibrary1.util.Resource.Loading())
            try {
                // Fetch HTML content directly using Jsoup
                val document = withContext(Dispatchers.IO) {
                    Jsoup.connect("https://www.fahasa.com/sach-trong-nuoc/van-hoc-trong-nuoc/tieu-thuyet.html").get()
                }

                // Parse HTML content
                val elements = document.select(".product-image") // Adjust selector
                val specialBookList = mutableListOf<Book>()
                var i = 0
                for (element in elements) {
                    // Process each element as needed
                    i++
                    val title = element.getElementsByTag("a").attr("title")
                    val image = element.getElementsByTag("img").attr("data-src")
                    val url = element.getElementsByTag("a").attr("href")
                    val author = element.getElementsByTag("a").attr("author")
                    val genre = element.getElementsByTag("a").attr("genre")
                    val description = element.getElementsByTag("a").attr("description")
                    if (i%2==0) {

                        continue
                    }
                    val book = Book(title,image,url,author,genre,description)
                    specialBookList += book
                    println(i)
                    // Update UI or perform further processing

                }


                _specialBooks.emit(com.example.smartlibrary1.util.Resource.Success(specialBookList))

            } catch (e: Exception) {
                // Handle errors
                e.printStackTrace()
            }
        }
    }

    fun fetchBestBook() {

        viewModelScope.launch {
            _bestBooks.emit(com.example.smartlibrary1.util.Resource.Loading())
            try {
                // Fetch HTML content directly using Jsoup
                val document = withContext(Dispatchers.IO) {
                    Jsoup.connect("https://www.fahasa.com/sach-trong-nuoc.html?order=num_orders&limit=24&p=1").get()
                }

                // Parse HTML content
                val elements = document.select(".product-image") // Adjust selector
                val specialBookList = mutableListOf<Book>()
                var i = 0
                for (element in elements) {
                    // Process each element as needed
                    i++
                    val title = element.getElementsByTag("a").attr("title")
                    val image = element.getElementsByTag("img").attr("data-src")
                    val url = element.getElementsByTag("a").attr("href")
                    val author = element.getElementsByTag("a").attr("author")
                    val genre = element.getElementsByTag("a").attr("genre")
                    val description = element.getElementsByTag("a").attr("description")
                    if (i%2==0) {

                        continue
                    }
                    val book = Book(title,image,url,author,genre,description)
                    specialBookList += book
                    println(i)
                    // Update UI or perform further processing

                }


                _bestBooks.emit(com.example.smartlibrary1.util.Resource.Success(specialBookList))

            } catch (e: Exception) {
                // Handle errors
                e.printStackTrace()
            }
        }
    }

}

