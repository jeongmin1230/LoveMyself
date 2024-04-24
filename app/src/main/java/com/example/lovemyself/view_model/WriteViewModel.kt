package com.example.lovemyself.view_model

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.lovemyself.R
import com.example.lovemyself.etc.User
import com.google.firebase.database.FirebaseDatabase

class WriteViewModel: ViewModel() {
    val praise = mutableStateOf("")
    var date = ""

    fun write(context: Context, onDone: () -> Unit) {
        FirebaseDatabase.getInstance().getReference("${User.uid}/praise/$date").setValue(praise.value).addOnCompleteListener {
            if(it.isSuccessful) {
                Toast.makeText(context, context.getString(R.string.done), Toast.LENGTH_SHORT).show()
                praise.value = ""
                onDone()
            } else Toast.makeText(context, context.getString(R.string.fail), Toast.LENGTH_SHORT).show()
        }
    }
}