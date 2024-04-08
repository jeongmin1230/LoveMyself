package com.example.lovemyself.view_model

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.lovemyself.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterViewModel: ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    val name = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val confirmPassword = mutableStateOf("")

    fun register(context: Context, navController: NavHostController) {
        val database = FirebaseDatabase.getInstance().getReference(auth.currentUser?.uid ?: "")
        auth.createUserWithEmailAndPassword(email.value, password.value).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                database.apply {
                    child("uid").setValue(auth.currentUser?.uid)
                    child("name").setValue(name.value)
                    child("email").setValue(email.value)
                    child("password").setValue(password.value)
                }
                Toast.makeText(context, context.getString(R.string.register_complete), Toast.LENGTH_SHORT).show()
                navController.navigate(context.getString(R.string.register_complete))
            }
        }
    }
}