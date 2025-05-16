package com.pri.artsysearchapp.ui.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pri.artsysearchapp.common.SnackBarManager
import com.pri.artsysearchapp.ui.components.AppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    onRegisterSuccess: () -> Unit,
    registerViewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState = registerViewModel.uiState.collectAsState().value
    Scaffold(
        topBar = {
            AppBar(
                title = "Register",
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {

            OutlinedTextField(
                value = uiState.fullName,
                onValueChange = { registerViewModel.onFullNameChanged(it) },
                label = { Text("Enter full name") },
                isError = uiState.fullNameError != null,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (it.isFocused) {
                            registerViewModel.validateFullName()
                        }
                    }
            )
            if (uiState.fullNameError != null) {
                Text(
                    text = uiState.fullNameError,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { registerViewModel.onEmailChanged(it) },
                label = { Text("Enter email") },
                isError = uiState.emailError != null,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (it.isFocused) {
                            registerViewModel.validateEmail()
                        }
                    }
            )
            if (uiState.emailError != null) {
                Text(
                    text = uiState.emailError,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.password,
                onValueChange = { registerViewModel.onPasswordChanged(it) },
                label = { Text("Password") },
                isError = uiState.passwordError != null,
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (it.isFocused) {
                            registerViewModel.validatePassword()
                        }
                    }
            )
            if (uiState.passwordError != null) {
                Text(
                    text = uiState.passwordError,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    registerViewModel.onRegisterClicked(
                        onSuccess = {
                            SnackBarManager.showMessage("Registered successfully")
                            onRegisterSuccess()
                        }
                    )
                },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Register")
                }
            }

            // General Register Error
            if (uiState.generalError != null) {
                Text(
                    text = uiState.generalError,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            val uriHandler = LocalUriHandler.current

            Text(
                text = buildAnnotatedString {
                    append("Already have an account? ")
                    val link = LinkAnnotation.Url(
                        url = "login",
                        styles = TextLinkStyles(
                            SpanStyle(color = MaterialTheme.colorScheme.primary)
                        )
                    ) {
                        val url = (it as LinkAnnotation.Url).url
                        if (url == "login") {
                            // Navigate to login screen
                            navController.navigate("login") {
                                popUpTo("register") { inclusive = true }
                                launchSingleTop = true
                            }
                        } else {
                            uriHandler.openUri(url)
                        }
                    }
                    withLink(link) {
                        append("Login")
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}