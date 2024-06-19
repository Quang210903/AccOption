package com.example.smartlibrary1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartlibrary1.data.Book
import com.example.smartlibrary1.data.BookDetail
import com.example.smartlibrary1.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class SearchBookDetailViewmodel (val book: Book) : ViewModel(){

    private val _bookDetail = MutableStateFlow<Resource<List<BookDetail>>>(Resource.Unspecified())
    val  bookDetail: MutableStateFlow<Resource<List<BookDetail>>> = _bookDetail
    val bookDetailList = mutableListOf<BookDetail>()





    init{
        fetchBookDetail()


    }
    fun fetchBookDetail() {
        println("Da duoc goi")
        viewModelScope.launch {
            println("Da duoc goi 1")
            println(book.url)
            _bookDetail.emit(com.example.smartlibrary1.util.Resource.Loading())
            try {
                println(book.url)
                val description1 = mutableListOf<String>()
                // Fetch HTML content directly using Jsoup
                val document = withContext(Dispatchers.IO) {
                    Jsoup.connect(book.url).get()
                }
                println("Da duoc goi 2")

                val author = ""
                val dataTitleElement = document.selectFirst(".summary-inner")
                val title = dataTitleElement?.getElementsByTag("h1")?.text() ?: "Unkown titile"
                val dataGenreElement = document.selectFirst(".posted_in")
                val genre = dataGenreElement?.getElementsByTag("a")?.text()?:"Unknown genre"
                val dataDescription = document.selectFirst(".wc-tab-inner")
                val description = dataDescription?.getElementsByTag("p")?.text()?: "Unknown description"


                println(title)
                println(genre)
                println(description)
                val bookDetail = BookDetail(title,"",description,author,genre)
                bookDetailList.add(bookDetail)

                _bookDetail.emit(Resource.Success(bookDetailList))

            } catch (e: Exception) {
                // Handle errors
                e.printStackTrace()
            }
        }
    }

    fun getBookDetail(): BookDetail {
        return bookDetailList[0]
    }



}