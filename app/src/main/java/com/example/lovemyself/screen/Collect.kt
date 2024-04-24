package com.example.lovemyself.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import com.example.lovemyself.R
import com.example.lovemyself.etc.Appbar
import com.example.lovemyself.ui.theme.BasicBlack
import com.example.lovemyself.view_model.CollectViewModel

@Composable
fun CollectScreen(dayList: MutableList<String>, onClickBack: () -> Unit) {
    val collectViewModel = CollectViewModel()
    val praiseMapState = remember { mutableStateOf(emptyMap<String, String>()) }
    collectViewModel.loadPraise(dayList) { praiseMapState.value = it }
    Column(Modifier.background(Color.White)) {
        Appbar(screenName = stringArrayResource(id = R.array.menu_item)[2]) { onClickBack() }
        praiseMapState.value.forEach { (day, praise) ->
            ShowPraise(day.substring(5, 10).replace("-", "/"), if(praise == "null") "" else praise)
        }
    }
}

@Composable
fun ShowPraise(day: String, praise: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(all = 8.dp)) {
        Text(
            text = day,
            style = MaterialTheme.typography.bodySmall.copy(Color.DarkGray)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = praise,
            style = MaterialTheme.typography.bodyMedium.copy(BasicBlack),
            modifier = Modifier.weight(1f)
        )
    }
    Divider()
}