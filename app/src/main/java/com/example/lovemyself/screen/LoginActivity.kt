package com.example.lovemyself.screen

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lovemyself.R
import com.example.lovemyself.ui.theme.BasicBlack
import com.example.lovemyself.ui.theme.LoveMyselfTheme
import com.example.lovemyself.view_model.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential

class Login : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoveMyselfTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val navController = rememberNavController()
                    val context = LocalContext.current
                    NavHost(navController, startDestination = context.getString(R.string.nav_route_login)) {
                        composable(context.getString(R.string.nav_route_login)) {
                            LoginScreen(navController)
                        }
                        composable(context.getString(R.string.register)) {
                            RegisterScreen(navController)
                        }
                        composable(context.getString(R.string.nav_route_main)) {
                            MainScreen(navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    val loginViewModel = LoginViewModel()
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(all = 12.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_praise),
            contentDescription = stringResource(id = R.string.praise_description),
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        EnterForm(what = stringResource(id = R.string.email), tf = loginViewModel.email, visualTransformation = VisualTransformation.None)
        EnterForm(what = stringResource(id = R.string.password), tf = loginViewModel.password, visualTransformation = PasswordVisualTransformation('*'))
        Button(
            onClick = { loginViewModel.login(context, navController) },
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth(),
            enabled = email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFCDD2),
                disabledContainerColor = Color(0xFFFFCDD2))
        ) {
            Text(
                text = stringResource(id = R.string.do_login),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = BasicBlack)
            )
        }
        Text(
            text = stringResource(id = R.string.or),
            style = MaterialTheme.typography.labelMedium.copy(BasicBlack),
            modifier = Modifier.padding(vertical = 4.dp)
        )
        ChooseTypeBox(
            bg = Color(0xFFFCEDEE),
            painterResource = painterResource(id = R.drawable.ic_google),
            description = stringResource(id = R.string.google_description),
            text = stringResource(id = R.string.sing_in_up_google)
        ) {}
        ChooseTypeBox(
            bg = Color(0xFFFCEDEE),
            painterResource = null,
            description = stringResource(id = R.string.register),
            text = stringResource(id = R.string.register)
        ) { navController.navigate(context.getString(R.string.register)) }

        /* 이메일 로그인 (아이디 비밀 번호 입력 창)
        * 로그인 버튼
        * -------
        * 구글 회원 가입 / 로그인
        * 네이버 회원 가입 / 로그인
        * 더 작은 글씨로 -> 아이디 / 비밀 번호가 기억 나지 않아요 */
    }
}

@Composable
fun ChooseTypeBox(bg: Color, painterResource: Painter?, description: String, text: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(bg, shape = CircleShape)
            .clickable { onClick() }
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        if (painterResource != null) {
            Image(
                painter = painterResource,
                contentDescription = description,
                modifier = Modifier
                    .padding(end = 20.dp)
                    .size(20.dp)
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(BasicBlack),
        )
    }
    Spacer(modifier = Modifier.height(12.dp))
}