package com.example.lovemyself.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.lovemyself.etc.PinKeypad
import com.example.lovemyself.etc.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun Pin(isLock: MutableState<Boolean>) {
    val pin = remember { mutableStateOf("") }
    PinKeypad(
        context = LocalContext.current,
        pin = pin,
        firstScreen = true) {
        isLock.value = false
    }
}

fun lockState(isLock: MutableState<Boolean>, pin: MutableState<String>) {
    FirebaseDatabase.getInstance().getReference("${User.uid}/checkState/lock").addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if(snapshot.child("value").getValue(Boolean::class.java) == true) {
                isLock.value = true
                pin.value = snapshot.child("text").getValue(String::class.java) ?: ""
                User.screenPin = pin.value
            } else isLock.value = false

        }

        override fun onCancelled(error: DatabaseError) {
            println("error : ${error.message}")
        }
    })
}