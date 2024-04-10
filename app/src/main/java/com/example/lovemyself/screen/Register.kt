package com.example.lovemyself.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.lovemyself.Appbar
import com.example.lovemyself.EnterForm
import com.example.lovemyself.R
import com.example.lovemyself.ui.theme.BasicBlack
import com.example.lovemyself.view_model.RegisterViewModel


@SuppressLint("SuspiciousIndentation")
@Composable
fun RegisterScreen(navController: NavHostController) {
    val context = LocalContext.current
    val registerViewModel = RegisterViewModel()
        Column(modifier = Modifier
            .padding(all = 12.dp)
            .fillMaxSize()) {
            Appbar(stringResource(id = R.string.register)) { navController.popBackStack() }
            EnterForm(
                what = stringResource(id = R.string.name),
                tf = registerViewModel.name,
                visualTransformation = VisualTransformation.None,
                keyboardType = KeyboardType.Text
            )
            EnterForm(
                what = stringResource(id = R.string.email),
                tf = registerViewModel.email,
                visualTransformation = VisualTransformation.None,
                keyboardType = KeyboardType.Email
            )
            EnterForm(
                what = stringResource(id = R.string.password),
                tf = registerViewModel.password,
                visualTransformation = PasswordVisualTransformation('*'),
                keyboardType = KeyboardType.Text
            )
            EnterForm(
                what = stringResource(id = R.string.confirm_password),
                tf = registerViewModel.confirmPassword,
                visualTransformation = PasswordVisualTransformation('*'),
                keyboardType = KeyboardType.Text
            )

        Button(
            onClick = {
                if(registerViewModel.name.value.isNotEmpty() && registerViewModel.email.value.isNotEmpty() && registerViewModel.password.value.isNotEmpty() && registerViewModel.confirmPassword.value.isNotEmpty()) registerViewModel.register(context, navController)
                else Toast.makeText(context, context.getString(R.string.warning), Toast.LENGTH_SHORT).show()
                      },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFCDD2)),
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()) {
            Text(
                text = stringResource(id = R.string.register),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = BasicBlack)
            )
        }
    }
}