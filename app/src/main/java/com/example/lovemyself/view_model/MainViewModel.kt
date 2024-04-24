package com.example.lovemyself.view_model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.lovemyself.etc.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainViewModel: ViewModel() {
    var weekResult = mutableStateOf(listOf<Boolean>())

    val date = mutableStateOf("")

    fun checkWeek(dayList: MutableList<String>){
        weekResult.value = emptyList()
        FirebaseDatabase.getInstance().getReference("${User.uid}/praise").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in 0 until 7) {
                    if (snapshot.child(dayList[i]).exists()) {
                        weekResult.value += true
                    } else {
                        weekResult.value += false
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("----error----")
                error.apply {
                    println(message)
                    println(code)
                    println(details)
                }
            }
        })
    }
}