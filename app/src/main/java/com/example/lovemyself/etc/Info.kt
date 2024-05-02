package com.example.lovemyself.etc

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.navigation.NavHostController
import com.example.lovemyself.R
import com.example.lovemyself.screen.MainActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

fun getStoredUserEmail(context: Context): String {
    val sharedPreferences = context.getSharedPreferences("UserCredentials", Context.MODE_PRIVATE)
    return sharedPreferences.getString("email", "") ?: ""
}

fun getStoredUserPassword(context: Context): String {
    val sharedPreferences = context.getSharedPreferences("UserCredentials", Context.MODE_PRIVATE)
    return sharedPreferences.getString("password", "") ?: ""
}

@SuppressLint("CommitPrefEdits")
fun autoLogin(context: Context, isSave: Boolean, name: String, email: String, password: String) {
    val sharedPreferences = context.getSharedPreferences("UserCredentials", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    if(isSave) {
        editor.apply {
            putString("name", name)
            putString("email", email)
            putString("password", password)
        }.apply()
    } else {
        editor.apply {
            remove("name")
            remove("email")
            remove("password")
        }.apply()
    }
}

fun getUserData(context: Context, uid: String, email: String, password: String) {
    User.uid = uid
    FirebaseDatabase.getInstance().getReference(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("name").getValue(String::class.java) ?: ""
                User.name = name
                autoLogin(context, true, name, email, password)
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        }
    )
}
