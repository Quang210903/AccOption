package com.example.smartlibrary1.data

data class User(
    val firstname: String,
    val lastname: String,
    val email: String,
    var imagePath: String= ""

){
    constructor(): this("","","","")
}
