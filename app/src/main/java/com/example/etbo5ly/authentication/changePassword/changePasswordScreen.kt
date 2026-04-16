package com.example.etbo5ly.authentication.changePassword

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.etbo5ly.authentication.State
import com.example.etbo5ly.R

@SuppressLint("ViewModelConstructorInComposable")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(navController: NavController, code: String?){
    var password by remember {
        mutableStateOf("")
    }
    var repassword by remember {
        mutableStateOf("")
    }
    val changePass:changePassword= viewModel()
    val state by changePass.status.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state) {
        when (state) {
            is State.Success -> {
                Toast.makeText(context, context.getString(R.string.password_updated), Toast.LENGTH_SHORT).show()
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            }
            is State.Fail -> {
                Toast.makeText(context, (state as State.Fail).msg, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }
    Column() {
        TopAppBar(
            { Text(stringResource(R.string.new_password)) }
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text(stringResource(R.string.password)) },
            shape = RoundedCornerShape(25.dp),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .padding(start = 50.dp, top = 7.dp, end = 30.dp, bottom = 5.dp)
                .fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = stringResource(R.string.password_field),
                    tint = Color.Gray
                )
            }
        )

        TextField(
            value = repassword,
            onValueChange = { repassword = it },
            placeholder = { Text(stringResource(R.string.repassword)) },
            shape = RoundedCornerShape(25.dp),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .padding(start = 50.dp, top = 7.dp, end = 30.dp, bottom = 5.dp)
                .fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = stringResource(R.string.password_field),
                    tint = Color.Gray
                )
            }
        )

        Button(
            { changePass.changePassword(code,password,repassword) },
            content = { Text(stringResource(R.string.change), fontSize = 25.sp, fontWeight = FontWeight.Bold) },
            modifier = Modifier
                .padding(
                    top = 7.dp,
                    start = 50.dp,
                    end = 30.dp
                )
                .fillMaxWidth()
                .size(50.dp),
            colors = ButtonColors(Color.Cyan,
                Color.Black,
                Color.Cyan,
                Color.Gray
            )
        )
    }
}