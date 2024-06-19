package com.example.smartlibrary1.data

import android.os.Message
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.Month

@Parcelize
data class Plan(
    val idPlanBook: Int,
    val title: String,
    val message: String,
    val day: Int,
    val month: Int,
    val year : Int,
    val hour: Int,
    val minute: Int


): Parcelable {}
