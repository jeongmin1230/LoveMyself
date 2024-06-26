package com.example.lovemyself.view_model

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.lovemyself.R
import com.example.lovemyself.etc.AlarmReceiver
import com.example.lovemyself.etc.User
import com.example.lovemyself.etc.autoLogin
import com.example.lovemyself.screen.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class SettingViewModel: ViewModel() {
    private val firebase = FirebaseDatabase.getInstance().getReference("${User.uid}/checkState")
    private val alarmRef = firebase.child("alarm")
    private val lockRef = firebase.child("lock")

    private var alarmValue = mutableStateOf(false)
    private var alarmTime = ""

    private var lockValue = mutableStateOf(false)
    private var pin = ""


    fun alarmText(): String {
        alarmRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                alarmTime = snapshot.child("text").getValue(String::class.java) ?: ""
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return alarmTime
    }
    fun alarmValue(): MutableState<Boolean> {
        alarmRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                alarmValue.value = snapshot.child("value").getValue(Boolean::class.java) ?: false
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return alarmValue
    }

    fun lockText(): String {
        lockRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pin = snapshot.child("text").getValue(String::class.java) ?: ""
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        return pin
    }

    fun lockValue(): MutableState<Boolean> {
        lockRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lockValue.value = snapshot.child("value").getValue(Boolean::class.java) ?: false
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return lockValue
    }

    fun updateText(isAlarm: Boolean, text: String) {
        if(isAlarm) alarmTime = text else pin = text
    }

    fun updateValue(isAlarm: Boolean, isOn: Boolean) {
        if(isAlarm) {
            alarmRef.child("text").setValue(alarmTime)
            alarmRef.child("value").setValue(isOn)
        } else {
            lockRef.child("text").setValue(pin)
            lockRef.child("value").setValue(isOn)
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    fun setAlarm(context: Context, text: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val hour = text.split(" ")[1].split(":")[0].toInt()
        val minute = text.split(" ")[1].split(":")[1].toInt()
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.AM_PM, if (text.split(" ")[0] == "AM") Calendar.AM else Calendar.PM)
        }
        if(calendar.before(Calendar.getInstance())) calendar.add(Calendar.DAY_OF_MONTH, 1)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }


    fun logout(context: Context) {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
        autoLogin(context, false, "", "", "")
        User.deleteInfo()
        Toast.makeText(context, context.getString(R.string.do_logout), Toast.LENGTH_SHORT).show()
    }
}