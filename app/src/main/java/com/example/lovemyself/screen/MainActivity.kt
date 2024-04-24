package com.example.lovemyself.screen

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lovemyself.R
import com.example.lovemyself.etc.MyDrawer
import com.example.lovemyself.ui.theme.BasicBlack
import com.example.lovemyself.ui.theme.LoveMyselfTheme
import com.example.lovemyself.view_model.MainViewModel
import kotlinx.coroutines.launch
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
                    WholeScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WholeScreen() {
    val mainViewModel = MainViewModel()
    val weekResult = remember { mainViewModel.weekResult }
    val screenArray = stringArrayResource(id = R.array.menu_item)
    val mainNav = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    mainViewModel.date.value = LocalDate.now().toString()
    val dayList = mutableListOf<String>()
    for (i in 0 until 7) {
        dayList.add(LocalDate.now().minusDays(LocalDate.now().dayOfWeek.value.toLong()).plusDays(i.toLong()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
    }
    NavHost(navController = mainNav, screenArray[0]) {
        composable(screenArray[0]) {
            LaunchedEffect(weekResult) { mainViewModel.checkWeek(dayList) }
            ScreenLockScreen()
            MyDrawer(mainNav, drawerState, scope) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxHeight()
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_drawer),
                        contentDescription = stringResource(id = R.string.drawer_description),
                        modifier = Modifier
                            .padding(all = 10.dp)
                            .clickable { scope.launch { drawerState.open() } }
                    )
                    if(weekResult.value.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.fail_load_data),
                            style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center, color = BasicBlack),
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        MainScreen(weekResult, weekResult.value[LocalDate.now().dayOfWeek.value])
                    }
                }
            }
        }
        composable(screenArray[1]) {
            WriteScreen { mainNav.popBackStack() }
        }
        composable(screenArray[2]) {
            CollectScreen(dayList) { mainNav.popBackStack() }
        }
        composable(screenArray[3]) {
            SettingsScreen { mainNav.popBackStack() }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(weekState: MutableState<List<Boolean>>, doneToday: Boolean) {
    val dateFormat = LocalDate.now().toString().substring(5, 10).split("-")
    Column {
        ShowWeek(LocalDate.now(), weekState)
        Spacer(modifier = Modifier.height(10.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(all = 8.dp)
                .fillMaxWidth()
                .border(BorderStroke(1.dp, Color.LightGray))
        ) {
            val today = if(doneToday) stringResource(id = R.string.already_write) else stringResource(id = R.string.please_praise_for_me)
            Text(
                text = stringResource(id = R.string.today_mention).format(dateFormat[0], dateFormat[1]),
                style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center, color = BasicBlack),
                modifier = Modifier.padding(top = 10.dp)
            )
            Text(
                text = today,
                style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center, color = BasicBlack, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowWeek(today: LocalDate, state: MutableState<List<Boolean>>) {
    val text = listOf("일", "월", "화", "수", "목", "금", "토")
    val days = mutableListOf<String>()
    for(i in 0 until 7) {
        days.add(today.minusDays(today.dayOfWeek.value.toLong()).plusDays(i.toLong()).format(DateTimeFormatter.ofPattern("MM/dd")))
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if(state.value.size == 7) {
                text.forEachIndexed { index, dOW ->
                    DayOfWeekAndDays(dayOfWeek = dOW, day = days[index], did = state.value[index], modifier = Modifier.weight(1f))
                }
            }
        }
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
