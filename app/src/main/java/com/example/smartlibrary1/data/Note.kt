package com.example.smartlibrary1.data

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    val idbook: Int,
    val time_note: String,
    val date_note: String,
    val page: Int,
    val content: String
): Parcelable {}
