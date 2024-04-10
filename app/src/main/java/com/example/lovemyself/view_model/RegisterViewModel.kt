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
    val name = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val confirmPassword = mutableStateOf("")

    fun register(context: Context, navController: NavHostController) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email.value, password.value).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                FirebaseDatabase.getInstance().getReference(auth.currentUser?.uid ?: "").apply {
                    child("uid").setValue(auth.currentUser?.uid)
                    child("name").setValue(name.value)
                    child("email").setValue(email.value)
                    child("password").setValue(password.value)
                }
                Toast.makeText(context, context.getString(R.string.register_complete), Toast.LENGTH_SHORT).show()
                navController.navigate(context.getString(R.string.nav_route_login))
            }
            else Toast.makeText(context, context.getString(R.string.register_fail), Toast.LENGTH_SHORT).show()
        }
    }
}