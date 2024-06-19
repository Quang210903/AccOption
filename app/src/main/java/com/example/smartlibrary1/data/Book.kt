package com.example.smartlibrary1.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book (
//    val id: String,
    val title: String,
    val image: String,
    val url: String,
    val description: String,
    val author: String,
    val genre: String
):Parcelable{}