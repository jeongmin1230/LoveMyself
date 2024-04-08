package com.example.lovemyself.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.lovemyself.R
import com.example.lovemyself.ui.theme.BasicBlack
import com.example.lovemyself.view_model.RegisterViewModel


@Composable
fun RegisterScreen(navController: NavHostController) {
    val registerViewModel = RegisterViewModel()
    val context = LocalContext.current
    val condition = registerViewModel.name.value.trim().isNotEmpty() && registerViewModel.password.value.trim().isNotEmpty() && registerViewModel.confirmPassword.value.trim().isNotEmpty()
    Column(modifier = Modifier
        .padding(all = 12.dp)
        .fillMaxSize()) {
        EnterForm(what = stringResource(id = R.string.name), tf = registerViewModel.name, visualTransformation = VisualTransformation.None)
        EnterForm(what = stringResource(id = R.string.email), tf = registerViewModel.email, visualTransformation = VisualTransformation.None)
        EnterForm(what = stringResource(id = R.string.password), tf = registerViewModel.password, visualTransformation = PasswordVisualTransformation('*'))
        EnterForm(what = stringResource(id = R.string.confirm_password), tf = registerViewModel.confirmPassword, visualTransformation = PasswordVisualTransformation('*'))

/*        Button(
            enabled = condition,
            onClick = { registerViewModel.register(context, navController) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFCDD2), disabledContainerColor = Color.LightGray),
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()) {
            Text(
                text = stringResource(id = R.string.register),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = if(!condition) Color.White else BasicBlack)
            )
        }*/
    }
}