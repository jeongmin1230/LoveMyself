package com.example.lovemyself.screen

import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
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
                            if(pinText.value.isNotEmpty()) settingViewModel.updateValue(false, lockValue.value)
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
                Column {
                    EnterAlarm(alarmText, context, onClickBack = {
                            settingViewModel.updateValue(isAlarm = true, isOn = false)
                            navController.popBackStack()},
                        updateTime = {
                            settingViewModel.updateText(true, alarmText.value)
                            settingViewModel.updateValue(isAlarm = true, isOn = true)
                        }
                    )
                }
            }
            composable(navRoute[2]) {
                pinText.value = ""
                Column {
                    Appbar(stringResource(id = R.string.screen_lock)) {
                        settingViewModel.updateValue(isAlarm = false, isOn = false)
                        navController.popBackStack() }
                    EnterPin(pinText, { settingViewModel.updateText(false, pinText.value) }){
                        settingViewModel.updateValue(isAlarm = false, isOn = true)
                        navController.popBackStack()
                    }
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
fun SelectTime(selectedTime: MutableState<String>, list: List<String>, modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .border(BorderStroke(1.dp, Color.LightGray))
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .width(30.dp)
                .height(48.dp)
        ) {
            list.forEach {
                item {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge.copy(if(selectedTime.value==it) Color.Black else Color.LightGray),
                        modifier = Modifier.clickable { selectedTime.value = it }
                    )
                }
            }
        }
        Text(text = selectedTime.value)
    }
}

@Composable
fun EnterAlarm(alarmText: MutableState<String>, context: Context, onClickBack: () -> Unit, updateTime: () -> Unit) {
    val time = remember { mutableStateOf("") }
    val timeList = listOf("AM", "PM")
    val hour = remember { mutableStateOf("") }
    val hourList = (1..12).toList().map { it.toString() }
    val minute = remember { mutableStateOf("") }
    val minuteList = (0..59).toList().map { it.toString() }
    val confirm = remember { mutableStateOf(false) }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(if (confirm.value) Color.Black.copy(0.12f) else Color.White)) {
        Appbar(stringResource(id = R.string.alarm)) { onClickBack() }
        Text(
            text = stringResource(id = R.string.please_click),
            style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center, fontWeight = FontWeight.W800, color = BasicBlack),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            SelectTime(selectedTime = time, list = timeList, modifier = Modifier.weight(1f))
            SelectTime(selectedTime = hour, list = hourList, modifier = Modifier.weight(1f))
            SelectTime(selectedTime = minute, list = minuteList, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(id = R.string.setting),
            style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center, color = BasicBlack),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { if(time.value.isNotEmpty() && hour.value.isNotEmpty() && minute.value.isNotEmpty()) confirm.value = true }
        )
    }

    if(confirm.value) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { confirm.value = false },
            confirmButton = {
                Text(
                    text = stringResource(id = R.string.yes),
                    style = MaterialTheme.typography.labelMedium.copy(Color.Blue),
                    modifier = Modifier.clickable {
                        confirm.value = false
                        alarmText.value = time.value + " " + hour.value + ":" + minute.value
                        updateTime()
                        Toast.makeText(context, context.getString(R.string.success_to_setting_alarm_time).format(time.value, hour.value, minute.value), Toast.LENGTH_SHORT).show()
                    })
                            },
            dismissButton = {
                Text(
                    text = stringResource(id = R.string.no),
                    style = MaterialTheme.typography.labelMedium.copy(Color.Red),
                    modifier = Modifier.clickable { confirm.value = false }) },
            text = {
                Text(
                    text = stringResource(id = R.string.confirm_alarm_time).format(time.value, hour.value, minute.value),
                    style = MaterialTheme.typography.bodyLarge.copy(BasicBlack)
                )
            },
            modifier = Modifier.padding(horizontal = 16.dp),
            properties = DialogProperties(dismissOnClickOutside = false, usePlatformDefaultWidth = false)
        )
    }
}

@Composable
fun EnterPin(pin: MutableState<String>, updatePin: () -> Unit, updateLock: () -> Unit) {
    val numList = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "")
    Column {
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