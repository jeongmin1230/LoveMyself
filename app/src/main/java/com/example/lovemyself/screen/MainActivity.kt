package com.example.lovemyself.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.lovemyself.R

@Composable
fun MainScreen(navController: NavHostController) {
    Column(modifier = Modifier
        .background(Color.White)
        .fillMaxSize()) {
        Text(text = stringResource(id = R.string.main_screen))
    }
}