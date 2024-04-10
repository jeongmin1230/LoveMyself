package com.example.lovemyself.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.lovemyself.Appbar
import com.example.lovemyself.R
import com.example.lovemyself.ui.theme.BasicBlack
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(navController: NavHostController) {
    Column(modifier = Modifier
        .background(Color.White)
        .fillMaxSize()) {
        Appbar(screenName = stringResource(id = R.string.main_screen)) { navController.popBackStack() }
        ShowWeek(LocalDate.now())
        Spacer(modifier = Modifier.height(10.dp))
        TodayScreen(LocalDate.now())
    }
}

@Composable
fun TodayScreen(today: LocalDate) {
    val dateFormat = today.toString().substring(5, 10).split("-")
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Color.LightGray))
        ) {
        Text(
            text = stringResource(id = R.string.today_mention).format(dateFormat[0], dateFormat[1]),
            style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center, color = BasicBlack),
            modifier = Modifier.padding(top = 10.dp)
        )
        Text(
            text = stringResource(id = R.string.please_praise_for_me),
            style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center, color = BasicBlack, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 10.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowWeek(today: LocalDate) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        ShowDayOfWeek(today)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowDayOfWeek(today: LocalDate) {
    val text = listOf("일", "월", "화", "수", "목", "금", "토")
    Row(verticalAlignment = Alignment.CenterVertically) {
        text.forEach {
            Text(
                text = it,
                style = MaterialTheme.typography.labelMedium.copy(color = if(it == "일") Color.Red else if(it == "토") Color.Blue else BasicBlack, textAlign = TextAlign.Center),
                modifier = Modifier.weight(1f)
            )
        }
        // 월. 일 도 요일 밑에 나와야 함
    }
}