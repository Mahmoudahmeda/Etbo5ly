package com.example.etbo5ly.authentication.signup

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.etbo5ly.R
import com.example.etbo5ly.authentication.State

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    navController: NavController
) {
    val username by viewModel.username.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val state by viewModel.status.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(state) {
        when (state) {
            is State.Success -> {
                navController.navigate("home")
            }
            is State.Fail -> {
                Toast.makeText(context, (state as State.Fail).msg, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF13171F))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.account),
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            TextButton(
                onClick = { },
                modifier = Modifier
                    .background(Color(0xFF232832), CircleShape)
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = stringResource(R.string.sign_in),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF13171F)),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.chef),
                    contentDescription = stringResource(R.string.chef_illustration),
                    modifier = Modifier
                        .size(250.dp)
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = stringResource(R.string.welcome_chief),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = stringResource(R.string.discover_meal),
                    fontSize = 18.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                CustomInputField(
                    value = username,
                    onValueChange = viewModel::onUsernameChange,
                    label = stringResource(R.string.username)
                )

                CustomInputField(
                    value = email,
                    onValueChange = viewModel::onEmailChange,
                    label = stringResource(R.string.Email),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                CustomInputField(
                    value = password,
                    onValueChange = viewModel::onPasswordChange,
                    label = stringResource(R.string.password),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.performSignUp()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00EDFF)
                    )
                ) {
                    Text(
                        text = stringResource(R.string.sign_up),
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Divider(color = Color.DarkGray)

                Text(
                    text = stringResource(R.string.or_continue_with),
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                TextButton(
                    onClick = { },
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.continue_as_guest),
                        color = Color(0xFF00BFCE),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CustomInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(label, color = Color.Gray, fontSize = 20.sp, fontWeight = FontWeight.SemiBold) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF232832),
            unfocusedContainerColor = Color(0xFF232832),
            unfocusedTextColor = Color.White,
            focusedTextColor = Color.White,
            cursorColor = Color.Cyan
        ),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        textStyle = LocalTextStyle.current.copy(fontSize = 20.sp)
    )
}

@Composable
fun SocialButton(iconId: Int, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(2.dp, Color.DarkGray, RoundedCornerShape(16.dp))
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Image(
            painter = painterResource(id = iconId),
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, color = Color.Gray, fontSize = 16.sp)
    }
}