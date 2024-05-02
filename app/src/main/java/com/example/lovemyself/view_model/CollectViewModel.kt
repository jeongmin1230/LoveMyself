package com.example.lovemyself.view_model

import androidx.lifecycle.ViewModel
import com.example.lovemyself.etc.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CollectViewModel: ViewModel() {
    val praiseList = mutableMapOf<String, String>()

    fun loadPraise(dayList: MutableList<String>, response: (Map<String, String>) -> Unit) {
        FirebaseDatabase.getInstance().getReference("${User.uid}/praise").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in 0 .. 6) {
                    val dayPraise = snapshot.child(dayList[i])
                    praiseList.computeIfAbsent(dayPraise.key.toString()) { dayPraise.value.toString() }
                    response(praiseList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}