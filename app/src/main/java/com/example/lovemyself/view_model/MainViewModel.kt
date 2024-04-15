package com.example.lovemyself.view_model

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.lovemyself.R
import com.example.lovemyself.etc.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainViewModel: ViewModel() {
    val date = mutableStateOf("")
    val write = mutableStateOf(false)
    val praise = mutableStateOf("")

    fun write() {
        FirebaseDatabase.getInstance().getReference("${User.uid}/praise/${date.value}").setValue(praise.value)
    }

    fun checkAlreadyWrite(): MutableState<Boolean> {
        val result = mutableStateOf(false)
        FirebaseDatabase.getInstance().getReference("${User.uid}/praise/${date.value}").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                result.value = snapshot.exists()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        return result
    }
}