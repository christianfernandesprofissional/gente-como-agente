package com.example.gentecomoagente.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gentecomoagente.ui.components.CustomButton
import com.example.gentecomoagente.ui.components.CustomDropdown
import com.example.gentecomoagente.ui.components.CustomTextField
import com.example.gentecomoagente.ui.navigation.Routes


@Composable
fun HomeScreen(navController: NavController) {
    // ESTADOS
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }

    val opcoesProblema = listOf("Análise de Crédito", "Problema Técnico", "Dúvida Financeira")
    var problemaSelecionado by remember { mutableStateOf(opcoesProblema[0]) }

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
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                    CustomButton(
                        text = "Acessar como Funcionário",
                        onClick = { navController.navigate(Routes.LOGIN) }
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
                    onValueChange = { email = it }
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
                    CustomButton(
                        text = "Ticket Existente",
                        onClick = { navController.navigate(Routes.TICKET_EXISTENTE) },
                        modifier = Modifier.weight(1f)
                    )

                    CustomButton(
                        text = "Iniciar Atendimento",
                        onClick = { navController.navigate(Routes.CHAT_CLIENT) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

