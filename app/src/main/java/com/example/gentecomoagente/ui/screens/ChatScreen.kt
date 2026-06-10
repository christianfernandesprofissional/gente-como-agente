package com.example.gentecomoagente.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gentecomoagente.model.ChatMessage
import com.example.gentecomoagente.repository.TicketRepository
import com.example.gentecomoagente.ui.components.ChatMessageBubble
import com.example.gentecomoagente.ui.components.CustomButton
import com.google.firebase.Timestamp


//CHAT NOVO CHAT NOVO CHAT NOVO CHAT NOVO
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, ticketId: String, userType: String) {
    val ticketRepository = remember { TicketRepository() }
    val context = LocalContext.current
    // ESTADOS
    var inputText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<ChatMessage>() }
    var accessCode by remember { mutableStateOf("...") }
    var customerEmail by remember { mutableStateOf("") }
    var ticketStatus by remember { mutableStateOf("OPEN") }

    // Carregar dados do ticket
    LaunchedEffect(ticketId) {
        ticketRepository.listenToTicket(ticketId) { ticket ->
            if (ticket != null) {
                accessCode = ticket.accessCode
                customerEmail = ticket.customerEmail
                ticketStatus = ticket.status
            }
        }
    }

    // Escutar mensagens em tempo real
    LaunchedEffect(ticketId) {
        ticketRepository.listenToMessages(ticketId) { updatedMessages ->
            messages.clear()
            messages.addAll(updatedMessages)
        }
    }

    val listState = rememberLazyListState()

    // Rolagem automática para a última mensagem
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // --- CABEÇALHO ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .statusBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomButton(
                text = "Voltar",
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                onClick = { navController.popBackStack() },
                containerColor = Color(0xFFEEEEEE),
                contentColor = Color.Black,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            )

            if ((userType == "AGENT" || userType == "ADMIN") && ticketStatus != "CLOSED") {
                CustomButton(
                    text = "Encerrar Ticket",
                    onClick = {
                        ticketRepository.updateTicketStatus(
                            ticketId,
                            "CLOSED",
                            onSuccess = {
                                ticketStatus = "CLOSED"
                            },
                            onError = { error ->
                                Toast.makeText(
                                    context,
                                    error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    },
                    containerColor = Color(0xFFE57373),
                    contentColor = Color.White
                )
            }
        }

        Divider(color = Color(0xFF81D4FA), thickness = 1.dp)

        // --- PROTOCOLO OU TICKET ID ---
        Text(
            text = if (userType == "CLIENT") "Protocolo: $accessCode" else "Ticket: $ticketId",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            color = Color.DarkGray
        )

        // --- ÁREA DE CHAT ---
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
                .background(Color(0xFFF5F5F5))
                .border(1.dp, Color.Black)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { msg ->
                    ChatMessageBubble(message = msg)
                }
            }
        }

        // --- RODAPÉ (Desabilitado se ticket fechado) ---
        if (ticketStatus != "CLOSED") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text("Digite sua mensagem...", color = Color.Gray)
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.LightGray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                CustomButton(
                    text = "Enviar",
                    onClick = {
                        if (inputText.isNotBlank()) {
                            val newMessage = ChatMessage(
                                senderId = if (userType == "CLIENT") customerEmail else "AGENT",
                                senderType = userType,
                                content = inputText,
                                timestamp = Timestamp.now()
                            )
                            
                            ticketRepository.sendMessage(ticketId, newMessage, 
                                onSuccess = { inputText = "" },
                                onError = { /* Tratar erro */ }
                            )
                        }
                    },
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White,
                    modifier = Modifier.height(56.dp)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Este atendimento foi encerrado.",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
