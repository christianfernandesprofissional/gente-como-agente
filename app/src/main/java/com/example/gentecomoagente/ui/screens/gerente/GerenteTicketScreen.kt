package com.example.gentecomoagente.ui.screens.gerente

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

@Composable
fun GerenteTicketScreen(navController: NavController) {
    // 1. DADOS FALSOS (Simulando o banco de dados)
    // Usando o mesmo TicketModel que já criamos antes!
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
        // --- 2. CABEÇALHO ---
        // Botão Sair (Canto superior esquerdo, retangular)
        CustomButton(
            text = "Voltar",
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth(0.4f) // Ocupa 40% da largura
                .height(48.dp),
            shape = RectangleShape, // Formato retangular exigido
            containerColor = Color(0xFFE0E0E0),
            contentColor = Color.Black
        )

        // Linhas divisórias (Neutra e Azul Clara)
        Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
        Divider(color = Color(0xFF81D4FA), thickness = 1.dp)

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
                verticalArrangement = Arrangement.spacedBy(16.dp) // Espaço entre os cards
            ) {
                items(tickets) { ticket ->
                    GerenteTicketCard(ticket = ticket)
                }
            }
        }
    }
}

// --- COMPONENTE: CARD DO TICKET (Específico desta tela) ---
@Composable
fun GerenteTicketCard(ticket: TicketModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5)) // Fundo cinza claro
            .padding(16.dp) // Padding interno do card
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
                horizontalArrangement = Arrangement.SpaceBetween, // Separa o texto do botão
                verticalAlignment = Alignment.Bottom // Alinha por baixo
            ) {
                // Status
                Text(
                    text = "Atendimento: ${ticket.statusAtendimento}",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.weight(1f) // Evita que o texto empurre o botão
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Botão de Ação (Verde, arredondado)
                CustomButton(
                    text = ticket.textoBotaoAcao,
                    onClick = { /* Ação baseada no status */ },
                    containerColor = Color(0xFF4CAF50), // Verde
                    contentColor = Color.White,
                    shape = RoundedCornerShape(6.dp), // Bordas arredondadas
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    modifier = Modifier.height(36.dp) // Botão mais compacto
                )
            }
        }
    }
}