package com.example.lovemyself.view_model

import android.content.Context
import android.net.Credentials
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.lovemyself.R
import com.example.lovemyself.etc.User
import com.example.lovemyself.etc.autoLogin
import com.example.lovemyself.etc.getStoredUserEmail
import com.example.lovemyself.etc.getUserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.values

class LoginViewModel: ViewModel() {
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    companion object {
        const val RC_SIGN_IN = 100
    }
    private val auth = FirebaseAuth.getInstance()

    fun login(context: Context, navController: NavHostController) {
        auth.signInWithEmailAndPassword(email.value.trim(), password.value.trim())
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    User.email = email.value.trim()
                    User.password = password.value.trim()
                    getUserData(context, auth.uid ?: "", email.value, password.value, navController)
                } else {
                    Toast.makeText(context, context.getString(R.string.login_fail), Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun google() {
    }
}