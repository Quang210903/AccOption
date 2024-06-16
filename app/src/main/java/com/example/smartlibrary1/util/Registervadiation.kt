package com.example.smartlibrary1.util

sealed class RegisterVadiation {
    object Success : RegisterVadiation()
    data class Failure(val message: String) : RegisterVadiation()

}
data class RegisterFieldState (
    val email: RegisterVadiation,
    val password: RegisterVadiation
    )
