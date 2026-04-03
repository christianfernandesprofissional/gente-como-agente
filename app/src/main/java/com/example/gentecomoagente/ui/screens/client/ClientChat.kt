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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gentecomoagente.model.ChatMessage
import com.example.gentecomoagente.ui.components.ChatMessageBubble
import com.example.gentecomoagente.ui.components.TypingIndicator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatClientScreen(navController: NavController) {
    // ESTADOS
    var inputText by remember { mutableStateOf("") }

    // Lista de mensagens inicial (simulando um histórico)
    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                "Olá, Gustavo. Tudo bem? Vou te ajudar com o seu problema de acesso.",
                isFromClient = false
            ),
            ChatMessage("Estou com um problema ao acessar o site de vendas. Toda vez que clico em 'carrinho' a página atualiza e eu perco os produtos selecionados.", isFromClient = true)
        )
    }

    // Estado para simular se o atendente está digitando
    var isAgentTyping by remember { mutableStateOf(true) }

    // Controle de Scroll da lista
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Container Principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // --- 1. CABEÇALHO ---
        Column(modifier = Modifier.padding(16.dp)) {
            Button(
                onClick = { navController.popBackStack() }, // Volta para a tela anterior
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEEEEEE),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Voltar")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Voltar")
            }
        }

        Divider(color = Color(0xFF81D4FA), thickness = 1.dp) // Linha azul clara

        // --- 2. ÁREA DE CHAT (Ocupa o espaço restante) ---
        Box(
            modifier = Modifier
                .weight(1f) // Faz essa caixa empurrar o rodapé para baixo
                .padding(16.dp)
                .background(Color(0xFFF5F5F5)) // Fundo cinza claro
                .border(1.dp, Color.Black) // Borda preta
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp) // Espaço entre as mensagens
            ) {
                // Renderiza todas as mensagens da lista
                items(messages) { msg ->
                    ChatMessageBubble(message = msg)
                }

                // Renderiza o indicador de digitação se for verdadeiro
                if (isAgentTyping) {
                    item { TypingIndicator() }
                }
            }
        }

        // --- 3. RODAPÉ (Área de Envio) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Campo de Texto
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

            // Botão Enviar
            Button(
                onClick = {
                    if (inputText.isNotBlank()) {
                        // 1. Adiciona a mensagem na lista
                        messages.add(ChatMessage(text = inputText, isFromClient = false))
                        // 2. Limpa o campo
                        inputText = ""
                        // 3. Rola a tela para o final
                        coroutineScope.launch {
                            listState.animateScrollToItem(messages.size)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50), // Verde
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(56.dp) // Altura igual a do TextField
            ) {
                Text("Enviar")
            }
        }
    }
}