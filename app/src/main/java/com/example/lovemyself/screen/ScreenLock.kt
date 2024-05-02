package com.example.lovemyself.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.lovemyself.R
import com.example.lovemyself.etc.User
import com.example.lovemyself.ui.theme.BasicBlack
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun ScreenLockScreen(isLock: MutableState<Boolean>, pin: MutableState<String>, mainNav: NavHostController) {
    lockState(isLock, pin)
    println("lockState : ${isLock.value}")
    println("pin : ${pin.value}")
    User.screenPin = pin.value
    println("User.screenPin : ${User.screenPin}")
    if(isLock.value) Pin(mainNav)
}


@Composable
fun Pin(mainNav: NavHostController) {
    val context = LocalContext.current
    val pin = remember { mutableStateOf("") }
    val numList = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "")
    Column(modifier = Modifier
        .background(Color.White)
        .fillMaxSize()) {
        Text(
            text = stringResource(id = R.string.enter_current_pin),
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
    if(User.screenPin == pin.value) mainNav.navigate("홈")
    else if(User.screenPin != pin.value && pin.value.length == 4) {
        Toast.makeText(context, context.getString(R.string.incorrect_pin), Toast.LENGTH_SHORT).show()
        pin.value = ""
    }
}

fun lockState(isLock: MutableState<Boolean>, pin: MutableState<String>) {
    FirebaseDatabase.getInstance().getReference("${User.uid}/checkState/lock").addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if(snapshot.child("value").getValue(Boolean::class.java) == true) {
                isLock.value = true
                pin.value = snapshot.child("text").getValue(String::class.java) ?: ""
                println("snapshot.child(\"text\").getValue(String::class.java) ?: \"\" : ${snapshot.child("text").getValue(String::class.java) ?: ""}")
            } else println("no lock")

        }

        override fun onCancelled(error: DatabaseError) {
            println("error : ${error.message}")
        }
    })
    println("isLock ${isLock.value}")
}