package com.example.etbo5ly.authentication.signin


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import com.example.etbo5ly.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.etbo5ly.authentication.facebokk_login.facebook
import com.example.etbo5ly.authentication.google_signin.google
import com.facebook.login.LoginManager
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import com.example.etbo5ly.authentication.State

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun Signin_screen(viewModel: Signin, navController: NavController) {

    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    val buttoncolor = ButtonColors(Color.Cyan,
        Color.Black,
        Color.Cyan,
        Color.Gray
    )
    val context = LocalContext.current
    val loginstate by viewModel.status.collectAsStateWithLifecycle()
    val credentialManager = CredentialManager.create(context)
    val credentialScope = rememberCoroutineScope()
    val facebookAuth= remember {
        facebook{
            token -> viewModel.LoginWithFacebook(
            token
            )
        }}
    val facebookActivityLauncher = rememberLauncherForActivityResult(
        LoginManager.getInstance()
            .createLogInActivityResultContract(facebookAuth.callbackManager)){}
    val scroll = rememberScrollState()

    LaunchedEffect(Unit, key2 = loginstate) {
        viewModel.status.collect { state ->
            if (state is State.Success || FirebaseAuth.getInstance().currentUser != null) {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
    }
    DisposableEffect(Unit) {
        facebookAuth.registercallback()
        onDispose {
            facebookAuth.unregistercallback()
        }
    }
    Column(Modifier.fillMaxSize().verticalScroll(scroll)) {

        TopAppBar(
            {
                Text("Account", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            },
            actions = {
                Button(
                    { navController.navigate("signup") },
                    content = { Text("Signup", fontSize = 15.sp, fontWeight = FontWeight.Bold) },
                    colors = buttoncolor
                )
            }
        )

        Image(
            painter = painterResource(R.drawable.chef),
            contentDescription = stringResource(R.string.image_of_chef),
            modifier = Modifier
                .padding(start = 50.dp)
                .size(280.dp)
        )

        Text(
            stringResource(R.string.Signin_screen_text_1),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,   
            modifier = Modifier.padding(start = 130.dp, bottom = 5.dp)
        )

        Text(
            stringResource(R.string.Signin_screen_text_2),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 110.dp, bottom = 5.dp)
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = {
                Text(
                    text = stringResource(R.string.Email)
                )
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
                .padding(start = 50.dp, end = 30.dp, bottom = 5.dp)
                .fillMaxWidth()
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
                    contentDescription = "Password Field",
                    tint = Color.Gray
                )
            }

        )

        Button(
            { viewModel.LoginWithEmail(email, password) },
            content = { Text("Sign In", fontSize = 25.sp, fontWeight = FontWeight.Bold) },
            modifier = Modifier
                .padding(
                    top = 7.dp,
                    start = 50.dp,
                    end = 30.dp
                )
                .fillMaxWidth()
                .size(50.dp),
            colors = buttoncolor
        )

        TextButton(
            content = {
                Text(
                    text = "Forget Password ?",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.End,
                    color = Color.Cyan,
                )
            },
            modifier = Modifier.padding(
                end = 10.dp,
                start = 260.dp
            ),
            onClick = { navController.navigate("emailscreen")},
        )

        Row(
            Modifier.padding(bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            HorizontalDivider(
                Modifier.weight(1f),
                thickness = 1.dp,
                color = Color.Gray
            )

            Text(
                "OR CONTINUE WITH",
                Modifier.padding(horizontal = 8.dp)
            )

            HorizontalDivider(
                Modifier.weight(1f),
                thickness = 1.dp,
                color = Color.Gray
            )
        }

        Row(
            Modifier.fillMaxWidth(),
            Arrangement.Center,
            Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    credentialScope.launch {
                        val googleIdOption = GetGoogleIdOption.Builder()
                            .setServerClientId(getString(context, R.string.default_web_client_id))
                            .setFilterByAuthorizedAccounts(false)
                            .build()

                        val request = GetCredentialRequest.Builder()
                            .addCredentialOption(googleIdOption)
                            .build()
                        val result = credentialManager.getCredential(context, request)
                        google(result, viewModel)
                    }
                },
                content = {
                    Row(Modifier) {
                        Icon(
                            painter = painterResource(R.drawable.google_logo),
                            contentDescription = "Google Logo",
                            modifier = Modifier.padding(top = 6.dp, end = 15.dp)
                        )
                        Text(
                            text = "Google",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                },
                modifier = Modifier.padding(start = 15.dp, end = 10.dp),
                border = BorderStroke(1.dp, color = Color.Gray),
                colors = ButtonColors(
                    Color.Transparent,
                    Color.Gray,
                    Color.Transparent,
                    Color.Gray
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Button(
                onClick = {
                    facebookActivityLauncher.launch(
                        listOf(
                            "email",
                            "public_profile"
                        )
                    )
                },
                content = {
                    Row(Modifier) {
                        Icon(
                            painter = painterResource(R.drawable.facebook_logo),
                            contentDescription = "Facebook Logo",
                            modifier = Modifier.padding(top = 4.dp, end = 15.dp)
                        )
                        Text(
                            text = "Facebook",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                },
                modifier = Modifier.padding(start = 10.dp),
                border = BorderStroke(1.dp, color = Color.Gray),
                colors = ButtonColors(
                    Color.Transparent,
                    Color.Gray,
                    Color.Transparent,
                    Color.Gray
                ),
                shape = RoundedCornerShape(8.dp),
            )
        }

        BottomAppBar({
                TextButton(
                    content = {
                        Text(
                            text = "Continue as a Guest",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Cyan,
                        )
                    },
                    onClick = { viewModel.LoginasgGuest() },
                )
            },
            contentPadding = PaddingValues(
                end = 0.dp,
                start = 210.dp
            ),
            containerColor = Color.Transparent
        )
    }
}