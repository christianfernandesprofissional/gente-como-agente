package com.example.gentecomoagente.ui.screens.gerente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gentecomoagente.model.AgentModel
import com.example.gentecomoagente.service.AgentService
import com.example.gentecomoagente.ui.components.CustomButton
import com.example.gentecomoagente.ui.components.CustomTextField
import com.example.gentecomoagente.ui.components.CustomTopHeader
import com.example.gentecomoagente.ui.components.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentEditScreen(
    navController: NavController,
    agentId: String,
) {

    var nome by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var selectedRole by remember {
        mutableStateOf("agent")
    }

    var isActive by remember {
        mutableStateOf(true)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    val agentService = remember {
        AgentService()
    }

    val roles = listOf("admin", "agent")

    val context = LocalContext.current

    LaunchedEffect(agentId) {

        isLoading = true

        agentService.getAgentById(
            uid = agentId,

            onSuccess = { agent ->

                nome = agent?.username ?: ""
                email = agent?.email ?: ""
                selectedRole = agent?.role ?: "agent"
                isActive = agent?.isActive ?: true

                isLoading = false
            },

            onError = { error ->

                isLoading = false

                showToast(
                    context,
                    error
                )
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        CustomTopHeader(
            buttonText = "Voltar",
            buttonIcon = Icons.Default.ArrowBack,
            onClickButton = {
                navController.popBackStack()
            }
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

                // 🔥 ID DO AGENTE
                Text(
                    text = "ID do Agente",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = agentId,
                    onValueChange = {},
                    readOnly = true,

                    modifier = Modifier.fillMaxWidth(),

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.LightGray
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 🔥 NOME
                CustomTextField(
                    label = "Nome do Agente",
                    value = nome,
                    onValueChange = {
                        nome = it
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 🔥 EMAIL (READ ONLY)
                OutlinedTextField(
                    value = email,
                    onValueChange = {},
                    readOnly = true,

                    label = {
                        Text("E-mail")
                    },

                    modifier = Modifier.fillMaxWidth(),

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.LightGray
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 🔥 ROLE
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

                Spacer(modifier = Modifier.height(24.dp))

                // 🔥 STATUS DA CONTA
                Text(
                    text = "Status da Conta",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),

                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,

                        modifier = Modifier.selectable(
                            selected = isActive,
                            onClick = {
                                isActive = true
                            }
                        )
                    ) {

                        RadioButton(
                            selected = isActive,

                            onClick = {
                                isActive = true
                            }
                        )

                        Text("Ativo")
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,

                        modifier = Modifier.selectable(
                            selected = !isActive,
                            onClick = {
                                isActive = false
                            }
                        )
                    ) {

                        RadioButton(
                            selected = !isActive,

                            onClick = {
                                isActive = false
                            }
                        )

                        Text("Inativo")
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 🔥 BOTÃO SALVAR
                CustomButton(
                    text =
                        if (isLoading)
                            "Salvando..."
                        else
                            "Salvar Alterações",

                    onClick = {

                        if (nome.isBlank()) {

                            showToast(
                                context,
                                "Nome inválido"
                            )

                            return@CustomButton
                        }

                        isLoading = true

                        agentService.updateAgent(
                            uid = agentId,

                            username = nome.trim(),

                            role = selectedRole,

                            isActive = isActive,

                            onSuccess = {

                                isLoading = false

                                showToast(
                                    context,
                                    "Agente atualizado com sucesso"
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

                    modifier = Modifier.fillMaxWidth(0.7f),

                    containerColor = Color(0xFFBDBDBD),

                    contentColor = Color.Black
                )
            }
        }
    }
}