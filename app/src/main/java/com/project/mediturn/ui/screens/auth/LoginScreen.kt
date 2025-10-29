package com.project.mediturn.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.mediturn.viewmodel.AuthMessage
import com.project.mediturn.viewmodel.AuthUiState
import com.project.mediturn.viewmodel.AuthViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    viewModel: AuthViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Estados del ViewModel
    val email by viewModel.loginEmail.collectAsState()
    val password by viewModel.loginPassword.collectAsState()
    val passwordVisible by viewModel.loginPasswordVisible.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    val isFormValid = viewModel.isLoginFormValid()
    val isLoading = uiState is AuthUiState.Loading

    // 🔍 DEBUG: Mostrar estado
    LaunchedEffect(email, password, uiState, isLoggedIn) {
        println("=== 🔍 DEBUG LOGIN ===")
        println("Email: '$email'")
        println("Password: '$password' (${password.length} chars)")
        println("isFormValid: $isFormValid")
        println("isLoading: $isLoading")
        println("isLoggedIn: $isLoggedIn")
        println("uiState: $uiState")
        println("=====================")
    }

    // Escuchar mensajes de autenticación
    LaunchedEffect(Unit) {
        viewModel.authMessage.collectLatest { message ->
            println("🔔 AUTH MESSAGE: $message")
            when (message) {
                is AuthMessage.Success -> {
                    println("✅ LOGIN EXITOSO: ${message.message}")
                    snackbarHostState.showSnackbar(
                        message = message.message,
                        duration = SnackbarDuration.Short
                    )
                    // Navegar al home después de login exitoso
                    println("🚀 Navegando a Home...")
                    onNavigateToHome()
                }
                is AuthMessage.Error -> {
                    println("❌ LOGIN ERROR: ${message.message}")
                    snackbarHostState.showSnackbar(
                        message = message.message,
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }

    // Si ya está logueado, ir directo al home
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn && uiState is AuthUiState.Success) {
            println("✅ Ya está logueado, navegando a Home...")
            onNavigateToHome()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo y título
                Text(
                    text = "🏥",
                    fontSize = 72.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "MediTurn",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Agenda tus citas médicas fácilmente",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(48.dp))

                // 🔍 DEBUG CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text("🔍 DEBUG INFO", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Email válido: ${email.isNotBlank()}", fontSize = 11.sp)
                        Text("Password válido: ${password.isNotBlank()}", fontSize = 11.sp)
                        Text("Formulario válido: ${if (isFormValid) "✅" else "❌"}", fontSize = 11.sp)
                        Text("Botón habilitado: ${if (isFormValid && !isLoading) "✅" else "❌"}", fontSize = 11.sp)
                        Text("Estado: $uiState", fontSize = 10.sp)
                        Text("Sesión activa: $isLoggedIn", fontSize = 10.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Formulario de Login
                OutlinedTextField(
                    value = email,
                    onValueChange = { viewModel.updateLoginEmail(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Email") },
                    placeholder = { Text("ejemplo@email.com") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = "Email")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true,
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { viewModel.updateLoginPassword(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Contraseña") },
                    placeholder = { Text("Ingrese su contraseña") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = "Contraseña")
                    },
                    trailingIcon = {
                        IconButton(onClick = { viewModel.toggleLoginPasswordVisibility() }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            if (isFormValid) {
                                println("⌨️ ENTER presionado, llamando login()")
                                viewModel.login()
                            }
                        }
                    ),
                    singleLine = true,
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón de Login
                Button(
                    onClick = {
                        println("🔘 BOTÓN LOGIN PRESIONADO")
                        println("📧 Email: '$email'")
                        println("🔑 Password: '$password'")
                        viewModel.login()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = isFormValid && !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            Icons.Default.Login,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Iniciar Sesión ${if (isFormValid) "✅" else "❌"}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Divider con "O"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f))
                    Text(
                        "  O  ",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de Registro
                OutlinedButton(
                    onClick = onNavigateToRegister,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !isLoading
                ) {
                    Icon(
                        Icons.Default.PersonAdd,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Crear Cuenta",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Información adicional
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Si no tienes cuenta, regístrate para comenzar a agendar tus citas médicas",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Loading Overlay
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator()
                            Text("Iniciando sesión...")
                        }
                    }
                }
            }
        }
    }
}