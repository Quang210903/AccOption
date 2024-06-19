package com.example.smartlibrary1.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartlibrary1.data.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup


class SearchViewModel : ViewModel() {
    companion object {
        val searchBookList = mutableListOf<Book>()
    }
    private val _specialBooks = MutableStateFlow<com.example.smartlibrary1.util.Resource<List<Book>>>(com.example.smartlibrary1.util.Resource.Unspecified())
    val specialBook : MutableStateFlow<com.example.smartlibrary1.util.Resource<List<Book>>> = _specialBooks


    init{
        fetchSpecialBook("",1)


    }
    fun fetchSpecialBook(bookURL:String,number : Int) {

        viewModelScope.launch {
            _specialBooks.emit(com.example.smartlibrary1.util.Resource.Loading())
            try {
                val document = withContext(Dispatchers.IO) {
                    Jsoup.connect(bookURL).get()
                }

                // Parse HTML content
                val elements = document.select(".product-wrapper") // Adjust selector

                var i = 0
                for (element in elements) {
                    // Process each element as needed
                    i++

                    val image = element.getElementsByTag("img").attr("data-wood-src")
                    val a = element.getElementsByTag("a").text()
                    val title = a.substringAfter('%')
                    val author = ""
                    val url = element.getElementsByTag("a").attr("href")
                    val genre = ""
                    val description = ""
                    val book = Book(title,image,url,author,genre,description)
                    searchBookList += book
                    println(title)
                    println(url)
                    if ( number == 1) break
                }


                _specialBooks.emit(com.example.smartlibrary1.util.Resource.Success(searchBookList))

            } catch (e: Exception) {
                // Handle errors
                e.printStackTrace()
            }
        }
    }




}