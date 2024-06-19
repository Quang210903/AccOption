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
import org.jsoup.nodes.Element

class BookDetailViewModel(val book: Book) : ViewModel() {

    private val _bookDetail = MutableStateFlow<Resource<List<BookDetail>>>(Resource.Unspecified())
    val bookDetail: MutableStateFlow<Resource<List<BookDetail>>> = _bookDetail
    val bookDetailList = mutableListOf<BookDetail>()

    init {
        fetchBookDetail()
    }

    fun fetchBookDetail() {
        println("Da duoc goi")
        viewModelScope.launch {
            println("Da duoc goi 1")
            _bookDetail.emit(com.example.smartlibrary1.util.Resource.Loading())
            try {
                println(book.url)
                val description1 = mutableListOf<String>()
                // Fetch HTML content directly using Jsoup
                val document = withContext(Dispatchers.IO) {
                    Jsoup.connect(book.url).get()
                }
                println("Da duoc goi 2")

                val dataAuthorElement: Element? = document.selectFirst(".data_author")
                val author = dataAuthorElement?.text() ?: "Unknown Author"

                val dataTitleElement: Element? = document.selectFirst(".product-essential-detail")
                val title = dataTitleElement?.getElementsByTag("h1")?.text() ?: "Unknown Title"

                val dataGenreElement: Element? = document.selectFirst(".2")
                val genre = dataGenreElement?.getElementsByTag("a")?.text() ?: "Unknown Genre"

                val elements = document.select(".std")
                for (element in elements) {
                    val description = element.getElementsByTag("p").text()
                    description1.add(description)
                }

                val bookDetail = BookDetail(title, "", description1.getOrNull(0) ?: "", author, genre)
                bookDetailList.add(bookDetail)

                _bookDetail.emit(Resource.Success(bookDetailList))

            } catch (e: Exception) {
                // Handle errors
                e.printStackTrace()
                _bookDetail.emit(Resource.Error("Failed to fetch book details"))
            }
        }
    }

    fun getBookDetail(): BookDetail {
        return bookDetailList[0]
    }
}
