package com.example.lovemyself.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.lovemyself.R
import com.example.lovemyself.etc.Appbar
import com.example.lovemyself.ui.theme.BasicBlack

@Composable
fun SettingsScreen(onClickBack: () -> Unit) {
    val lock = remember { mutableStateOf(false) }
    val pin = remember {  mutableStateOf("") }
    val alarm = remember { mutableStateOf(false) }
    Column(modifier = Modifier
        .background(Color.White)
        .fillMaxSize()) {
        Appbar(stringArrayResource(id = R.array.menu_item)[3]) { onClickBack() }
        Column {
            SettingRow(text = stringResource(id = R.string.screen_lock), isChecked = lock) {
                EnterPin(pin = pin)
            }
            SettingRow(text = stringResource(id = R.string.alarm), isChecked = alarm) {
                Text(text = "시간 선택 하세용")
            }
        }
    }
}

@Composable
fun SettingRow(text: String, isChecked: MutableState<Boolean>, showIfCheck: @Composable () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 10.dp)){
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(BasicBlack),
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = isChecked.value,
                onCheckedChange = { isChecked.value = it },
                colors = SwitchDefaults.colors(
                    checkedIconColor = BasicBlack,
                    checkedThumbColor = BasicBlack,
                    checkedBorderColor = Color.DarkGray,
                    checkedTrackColor = Color.LightGray,
                    uncheckedThumbColor = Color.LightGray,
                    uncheckedTrackColor = Color.White
                ),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
        if(isChecked.value) showIfCheck()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterPin(pin: MutableState<String>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
    ) {
        Text(
            text = stringResource(id = R.string.enter_pin),
            style = MaterialTheme.typography.labelLarge.copy(BasicBlack)
        )
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = pin.value,
            onValueChange = { pin.value = it },
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            visualTransformation = VisualTransformation.None,
            textStyle = MaterialTheme.typography.labelMedium.copy(textAlign = TextAlign.Center, color = BasicBlack),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Black.copy(0.2f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
        )
    }
}