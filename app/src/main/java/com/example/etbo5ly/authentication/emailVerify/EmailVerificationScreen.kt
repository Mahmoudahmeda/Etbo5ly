package com.example.etbo5ly.authentication.emailVerify

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.etbo5ly.R
import com.example.etbo5ly.authentication.State

@SuppressLint("ViewModelConstructorInComposable")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailVerificationScreen() {
    var email by remember { mutableStateOf("") }
    val emailVerify: emailVerify = viewModel()
    val emailstate by emailVerify.status.collectAsState()
    val context = LocalContext.current
    
    LaunchedEffect(emailstate) {
        Log.d("Email", "in launch State ${emailstate}")
        when (emailstate) {
            is State.Success -> {
                Toast.makeText(context, "Check your Email Box", Toast.LENGTH_SHORT).show()
            }
            is State.Fail -> {
                Toast.makeText(context, (emailstate as State.Fail).msg, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = {
                Text(
                    "Reset Password",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 25.sp
                )
            }
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = {
                Text(text = stringResource(R.string.Email))
            },
            shape = RoundedCornerShape(25.dp),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = "Email icon",
                    tint = Color.Gray
                )
            },
            modifier = Modifier
                .padding(horizontal = 40.dp, vertical = 8.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { emailVerify.sendEmail(email) },
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Cyan,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("Send Email", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}
