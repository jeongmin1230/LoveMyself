package com.example.lovemyself.etc

import android.annotation.SuppressLint
import android.content.Context

fun getStoredUserEmail(context: Context): String {
    val sharedPreferences = context.getSharedPreferences("UserCredentials", Context.MODE_PRIVATE)
    return sharedPreferences.getString("email", "") ?: ""
}

fun getStoredUserPassword(context: Context): String {
    val sharedPreferences = context.getSharedPreferences("UserCredentials", Context.MODE_PRIVATE)
    return sharedPreferences.getString("password", "") ?: ""
}

@SuppressLint("CommitPrefEdits")
fun userCredentials(context: Context, isSave: Boolean, name: String, email: String, password: String) {
    val sharedPreferences = context.getSharedPreferences("UserCredentials", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    if(isSave) {
        editor.apply {
            putString("name", name)
            putString("email", email)
            putString("password", password)
        }
    } else {
        editor.apply {
            remove("name")
            remove("email")
            remove("password")
        }
    }
}
