package com.example.gentecomoagente.ui.screens.gerente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gentecomoagente.model.AgentModel
import com.example.gentecomoagente.repository.AuthRepository
import com.example.gentecomoagente.service.AgentService
import com.example.gentecomoagente.ui.components.CustomButton
import com.example.gentecomoagente.ui.components.CustomTextField
import com.example.gentecomoagente.ui.components.CustomTopHeader
import com.example.gentecomoagente.ui.components.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentCreationScreen(navController: NavController) {

    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    var selectedRole by remember { mutableStateOf("agent") }

    var expanded by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }

    val roles = listOf("admin", "agent")

    val context = LocalContext.current

    val agentService = remember {
        AgentService()
    }

    val authRepository = remember {
        AuthRepository(context)
    }

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
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // 🔥 USERNAME
                CustomTextField(
                    label = "Nome do Agente",
                    value = nome,
                    onValueChange = { nome = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 🔥 EMAIL
                CustomTextField(
                    label = "E-mail",
                    value = email,
                    onValueChange = { email = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 🔥 SENHA
                CustomTextField(
                    label = "Senha",
                    value = senha,
                    onValueChange = { senha = it },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 🔥 DROPDOWN ROLE
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {

                    OutlinedTextField(
                        value = selectedRole,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Role")
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded
                            )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.LightGray
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {

                        roles.forEach { role ->

                            DropdownMenuItem(
                                text = {
                                    Text(role)
                                },
                                onClick = {
                                    selectedRole = role
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 🔥 BOTÃO
                CustomButton(
                    text = if (isLoading)
                        "Salvando..."
                    else
                        "Salvar Agente",

                    onClick = {

                        if (
                            nome.isBlank() ||
                            email.isBlank() ||
                            senha.isBlank()
                        ) {

                            showToast(
                                context,
                                "Preencha todos os campos"
                            )

                            return@CustomButton
                        }

                        isLoading = true

                        val adminEmail =
                            authRepository.getAdminEmail()

                        val adminPassword =
                            authRepository.getAdminPassword()

                        if (
                            adminEmail == null ||
                            adminPassword == null
                        ) {

                            isLoading = false

                            showToast(
                                context,
                                "Sessão do admin inválida"
                            )

                            return@CustomButton
                        }

                        val agent = AgentModel(
                            username = nome.trim(),
                            email = email.trim(),
                            role = selectedRole,
                            isActive = true
                        )

                        agentService.createAgent(
                            adminEmail = adminEmail,

                            adminPassword = adminPassword,

                            email = email.trim(),

                            password = senha,

                            agent = agent,

                            onSuccess = {

                                isLoading = false

                                // 🔥 limpa campos
                                nome = ""
                                email = ""
                                senha = ""
                                selectedRole = "agent"

                                // 🔥 toast sucesso
                                showToast(
                                    context,
                                    "Agente criado com sucesso"
                                )
                            },

                            onError = { error ->

                                isLoading = false

                                showToast(
                                    context,
                                    error
                                )
                            }
                        )
                    },

                    modifier = Modifier.fillMaxWidth(0.6f),

                    containerColor = Color(0xFFBDBDBD),

                    contentColor = Color.Black
                )
            }
        }
    }
}