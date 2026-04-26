package com.example.gentecomoagente.ui.screens.agent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gentecomoagente.model.TicketModel
import com.example.gentecomoagente.ui.components.CustomButton
import com.example.gentecomoagente.ui.navigation.Routes


@Composable
fun TicketsAgentScreen(navController: NavController) {
    // 1. DADOS FALSOS
    val tickets = remember {
        listOf(
            TicketModel(
                customerName = "Gustavo",
                customerEmail = "gustavo@email.com",
                problemType = "Suporte Técnico",
                status = "OPEN",
                accessCode = "123456"
            ),
            TicketModel(
                customerName = "Maria",
                customerEmail = "maria@email.com",
                problemType = "Financeiro",
                status = "IN_PROGRESS",
                accessCode = "654321"
            ),
            TicketModel(
                customerName = "João",
                customerEmail = "joao@email.com",
                problemType = "Vendas",
                status = "CLOSED",
                accessCode = "999999"
            )
        )
    }

    // Container Principal (Fundo Branco)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // --- 2. CABEÇALHO (Refatorado com CustomButton) ---
        CustomButton(
            text = "Sair",
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth(0.45f)
                .height(48.dp),
            shape = RectangleShape, // Formato de bloco reto
            containerColor = Color(0xFFE0E0E0),
            contentColor = Color.Black,
            fontWeight = FontWeight.Medium
        )

        // --- 3. CONTEÚDO COM PADDING LATERAL ---
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {

            Spacer(modifier = Modifier.height(24.dp))

            // Informações do Agente
            Text(
                text = "Tickets de Atendimento",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Agente: Fabio",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Linha divisória Azul Clara
            Divider(color = Color(0xFF00BCD4), thickness = 1.dp)

            Spacer(modifier = Modifier.height(16.dp))

            // --- 4. LISTA DE TICKETS ---
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(tickets) { ticket ->
                    TicketListItem(
                        ticket = ticket,
                        onActionClick = {
                            navController.navigate(Routes.CHAT_AGENT)
                        }
                    )
                }
            }
        }
    }
}

// --- COMPONENTE: ITEM DA LISTA DE TICKETS ---
@Composable
fun TicketListItem(ticket: TicketModel, onActionClick: () -> Unit) {

    val statusText = when (ticket.status) {
        "OPEN" -> "Aberto"
        "IN_PROGRESS" -> "Em andamento"
        "CLOSED" -> "Fechado"
        else -> ticket.status
    }

    val buttonText = if (ticket.status == "CLOSED") "Visualizar" else "Conversar"

    Column(modifier = Modifier.fillMaxWidth()) {

        // 🔹 Título
        Text(
            text = "${ticket.customerName} - ${ticket.problemType}",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5))
                .padding(12.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {

                // 🔹 Problema (agora é problemType)
                Text(
                    text = "Tipo: ${ticket.problemType}",
                    fontSize = 14.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {

                    Text(
                        text = "Status: $statusText",
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    CustomButton(
                        text = buttonText,
                        onClick = onActionClick,
                        modifier = Modifier.height(36.dp),
                        shape = RoundedCornerShape(4.dp),
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.White,
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

