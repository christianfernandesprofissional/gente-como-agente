package com.example.gentecomoagente.ui.screens.gerente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gentecomoagente.model.TicketModel
import com.example.gentecomoagente.ui.components.CustomButton
import com.example.gentecomoagente.ui.components.CustomTopHeader


@Composable
fun GerenteTicketScreen(navController: NavController) {
    // 1. DADOS FALSOS
    val tickets = remember {
        listOf(
            TicketModel("1", "", "", "Área do carrinho com problema", "Em andamento", "Visualizar"),
            TicketModel("2", "", "", "Dúvida sobre estorno", "Não iniciado", "Conversar"),
            TicketModel("3", "", "", "Site fora do ar", "Finalizado", "Visualizar"),
            TicketModel("4", "", "", "Erro ao aplicar cupom", "Não iniciado", "Conversar")
        )
    }

    // Container Principal (Fundo Branco)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // --- 2. CABEÇALHO (Refatorado com CustomTopHeader) ---
        // Envolvemos em uma Box com offset negativo para anular o padding padrão do componente
        // e deixar o botão colado na borda esquerda, como no design original.
        Box(modifier = Modifier.offset(x = (-16).dp)) {
            CustomTopHeader(
                buttonText = "Sair", // Mudei para Sair conforme a especificação original
                onClickButton = { navController.popBackStack() },
                buttonWidthFraction = 0.45f // Ajustado para compensar o offset
            )
        }

        // --- 3. IDENTIFICAÇÃO DO USUÁRIO ---
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Gerente: André",
                fontSize = 18.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- 4. LISTA DE TICKETS ---
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(tickets) { ticket ->
                    GerenteTicketCard(ticket = ticket)
                }
            }
        }
    }
}

// --- COMPONENTE: CARD DO TICKET ---
@Composable
fun GerenteTicketCard(ticket: TicketModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // Problema
            Text(
                text = "Problema: ${ticket.problema}",
                fontSize = 15.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Linha inferior: Status + Botão
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                // Status
                Text(
                    text = "Atendimento: ${ticket.statusAtendimento}",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Botão de Ação (Já estava usando o CustomButton corretamente!)
                CustomButton(
                    text = ticket.textoBotaoAcao,
                    onClick = { /* Ação baseada no status */ },
                    containerColor = Color(0xFF4CAF50), // Verde
                    contentColor = Color.White,
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    modifier = Modifier.height(36.dp)
                )
            }
        }
    }
}