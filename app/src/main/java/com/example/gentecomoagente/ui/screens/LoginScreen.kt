package com.example.gentecomoagente.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gentecomoagente.repository.AgentRepository
import com.example.gentecomoagente.repository.AuthRepository
import com.example.gentecomoagente.ui.components.CustomButton
import com.example.gentecomoagente.ui.components.CustomTextField
import com.example.gentecomoagente.ui.components.CustomTopHeader
import com.example.gentecomoagente.ui.components.showToast
import com.example.gentecomoagente.ui.navigation.Routes

@Composable
fun LoginScreen(navController: NavController) {

    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authRepository = remember { AuthRepository() }
    val agentRepository = remember { AgentRepository() }

    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        CustomTopHeader(
            buttonText = "Voltar",
            buttonIcon = Icons.Default.ArrowBack,
            onClickButton = { navController.popBackStack() }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                CustomTextField(
                    label = "Email",
                    value = email,
                    onValueChange = { email = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                CustomTextField(
                    label = "Senha",
                    value = password,
                    onValueChange = { password = it },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Spacer(modifier = Modifier.height(40.dp))

                CustomButton(
                    text = if (isLoading) "Entrando..." else "Entrar",
                    onClick = {

                        if (email.isBlank() || password.isBlank()) {
                            showToast(context, "Preencha todos os campos")
                            return@CustomButton
                        }

                        isLoading = true

                        authRepository.login(
                            email = email.trim(),
                            password = password,
                            onSuccess = {

                                val uid = authRepository.getCurrentUserId()

                                if (uid == null) {
                                    isLoading = false
                                    showToast(context, "Erro ao obter usuário")
                                    return@login
                                }

                                agentRepository.getAgentById(
                                    uid = uid,
                                    onSuccess = { agent ->

                                        isLoading = false

                                        if (agent == null) {
                                            showToast(context, "Usuário não encontrado")
                                            return@getAgentById
                                        }

                                        if (!agent.isActive) {
                                            showToast(context, "Usuário inativo")
                                            return@getAgentById
                                        }

                                        when (agent.role.lowercase()) {

                                            "admin" -> {
                                                navController.navigate(Routes.GERENTE_HOME) {
                                                    popUpTo(0)
                                                }
                                            }

                                            "agent" -> {
                                                navController.navigate(Routes.TICKETS_AGENT) {
                                                    popUpTo(0)
                                                }
                                            }

                                            else -> {
                                                showToast(context, "Permissão inválida")
                                            }
                                        }
                                    },
                                    onError = {
                                        isLoading = false
                                        showToast(context, "Erro ao buscar usuário")
                                    }
                                )
                            },
                            onError = {
                                isLoading = false
                                showToast(context, "Email ou senha inválidos")
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(0.6f),
                    containerColor = Color(0xFFBDBDBD)
                )
            }
        }
    }
}