package com.example.lovemyself.etc

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.lovemyself.R
import com.example.lovemyself.screen.PinState
import com.example.lovemyself.ui.theme.BasicBlack

@Composable
fun Appbar(screenName: String, onBackClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(all = 10.dp)
            .fillMaxWidth()
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_back),
            contentDescription = stringResource(id = R.string.back_description),
            modifier = Modifier
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) { onBackClick() }
                .padding(end = 8.dp)
            )
        Text(
            text = screenName,
            style = MaterialTheme.typography.labelLarge.copy(BasicBlack)
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterForm(what: String, tf: MutableState<String>, visualTransformation: VisualTransformation, keyboardType: KeyboardType) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(bottom = 12.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = what,
            style = MaterialTheme.typography.labelLarge.copy(BasicBlack),
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = tf.value,
            onValueChange = { tf.value = it },
            shape = CircleShape,
            visualTransformation = visualTransformation,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Black.copy(0.2f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            textStyle = MaterialTheme.typography.labelMedium.copy(BasicBlack),
            singleLine = true
        )
    }
}

@Composable
fun PinKeypad(context: Context, pin: MutableState<String>, firstScreen: Boolean, action: () -> Unit) {
    val numList = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "←")
    Column(modifier = Modifier
        .background(Color.White)
        .fillMaxSize()) {
        Text(
            text = if(firstScreen) stringResource(id = R.string.enter_current_pin) else stringResource(id = R.string.enter_pin),
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
                            .weight(1f)
                            .clickable {
                                if(numList[it] == "←") pin.value = pin.value.dropLast(1)
                                else pin.value += numList[it] }
                    )
                }
            },
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.weight(0.6f)
        )
    }
    if(pin.value.isNotEmpty()) {
        when(firstScreen) {
            true -> {
                if(User.screenPin == pin.value) action()
                else if(User.screenPin != pin.value && pin.value.length == 4) {
                    Toast.makeText(context, context.getString(R.string.incorrect_pin), Toast.LENGTH_SHORT).show()
                    pin.value = ""
                }
            }
            false -> action()
        }
    }
}

@Composable
fun SettingDialog(content: String, confirmAction: () -> Unit, dismissAction: () -> Unit) {
    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = dismissAction,
        confirmButton = {
            Text(
                text = stringResource(id = R.string.yes),
                style = MaterialTheme.typography.labelMedium.copy(BasicBlack),
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable { confirmAction() })
        },
        dismissButton = {
            Text(
                text = stringResource(id = R.string.no),
                style = MaterialTheme.typography.labelMedium.copy(Color.Red),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .clickable { dismissAction() })
        },
        text = {
            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge.copy(BasicBlack))
        },
        modifier = Modifier.padding(horizontal = 16.dp),
        properties = DialogProperties(dismissOnClickOutside = false, usePlatformDefaultWidth = false)
    )
}