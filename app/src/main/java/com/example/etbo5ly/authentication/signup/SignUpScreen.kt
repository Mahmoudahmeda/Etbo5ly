package com.example.etbo5ly.authentication.signup

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import androidx.core.content.ContextCompat.getString
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.etbo5ly.R
import com.example.etbo5ly.authentication.State
import com.example.etbo5ly.authentication.facebook_signup.facebookSignUp
import com.example.etbo5ly.authentication.google_signup.googleSignUp
import com.facebook.login.LoginManager
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    navController: NavController
) {
    val username by viewModel.username.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val state by viewModel.status.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val credentialScope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }

    val facebookAuth = remember {
        facebookSignUp { token -> viewModel.signUpWithFacebook(token) }
    }
    val facebookActivityLauncher = rememberLauncherForActivityResult(
        contract = LoginManager.getInstance()
            .createLogInActivityResultContract(facebookAuth.callbackManager),
        onResult = {}
    )
    DisposableEffect(Unit) {
        facebookAuth.registercallback()
        onDispose { facebookAuth.unregistercallback() }
    }

    LaunchedEffect(Unit, key2 = state) {
        viewModel.status.collect { currentState ->
            when (currentState) {
                is State.Success -> {
                    navController.navigate("Home") {
                        popUpTo("signup") { inclusive = true }
                    }
                }
                is State.Fail -> {
                    Toast.makeText(context, currentState.msg, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
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
                text = "Account",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            TextButton(
                onClick = { navController.navigate("login") },
                modifier = Modifier
                    .background(Color(0xFF232832), CircleShape)
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Sign In",
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
                    contentDescription = stringResource(R.string.image_of_chef),
                    modifier = Modifier
                        .size(250.dp)
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = "Welcome Chief !",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "Discover your next favorite meal today",
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
                    label = "Username"
                )
                CustomInputField(
                    value = email,
                    onValueChange = viewModel::onEmailChange,
                    label = "Email",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                CustomInputField(
                    value = password,
                    onValueChange = viewModel::onPasswordChange,
                    label = "Password",
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.performSignUp() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00EDFF))
                ) {
                    Text(
                        text = "Sign Up",
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                HorizontalDivider(color = Color.DarkGray)

                Text(
                    text = "OR CONTINUE WITH",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Row(
                    Modifier.fillMaxWidth(),
                    Arrangement.Center,
                    Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            credentialScope.launch {
                                try {
                                    val googleIdOption = GetGoogleIdOption.Builder()
                                        .setServerClientId(getString(context, R.string.default_web_client_id))
                                        .setFilterByAuthorizedAccounts(false)
                                        .build()
                                    val request = GetCredentialRequest.Builder()
                                        .addCredentialOption(googleIdOption)
                                        .build()
                                    val result = credentialManager.getCredential(context, request)
                                    googleSignUp(result, viewModel)
                                } catch (e: GetCredentialCancellationException) {
                                } catch (e: NoCredentialException) {
                                    Toast.makeText(context, "No Google account found on this device", Toast.LENGTH_SHORT).show()
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Google Sign Up Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        content = {
                            Row {
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
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )


                    Button(
                        onClick = {
                            facebookActivityLauncher.launch(
                                listOf("email", "public_profile")
                            )
                        },
                        content = {
                            Row {
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
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = { viewModel.signUpAsGuest() },
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = "Continue as Guest",
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
        placeholder = {
            Text(label, color = Color.Gray, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        },
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