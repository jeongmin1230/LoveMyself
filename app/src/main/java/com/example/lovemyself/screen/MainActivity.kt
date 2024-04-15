package com.example.lovemyself.screen

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.lovemyself.etc.Appbar
import com.example.lovemyself.R
import com.example.lovemyself.etc.User
import com.example.lovemyself.ui.theme.BasicBlack
import com.example.lovemyself.ui.theme.LoveMyselfTheme
import com.example.lovemyself.view_model.MainViewModel
import com.google.firebase.database.FirebaseDatabase
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoveMyselfTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {
    val mainViewModel = MainViewModel()
    val context = LocalContext.current
    mainViewModel.date.value = LocalDate.now().toString()
    Column(modifier = Modifier
        .background(Color.White)
        .fillMaxSize()) {
        ShowWeek(LocalDate.now(), mainViewModel.checkAlreadyWrite().value)
        Spacer(modifier = Modifier.height(10.dp))
        TodayScreen(context, LocalDate.now(), mainViewModel.write, mainViewModel.praise, mainViewModel.checkAlreadyWrite()) { mainViewModel.write() }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowWeek(today: LocalDate, did: Boolean) {
    val text = listOf("일", "월", "화", "수", "목", "금", "토")
    val days = mutableListOf<String>()
    for(i in 0 until 7) {
        days.add(today.minusDays(today.dayOfWeek.value.toLong()).plusDays(i.toLong()).format(DateTimeFormatter.ofPattern("MM/dd")))
    }
    Spacer(modifier = Modifier.height(20.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            text.forEachIndexed { index, dOW ->
                DayOfWeekAndDays(dayOfWeek = dOW, day = days[index], did = false, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun TodayScreen(context: Context, today: LocalDate, write: MutableState<Boolean>, praise: MutableState<String>, check: MutableState<Boolean>, onClick: () -> Unit) {
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
            text = if(check.value) stringResource(id = R.string.already_write) else stringResource(id = R.string.please_praise_for_me),
            style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center, color = BasicBlack, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 10.dp)
        )
    }

    Column(modifier = Modifier
        .padding(horizontal = 8.dp)
        .background(Color.LightGray)
        .fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.go_write),
            style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center, color = BasicBlack),
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = MutableInteractionSource()
                ) { write.value = true }
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
    }
    Spacer(modifier = Modifier.height(20.dp))
    if(write.value && !check.value) {
        WriteScreen(praise, write) { onClick() }
    } else if(write.value && check.value) {
        Toast.makeText(context, context.getString(R.string.already_write), Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun DayOfWeekAndDays(dayOfWeek: String, day: String, did: Boolean, modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ){
        Text(
            text = dayOfWeek,
            style = MaterialTheme.typography.labelMedium.copy(color = if(dayOfWeek == "일") Color.Red else if(dayOfWeek == "토") Color.Blue else BasicBlack, textAlign = TextAlign.Center),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = day,
            style = MaterialTheme.typography.labelLarge.copy(BasicBlack)
        )
        Image(
            imageVector = if(did) ImageVector.vectorResource(R.drawable.ic_did) else ImageVector.vectorResource(R.drawable.ic_yet),
            contentDescription = stringResource(id = R.string.did_description)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteScreen(praise: MutableState<String>, write: MutableState<Boolean>, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TextField(
            value = praise.value,
            onValueChange = { praise.value = it },
            shape = CircleShape,
            visualTransformation = VisualTransformation.None,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(end = 8.dp)
                .height(48.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Black.copy(0.2f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            textStyle = MaterialTheme.typography.labelMedium.copy(BasicBlack),
            singleLine = true
        )
        Button(
            onClick = {
                onClick()
                write.value = false
            },
            enabled = praise.value.trim().isNotEmpty(),
            modifier = Modifier.height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFCDD2).copy(0.4f),
                disabledContainerColor = Color(0xFFFFCDD2))
        ) {
            Text(
                text = stringResource(id = R.string.write),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = BasicBlack)
            )
        }
    }
}
