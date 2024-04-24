package com.example.lovemyself.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.lovemyself.R
import com.example.lovemyself.etc.Appbar
import com.example.lovemyself.etc.EnterForm
import com.example.lovemyself.ui.theme.BasicBlack
import com.example.lovemyself.view_model.WriteViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WriteScreen(onClickBack: () -> Unit) {
    val context = LocalContext.current
    val writeViewModel = WriteViewModel()
    writeViewModel.date = LocalDate.now().toString()
    Column(Modifier.background(Color.White)) {
        Appbar(screenName = stringArrayResource(id = R.array.menu_item)[1]) { onClickBack() }
        Column(Modifier.padding(all = 8.dp)) {
            EnterForm(
                what = "",
                tf = writeViewModel.praise,
                visualTransformation = VisualTransformation.None,
                keyboardType = KeyboardType.Text
            )
            Button(
                onClick = { if(writeViewModel.praise.value.trim().isNotEmpty()) writeViewModel.write(context){ onClickBack() } },
                modifier = Modifier.height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFCDD2).copy(0.4f),
                    disabledContainerColor = Color(0xFFFFCDD2)
                )
            ) {
                Text(
                    text = stringResource(id = R.string.write),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = BasicBlack)
                )
            }
        }

    }
}