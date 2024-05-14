package com.example.lovemyself.etc

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.lovemyself.R
import com.example.lovemyself.screen.MainActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class AlarmReceiver: BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        val pendingIntent = PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE)
        createNotificationChannel(context)
        val builder = NotificationCompat.Builder(context, "channel_id")
            .setSmallIcon(androidx.core.R.drawable.notification_icon_background)
            .setContentTitle(context.getString(R.string.app_name_kor))
            .setContentText(context.getString(R.string.alarm_content))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return
            notify(123, builder.build())
        }
        getAlarmText(context)
    }

    private fun getAlarmText(context: Context): String {
        var time = ""
        FirebaseDatabase.getInstance().getReference("${User.uid}/checkState/alarm/text")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    time = snapshot.getValue(String::class.java) ?: ""
                    setNextAlarm(context, time)
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        return time
    }

    private fun createNotificationChannel(context: Context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.app_name_kor)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("channel_id", name, importance).apply { description = context.getString(R.string.alarm_content) }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setNextAlarm(context: Context, time: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val hour = time.split(" ")[1].split(":")[0].toInt()
        val minute = time.split(" ")[1].split(":")[1].toInt()
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.AM_PM, if (time.split(" ")[0] == "AM") Calendar.AM else Calendar.PM)
            add(Calendar.DAY_OF_MONTH, 1)
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}