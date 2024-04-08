package com.example.lovemyself.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.lovemyself.ui.theme.BasicBlack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterForm(what: String, tf: MutableState<String>, visualTransformation: VisualTransformation) {
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
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Black.copy(0.2f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                textColor = BasicBlack
            )
        )
    }
}