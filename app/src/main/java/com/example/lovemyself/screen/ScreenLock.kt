package com.example.lovemyself.screen

import android.app.KeyguardManager
import android.content.Context.KEYGUARD_SERVICE
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun ScreenLockScreen() {
    val context = LocalContext.current
    val keyguardManager = context.getSystemService(KEYGUARD_SERVICE) as KeyguardManager
    Column(Modifier.background(Color.White)){
        println("is lock ? ${keyguardManager.isKeyguardLocked}")
    }
}