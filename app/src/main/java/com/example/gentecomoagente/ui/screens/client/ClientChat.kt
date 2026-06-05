package com.example.gentecomoagente.ui.screens.client


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gentecomoagente.model.ChatMessage
import com.example.gentecomoagente.repository.TicketRepository
import com.example.gentecomoagente.ui.components.ChatMessageBubble
import com.example.gentecomoagente.ui.components.CustomButton
import com.example.gentecomoagente.ui.components.CustomTopHeader
import com.example.gentecomoagente.ui.components.TypingIndicator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatClientScreen(navController: NavController, ticketId: String) {
    val ticketRepository = remember { TicketRepository() }
    
    // ESTADOS
    var inputText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<ChatMessage>() }
    var accessCode by remember { mutableStateOf("...") }
    var customerEmail by remember { mutableStateOf("client") }

    // Carregar dados do ticket (incluindo accessCode)
    LaunchedEffect(ticketId) {
        ticketRepository.getTicket(ticketId, 
            onSuccess = { ticket ->
                accessCode = ticket?.accessCode ?: "N/A"
                customerEmail = ticket?.customerEmail ?: "client"
            },
            onError = { /* Tratar erro */ }
        )
    }

    // Escutar mensagens em tempo real
    LaunchedEffect(ticketId) {
        ticketRepository.listenToMessages(ticketId) { updatedMessages ->
            messages.clear()
            messages.addAll(updatedMessages)
        }
    }

    var isAgentTyping by remember { mutableStateOf(false) } // Pode ser implementado no futuro

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

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
        // --- CABEÇALHO (Refatorado) ---
        CustomTopHeader(
            buttonText = "Voltar",
            buttonIcon = Icons.Default.ArrowBack,
            onClickButton = { navController.popBackStack() }
        )

        // --- PROTOCOLO (ACCESS CODE) ---
        Text(
            text = "Protocolo: $accessCode",
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

                if (isAgentTyping) {
                    item { TypingIndicator() }
                }
            }
        }

        // --- RODAPÉ ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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

            // --- BOTÃO ENVIAR ---
            CustomButton(
                text = "Enviar",
                onClick = {
                    if (inputText.isNotBlank()) {
                        val newMessage = ChatMessage(
                            senderId = customerEmail,
                            senderType = "CLIENT",
                            content = inputText,
                            timestamp = com.google.firebase.Timestamp.now()
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
    }
}