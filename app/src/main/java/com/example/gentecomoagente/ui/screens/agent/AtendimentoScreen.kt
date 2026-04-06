package com.example.gentecomoagente.ui.screens.agent

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
import com.example.gentecomoagente.ui.components.ChatMessageBubble
import com.example.gentecomoagente.ui.components.CustomButton
import com.example.gentecomoagente.ui.components.TypingIndicator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AtendimentoScreen(navController: NavController) {
    var inputText by remember { mutableStateOf("") }
    val ticketNumber = "TKT-001234"

    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                "Olá, Gustavo. Tudo bem? Vou te ajudar com o seu problema de acesso.",
                isFromClient = false
            ),
            ChatMessage(
                "Estou com um problema ao acessar o site de vendas. Toda vez que clico em 'carrinho' a página atualiza e eu perco os produtos selecionados.",
                isFromClient = true
            )
        )
    }

    var isAgentTyping by remember { mutableStateOf(true) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // --- CABEÇALHO (Refatorado com CustomButton) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botão Voltar
            CustomButton(
                text = "Voltar",
                icon = Icons.Default.ArrowBack,
                onClick = { navController.popBackStack() },
                containerColor = Color(0xFFEEEEEE),
                contentColor = Color.Black,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            )

            // Botão Encerrar Ticket
            CustomButton(
                text = "Encerrar Ticket",
                onClick = { /* lógica para encerrar ticket */ },
                containerColor = Color(0xFFE57373), // Vermelho claro
                contentColor = Color.White
            )
        }

        Divider(color = Color(0xFF81D4FA), thickness = 1.dp)

        // --- NÚMERO DO TICKET ---
        Text(
            text = "Ticket: $ticketNumber",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(8.dp))

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
                    Text("Tudo bem. O que devo te informar?", color = Color.Gray)
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

            // --- BOTÃO ENVIAR (Refatorado com CustomButton) ---
            CustomButton(
                text = "Enviar",
                onClick = {
                    if (inputText.isNotBlank()) {
                        messages.add(
                            ChatMessage(
                                text = inputText,
                                isFromClient = true
                            )
                        )
                        inputText = ""

                        coroutineScope.launch {
                            listState.animateScrollToItem(messages.size)
                        }
                    }
                },
                containerColor = Color(0xFF4CAF50), // Verde
                contentColor = Color.White,
                modifier = Modifier.height(56.dp) // Mantém a altura igual a do campo de texto
            )
        }
    }
}