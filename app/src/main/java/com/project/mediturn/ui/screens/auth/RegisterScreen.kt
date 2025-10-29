package com.project.mediturn.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.mediturn.viewmodel.AuthMessage
import com.project.mediturn.viewmodel.AuthUiState
import com.project.mediturn.viewmodel.AuthViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    viewModel: AuthViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Estados del ViewModel
    val name by viewModel.registerName.collectAsState()
    val email by viewModel.registerEmail.collectAsState()
    val password by viewModel.registerPassword.collectAsState()
    val confirmPassword by viewModel.registerConfirmPassword.collectAsState()
    val phone by viewModel.registerPhone.collectAsState()
    val dni by viewModel.registerDni.collectAsState()

    val passwordVisible by viewModel.registerPasswordVisible.collectAsState()
    val confirmPasswordVisible by viewModel.registerConfirmPasswordVisible.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    // Errores de validación
    val nameError by viewModel.registerNameError.collectAsState()
    val emailError by viewModel.registerEmailError.collectAsState()
    val phoneError by viewModel.registerPhoneError.collectAsState()
    val dniError by viewModel.registerDniError.collectAsState()
    val passwordError by viewModel.registerPasswordError.collectAsState()
    val confirmPasswordError by viewModel.registerConfirmPasswordError.collectAsState()

    val isFormValid = viewModel.isRegisterFormValid()
    val isLoading = uiState is AuthUiState.Loading

    // 🔍 DEBUG: Mostrar estado de validaciones
    LaunchedEffect(name, email, phone, dni, password, confirmPassword) {
        println("=== 🔍 DEBUG REGISTRO ===")
        println("Name: '$name' (${name.length} chars) - Error: $nameError")
        println("Email: '$email' - Error: $emailError")
        println("Phone: '$phone' - Error: $phoneError")
        println("DNI: '$dni' (${dni.length} chars) - Error: $dniError")
        println("Password: '$password' (${password.length} chars) - Error: $passwordError")
        println("Confirm: '$confirmPassword' - Error: $confirmPasswordError")
        println("isFormValid: $isFormValid")
        println("isLoading: $isLoading")
        println("========================")
    }

    // Escuchar mensajes de autenticación
    LaunchedEffect(Unit) {
        viewModel.authMessage.collectLatest { message ->
            when (message) {
                is AuthMessage.Success -> {
                    snackbarHostState.showSnackbar(
                        message = message.message,
                        duration = SnackbarDuration.Short
                    )
                    onNavigateToHome()
                }
                is AuthMessage.Error -> {
                    snackbarHostState.showSnackbar(
                        message = message.message,
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Crear Cuenta",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icono y texto de bienvenida
                Text(
                    text = "📝",
                    fontSize = 64.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Complete sus datos",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Regístrese para comenzar a agendar citas",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 🔍 DEBUG CARD - Mostrar estado
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text("🔍 DEBUG INFO", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Formulario válido: ${if (isFormValid) "✅ SÍ" else "❌ NO"}", fontSize = 11.sp)
                        Text("Botón habilitado: ${if (isFormValid && !isLoading) "✅ SÍ" else "❌ NO"}", fontSize = 11.sp)
                        if (nameError != null) Text("❌ Nombre: $nameError", fontSize = 10.sp)
                        if (emailError != null) Text("❌ Email: $emailError", fontSize = 10.sp)
                        if (phoneError != null) Text("❌ Teléfono: $phoneError", fontSize = 10.sp)
                        if (dniError != null) Text("❌ DNI: $dniError", fontSize = 10.sp)
                        if (passwordError != null) Text("❌ Password: $passwordError", fontSize = 10.sp)
                        if (confirmPasswordError != null) Text("❌ Confirmar: $confirmPasswordError", fontSize = 10.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Formulario
                OutlinedTextField(
                    value = name,
                    onValueChange = { viewModel.updateRegisterName(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nombre Completo *") },
                    placeholder = { Text("Ej: Juan Pérez García") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = "Nombre")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true,
                    enabled = !isLoading,
                    isError = nameError != null,
                    supportingText = {
                        if (nameError != null) {
                            Text(nameError!!, color = MaterialTheme.colorScheme.error)
                        } else {
                            Text("Mínimo 3 caracteres", fontSize = 12.sp)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { viewModel.updateRegisterEmail(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Email *") },
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
                    enabled = !isLoading,
                    isError = emailError != null,
                    supportingText = {
                        if (emailError != null) {
                            Text(emailError!!, color = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { viewModel.updateRegisterPhone(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Teléfono *") },
                    placeholder = { Text("987654321") },
                    leadingIcon = {
                        Icon(Icons.Default.Phone, contentDescription = "Teléfono")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true,
                    enabled = !isLoading,
                    isError = phoneError != null,
                    supportingText = {
                        if (phoneError != null) {
                            Text(phoneError!!, color = MaterialTheme.colorScheme.error)
                        } else {
                            Text("Formato: 9XXXXXXXX", fontSize = 12.sp)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = dni,
                    onValueChange = { viewModel.updateRegisterDni(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("DNI *") },
                    placeholder = { Text("12345678") },
                    leadingIcon = {
                        Icon(Icons.Default.Badge, contentDescription = "DNI")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true,
                    enabled = !isLoading,
                    isError = dniError != null,
                    supportingText = {
                        if (dniError != null) {
                            Text(dniError!!, color = MaterialTheme.colorScheme.error)
                        } else {
                            Text("${dni.length}/8 dígitos", fontSize = 12.sp)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { viewModel.updateRegisterPassword(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Contraseña *") },
                    placeholder = { Text("Mínimo 8 caracteres") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = "Contraseña")
                    },
                    trailingIcon = {
                        IconButton(onClick = { viewModel.toggleRegisterPasswordVisibility() }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Ocultar" else "Mostrar"
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true,
                    enabled = !isLoading,
                    isError = passwordError != null,
                    supportingText = {
                        if (passwordError != null) {
                            Text(passwordError!!, color = MaterialTheme.colorScheme.error)
                        } else {
                            Text("Debe contener mayúscula, minúscula y número", fontSize = 12.sp)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { viewModel.updateRegisterConfirmPassword(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Confirmar Contraseña *") },
                    placeholder = { Text("Repita su contraseña") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = "Confirmar")
                    },
                    trailingIcon = {
                        IconButton(onClick = { viewModel.toggleRegisterConfirmPasswordVisibility() }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (confirmPasswordVisible) "Ocultar" else "Mostrar"
                            )
                        }
                    },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            if (isFormValid) {
                                viewModel.register()
                            }
                        }
                    ),
                    singleLine = true,
                    enabled = !isLoading,
                    isError = confirmPasswordError != null,
                    supportingText = {
                        if (confirmPasswordError != null) {
                            Text(confirmPasswordError!!, color = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Botón de Registro
                Button(
                    onClick = {
                        println("🔘 BOTÓN PRESIONADO - Llamando a viewModel.register()")
                        viewModel.register()
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
                            Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Crear Cuenta ${if (isFormValid) "✅" else "❌"}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de volver a Login
                TextButton(
                    onClick = onNavigateToLogin,
                    enabled = !isLoading
                ) {
                    Text("¿Ya tienes cuenta? Inicia sesión")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Información de privacidad
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
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
                            Icons.Default.Security,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            "Tu información está protegida con encriptación de nivel bancario",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            lineHeight = 16.sp
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
                            Text("Creando cuenta...")
                        }
                    }
                }
            }
        }
    }
}