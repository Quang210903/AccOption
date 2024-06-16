package com.example.smartlibrary1.util

import android.util.Patterns

fun validateEmail(email: String) : RegisterVadiation{
    if (email.isEmpty())
        return RegisterVadiation. Failure ("Email cannot be empty")

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterVadiation. Failure ("Wrong email format")

    return RegisterVadiation.Success
}
fun validatePassword(password: String) : RegisterVadiation{
    if (password.isEmpty())
        return RegisterVadiation. Failure ("Password cannot be empty")

    if (password.length < 6)
        return RegisterVadiation. Failure ("Password should contains 6 char")

    return RegisterVadiation. Success
}

