package com.example.smartlibrary1.viewmodel

import androidx.lifecycle.ViewModel
import com.example.smartlibrary1.data.User
import com.example.smartlibrary1.util.Constants.USER_COLLECTION
import com.example.smartlibrary1.util.RegisterFieldState
import com.example.smartlibrary1.util.RegisterVadiation
import com.example.smartlibrary1.util.Resource
import com.example.smartlibrary1.util.validateEmail
import com.example.smartlibrary1.util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewmodel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {
    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: Flow<Resource<User>> = _register


    private val _validation = Channel <RegisterFieldState>()
    val vadiation = _validation.receiveAsFlow()

    fun createAccountWiththeEmailAndPassword(user: User, password: String) {
        if (checkValidation(user, password)) {
            runBlocking {
                _register.value = Resource.Loading()
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        saveUserinfo(it.uid,user)
                    }
                }.addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())
                }
        }else {
            val registerFieldsState = RegisterFieldState(
                validateEmail(user.email), validatePassword(password)
            )
            runBlocking {
                _validation.send(registerFieldsState)
            }
        }
}

    private fun saveUserinfo(userUid: String, user: User) {
        db.collection(USER_COLLECTION)
            .document(userUid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }.addOnFailureListener{
                _register.value = Resource.Error(it.message.toString())
            }
    }

    private fun checkValidation(user: User, password: String): Boolean {
        val emailVadiation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val shouldRegister = emailVadiation is RegisterVadiation.Success &&
                passwordValidation is RegisterVadiation.Success
        return shouldRegister
    }
}
