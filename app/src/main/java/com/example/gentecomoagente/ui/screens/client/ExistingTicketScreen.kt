package com.example.gentecomoagente.ui.screens.client

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gentecomoagente.ui.components.CustomBadge
import com.example.gentecomoagente.ui.components.CustomButton
import com.example.gentecomoagente.ui.components.CustomDropdown
import com.example.gentecomoagente.ui.components.CustomTextField
import com.example.gentecomoagente.ui.navigation.Routes

@Composable
fun ExistingTicketScreen(navController: NavController) {
    // ESTADOS
    var ticketNumber by remember { mutableStateOf("TKT-001234") }

    val opcoesProblema = listOf("Suporte Técnico", "Financeiro", "Dúvida Geral")
    var problemaSelecionado by remember { mutableStateOf(opcoesProblema[0]) }

    var descricao by remember {
        mutableStateOf(
            "Estou com um problema ao acessar o site de vendas. Toda vez que clico em 'carrinho' a página atualiza e eu perco os produtos selecionados."
        )
    }

    // Container Principal
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Centraliza o conteúdo verticalmente
        ) {

            // --- CABEÇALHO ESPECÍFICO DESTA TELA ---
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ) {
                CustomButton(
                    text = "Acessar como Funcionário",
                    onClick = { navController.navigate(Routes.LOGIN) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Inicie um novo atendimento ou informe o Ticket para continuar uma conversa",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Divider(color = Color(0xFF81D4FA), thickness = 1.dp)

            Spacer(modifier = Modifier.height(24.dp))

            // --- BUSCA DE TICKET ---
            CustomTextField(
                label = "Número do Ticket",
                value = ticketNumber,
                onValueChange = { ticketNumber = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            CustomButton(
                text = "Buscar Ticket",
                onClick = { /* Ação de busca */ }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- STATUS DO TICKET ---
            CustomBadge(
                text = "Problema Encontrado",
                backgroundColor = Color(0xFF4CAF50) // Verde
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- TIPO DE PROBLEMA ---
            CustomDropdown(
                label = "Tipo de Problema",
                options = opcoesProblema,
                selectedOption = problemaSelecionado,
                onOptionSelected = { problemaSelecionado = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- DESCRIÇÃO DO PROBLEMA ---
            CustomTextField(
                label = "Descrição do Problema",
                value = descricao,
                onValueChange = { descricao = it },
                singleLine = false,
                minLines = 5,
                readOnly = true // Campo travado para edição
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- RODAPÉ ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CustomButton(
                    text = "Novo Ticket",
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                )

                CustomButton(
                    text = "Visualizar ticket",
                    onClick = { /* Ação */ },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}