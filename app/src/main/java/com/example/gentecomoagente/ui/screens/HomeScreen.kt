package com.example.gentecomoagente.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gentecomoagente.model.ProblemTypeModel
import com.example.gentecomoagente.model.TicketModel
import com.example.gentecomoagente.repository.ProblemTypeRepository
import com.example.gentecomoagente.repository.TicketRepository
import com.example.gentecomoagente.ui.components.CustomButton
import com.example.gentecomoagente.ui.components.CustomDropdown
import com.example.gentecomoagente.ui.components.CustomTextField
import com.example.gentecomoagente.ui.navigation.Routes
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth


@Composable
fun HomeScreen(navController: NavController) {
    val problemRepository = remember { ProblemTypeRepository() }
    val ticketRepository = remember { TicketRepository() }

    val auth = FirebaseAuth.getInstance()
    val currentEmail = auth.currentUser?.email ?: ""
    
    // ESTADOS
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(currentEmail) }
    var descricao by remember { mutableStateOf("") }

    var problemTypes by remember { mutableStateOf<List<ProblemTypeModel>>(emptyList()) }
    var problemaSelecionado by remember { mutableStateOf("") }

    var showSuccessDialog by remember { mutableStateOf(false) }
    var createdTicketId by remember { mutableStateOf("") }
    var createdAccessCode by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val isFormValid = nome.isNotBlank() && email.isNotBlank() && descricao.isNotBlank() && problemaSelecionado.isNotBlank()

    // Carregar tipos de problema do Firestore
    LaunchedEffect(Unit) {
        problemRepository.findAll(
            onSuccess = { list ->
                problemTypes = list
                if (list.isNotEmpty()) {
                    problemaSelecionado = list[0].name
                }
            },
            onError = { /* Tratar erro se necessário */ }
        )
    }

    val opcoesProblema = problemTypes.map { it.name }

    // Container Principal
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(1.dp),
        contentAlignment = Alignment.Center
    ) {
        // Card Branco Central
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            // Column com Scroll Vertical
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // --- CABEÇALHO ESPECÍFICO DESTA TELA ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomButton(
                        text = "Voltar",
                        onClick = { navController.popBackStack() }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Centro de Suporte ao Cliente",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Inicie um novo atendimento ou informe o Ticket para continuar uma conversa",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                Divider(color = Color(0xFF81D4FA), thickness = 1.dp) // Linha azul clara

                Spacer(modifier = Modifier.height(24.dp))

                // --- FORMULÁRIO ---
                CustomTextField(
                    label = "Nome Completo",
                    value = nome,
                    onValueChange = { nome = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    label = "Email",
                    value = email,
                    onValueChange = { },
                    readOnly = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomDropdown(
                    label = "Tipo de Problema",
                    options = opcoesProblema,
                    selectedOption = problemaSelecionado,
                    onOptionSelected = { problemaSelecionado = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    label = "Descrição do Problema",
                    value = descricao,
                    onValueChange = { descricao = it },
                    singleLine = false,
                    minLines = 4
                )

                Spacer(modifier = Modifier.height(32.dp))

                // --- RODAPÉ (Botões Lado a Lado) ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
//                    CustomButton(
//                        text = "Ticket Existente",
//                        onClick = { navController.navigate(Routes.TICKET_EXISTENTE) },
//                        modifier = Modifier.weight(1f)
//                    )

                    CustomButton(
                        text = if (isLoading) "Enviando..." else "Iniciar Atendimento",
                        onClick = {
                            isLoading = true
                            val newTicket = TicketModel(
                                customerName = nome,
                                customerEmail = email,
                                problemType = problemaSelecionado,
                                status = "OPEN",
                                createdAt = Timestamp.now(),
                                lastMessage = descricao,
                                lastMessageAt = Timestamp.now()
                            )

                            ticketRepository.createTicket(
                                ticket = newTicket,
                                initialMessage = descricao,
                                onSuccess = { id, accessCode ->
                                    createdTicketId = id
                                    createdAccessCode = accessCode
                                    showSuccessDialog = true
                                    isLoading = false
                                },
                                onError = { error ->
                                    isLoading = false
                                    Log.e("HomeScreen", "Erro ao criar ticket: $error")
                                }
                            )
                        },
                        enabled = isFormValid && !isLoading,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Ticket Criado com Sucesso!") },
            text = {
                Column {
                    Text("Número do Ticket:", fontSize = 14.sp)
                    Text(text = createdTicketId, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1976D2))
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text("Código de Acesso:", fontSize = 14.sp)
                    Text(text = createdAccessCode, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFFD32F2F))
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("IMPORTANTE: Guarde esses dados para consultar seu atendimento futuramente.", fontSize = 12.sp, color = Color.Gray)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showSuccessDialog = false
                    navController.navigate("${Routes.CHAT_GERAL}/$createdTicketId/CLIENT")
                }) {
                    Text("Ir para o Atendimento")
                }
            }
        )
    }
}
