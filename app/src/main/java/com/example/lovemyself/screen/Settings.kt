package com.example.lovemyself.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lovemyself.R
import com.example.lovemyself.etc.Appbar
import com.example.lovemyself.ui.theme.BasicBlack
import com.example.lovemyself.view_model.SettingViewModel

@Composable
fun SettingsScreen(backToMain: () -> Unit) {
    val settingViewModel = SettingViewModel()
    val context = LocalContext.current
    val navRoute = stringArrayResource(id = R.array.nav)
    val alarmText = remember { mutableStateOf(settingViewModel.alarmText())  }
    val alarmValue = remember { settingViewModel.alarmValue() }
    val pinText = remember { mutableStateOf(settingViewModel.lockText())  }
    val lockValue = remember { settingViewModel.lockValue() }
    val navController = rememberNavController()
    Column(modifier = Modifier
        .background(Color.White)
        .fillMaxSize()) {
        NavHost(navController, startDestination = navRoute[0]) {
            composable(navRoute[0]) {
                Column {
                    Appbar(stringResource(id = R.string.setting)) { backToMain() }
                    SettingRow(text = stringResource(id = R.string.alarm), checked = alarmValue,
                        turnOn = {
                            settingViewModel.updateValue(true, alarmValue.value)
                            navController.navigate(navRoute[1]) },
                        turnOff = {
                            settingViewModel.updateValue(true, alarmValue.value)
                        })
                    SettingRow(text = stringResource(id = R.string.screen_lock), checked = lockValue,
                        turnOn = {
                            settingViewModel.updateValue(false, lockValue.value)
                            navController.navigate(navRoute[2]) },
                        turnOff = {
                            settingViewModel.updateValue(false, lockValue.value)
                        })
                    Text(
                        text = stringResource(id = R.string.logout),
                        style = MaterialTheme.typography.bodyMedium.copy(Color.Red),
                        modifier = Modifier
                            .clickable { settingViewModel.logout(context) }
                            .padding(horizontal = 10.dp))
                }
            }
            composable(navRoute[1]) {
                EnterAlarm(navController)
            }
            composable(navRoute[2]) {
                pinText.value = ""
                EnterPin(navController, pinText, { settingViewModel.updateText(false, pinText.value) }){
                    settingViewModel.updateValue(isAlarm = false, isOn = true)
                    /* TODO
                    navController.popBackStack() 하면 화면이 잠깐 나타 났다가 사라 져서 일단 뒤로 가기는 안 해 놓음 .. */
                }
            }
        }
    }
}

@Composable
fun SettingRow(text: String, checked: MutableState<Boolean>, turnOn: () -> Unit, turnOff:() -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                checked.value = !checked.value
                if (checked.value) turnOn()
                else turnOff()
            }
            .padding(horizontal = 10.dp),
        content = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(BasicBlack),
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = checked.value,
                onCheckedChange = {
                    checked.value = it
                    if (checked.value) turnOn()
                    else turnOff()
                },
                colors = SwitchDefaults.colors(
                    checkedIconColor = BasicBlack,
                    checkedThumbColor = BasicBlack,
                    checkedBorderColor = Color.DarkGray,
                    checkedTrackColor = Color.LightGray,
                    uncheckedThumbColor = Color.LightGray,
                    uncheckedTrackColor = Color.White
                ),
            )
        }
    )
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun EnterAlarm(navController: NavHostController) {
    Column {
        Appbar(stringResource(id = R.string.alarm)) { navController.popBackStack() }
    }
}

@Composable
fun EnterPin(navController: NavHostController, pin: MutableState<String>, updatePin: () -> Unit, updateLock: () -> Unit) {
    val numList = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "")
    Column {
        Appbar(stringResource(id = R.string.screen_lock)) { navController.popBackStack() }
        Text(
            text = stringResource(id = R.string.enter_pin),
            style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center, color = BasicBlack),
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth()
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(0.4f)
                .fillMaxWidth()
        ) {
            pin.value.forEach { _ ->
                PinState()
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            content = {
                items(numList.size) {
                    Text(
                        text = numList[it],
                        style = MaterialTheme.typography.displayMedium.copy(textAlign = TextAlign.Center, color = BasicBlack),
                        modifier = Modifier
                            .padding(vertical = 20.dp)
                            .clickable { pin.value += numList[it] }
                            .weight(1f)
                    )
                }
            },
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.weight(0.6f)
        )
    }
    if(pin.value.length == 4) {
        updatePin()
        updateLock()
    }
}

@Composable
fun PinState() {
    Image(
        imageVector = ImageVector.vectorResource(R.drawable.ic_circle),
        contentDescription = stringResource(id = R.string.pin_state_description)
    )
}