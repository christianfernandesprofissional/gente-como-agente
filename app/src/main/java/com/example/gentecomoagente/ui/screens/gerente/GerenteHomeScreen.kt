package com.example.gentecomoagente.ui.screens.gerente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gentecomoagente.model.AgentModel
import com.example.gentecomoagente.repository.AuthRepository
import com.example.gentecomoagente.service.AgentService
import com.example.gentecomoagente.ui.components.CustomButton
import com.example.gentecomoagente.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GerenteHomeScreen(navController: NavController) {

    var filtroStatus by remember {
        mutableStateOf("Todos")
    }

    var expandedFiltro by remember {
        mutableStateOf(false)
    }

    val statusOptions = listOf(
        "Todos",
        "Ordem alfabética",
        "Ativos",
        "Inativos"
    )

    val agentService = remember { AgentService() }

    var agentes by remember { mutableStateOf<List<AgentModel>>(emptyList()) }
    val agentesFiltrados = when (filtroStatus) {

        "Ordem alfabética" -> {
            agentes.sortedBy { it.username.lowercase() }
        }

        "Ativos" -> {
            agentes.filter { it.isActive }
        }

        "Inativos" -> {
            agentes.filter { !it.isActive }
        }

        else -> agentes
    }

    var isLoading by remember { mutableStateOf(true) }
    val authRepository = remember {
        AuthRepository()
    }

    // 🔥 busca agentes reais do Firestore
    LaunchedEffect(Unit) {

        agentService.findAllAgents(
            onSuccess = { lista ->
                agentes = lista
                isLoading = false
            },
            onError = {
                isLoading = false
                println("Erro ao buscar agentes: $it")
            }
        )
    }

    Scaffold(
        containerColor = Color.White,

        // --- CABEÇALHO ---
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {

                Row(modifier = Modifier.fillMaxWidth().statusBarsPadding()) {

                    CustomButton(
                        text = "Sair",
                        onClick = {

                            authRepository.logout()

                            navController.navigate(Routes.LOGIN_Google) {

                                popUpTo(0)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(15),
                        containerColor = Color(0xFFE0E0E0),
                        contentColor = Color.Black
                    )

                    Spacer(modifier = Modifier.width(2.dp))

                    CustomButton(
                        text = "Tipos de Problema",
                        onClick = {
                            navController.navigate(Routes.PROBLEM_TYPE)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(15),
                        containerColor = Color(0xFFE0E0E0),
                        contentColor = Color.Black
                    )
                }

                Column(
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 16.dp
                    )
                ) {

                    Text(
                        text = "Agentes",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Gerente",
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

// 🔥 FILTROS
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF5F5F5)
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {

                            Text(
                                text = "Filtrar por:",
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Spacer(modifier = Modifier.height(12.dp))

                            // 🔥 FILTRO STATUS
                            ExposedDropdownMenuBox(
                                expanded = expandedFiltro,
                                onExpandedChange = {
                                    expandedFiltro = !expandedFiltro
                                }
                            ) {

                                OutlinedTextField(
                                    value = filtroStatus,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = {
                                        Text("Status")
                                    },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = expandedFiltro
                                        )
                                    },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                )

                                ExposedDropdownMenu(
                                    expanded = expandedFiltro,
                                    onDismissRequest = {
                                        expandedFiltro = false
                                    }
                                ) {

                                    statusOptions.forEach { option ->

                                        DropdownMenuItem(
                                            text = {
                                                Text(option)
                                            },
                                            onClick = {
                                                filtroStatus = option
                                                expandedFiltro = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Divider(
                    color = Color(0xFF00BCD4),
                    thickness = 1.dp
                )
            }
        },

        // --- RODAPÉ ---
        bottomBar = {

            Row(modifier = Modifier.fillMaxWidth().navigationBarsPadding()) {

                CustomButton(
                    text = "Visualizar Tickets",
                    onClick = {
                        navController.navigate(Routes.TICKETS_AGENT)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(15),
                    containerColor = Color(0xFFE0E0E0),
                    contentColor = Color.Black
                )

                Spacer(modifier = Modifier.width(2.dp))

                CustomButton(
                    text = "Cadastrar Novo Agente",
                    onClick = {
                        navController.navigate(Routes.AGENT_CREATION)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(15),
                    containerColor = Color(0xFFE0E0E0),
                    contentColor = Color.Black
                )
            }
        }

    ) { paddingValues ->

        // 🔥 loading
        if (isLoading) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        } else {

            // 🔥 sem agentes
            // 🔥 sem agentes
            if (agentes.isEmpty()) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "Sem agente cadastrado",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                }

            } else {

                // 🔥 lista real do firestore
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(agentesFiltrados) { agente ->

                        AgentListItem(
                            agente = agente,
                            navController
                        )
                    }
                }
            }
        }
    }
}

// --- CARD DO AGENTE ---
@Composable
fun AgentListItem(
    agente: AgentModel,
    navController: NavController
) {

    val statusText =
        if (agente.isActive) "Ativo"
        else "Inativo"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(12.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🔹 esquerda
            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = agente.username,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = agente.email,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // 🔹 direita
            Column(
                horizontalAlignment = Alignment.End
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    CustomButton(
                        text = "Editar",
                        onClick = { navController.navigate("${Routes.AGENT_EDIT}/${agente.id}") },
                        containerColor = Color(0xFF1976D2),
                        contentColor = Color.White,
                        fontSize = 11.sp,
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        elevation = 2.dp,
                        shape = RoundedCornerShape(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                CustomButton(
                    text = "Cargo: " + if (agente.role.uppercase() == "AGENT") {
                        "Agente"
                    } else {
                        "Admin"
                    },
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(0.5f),
                    containerColor = Color(0xFF388E3C),
                    contentColor = Color.White,
                    fontSize = 11.sp,
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    elevation = 2.dp,
                    shape = RoundedCornerShape(4.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = statusText,
                    fontSize = 12.sp,
                    color =
                        if (agente.isActive)
                            Color(0xFF2E7D32)
                        else
                            Color.Red
                )
            }
        }
    }
}