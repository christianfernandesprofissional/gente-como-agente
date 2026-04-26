package com.example.gentecomoagente.ui.screens.gerente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import com.example.gentecomoagente.model.AgentModel
import com.example.gentecomoagente.ui.components.CustomButton
import com.example.gentecomoagente.ui.navigation.Routes


@Composable
fun GerenteHomeScreen(navController: NavController) {
    // 1. DADOS FALSOS
    val agentes = remember {
        listOf(
            AgentModel(
                id = "1",
                username = "Gustavo",
                email = "gustavo.suporte@empresa.com",
                role = "agent",
                isActive = true
            ),
            AgentModel(
                id = "2",
                username = "Maria",
                email = "maria.vendas@empresa.com",
                role = "agent",
                isActive = true
            ),
            AgentModel(
                id = "3",
                username = "João",
                email = "joao.cobranca@empresa.com",
                role = "agent",
                isActive = true
            ),
            AgentModel(
                id = "4",
                username = "Ana",
                email = "ana.credito@empresa.com",
                role = "agent",
                isActive = true
            ),
            AgentModel(
                id = "5",
                username = "Carlos",
                email = "carlos.suporte@empresa.com",
                role = "agent",
                isActive = false
            )
        )
    }

    Scaffold(
        containerColor = Color.White,

        // --- 2. CABEÇALHO FIXO (TopBar) ---
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // 2.1 Botões Superiores (Refatorados com CustomButton)
                Row(modifier = Modifier.fillMaxWidth()) {
                    CustomButton(
                        text = "Sair",
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RectangleShape,
                        containerColor = Color(0xFFE0E0E0),
                        contentColor = Color.Black
                    )

                    Spacer(modifier = Modifier.width(2.dp))

                    CustomButton(
                        text = "Tipos de Problema",
                        onClick = { navController.navigate(Routes.PROBLEM_TYPE)},
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RectangleShape,
                        containerColor = Color(0xFFE0E0E0),
                        contentColor = Color.Black
                    )
                }

                // 2.2 Título e Subtítulo
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                    Text(text = "Agentes", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Gerente: Aroldo", fontSize = 16.sp, color = Color.DarkGray)
                }

                // 2.3 Linha divisória
                Divider(color = Color(0xFF00BCD4), thickness = 1.dp)
            }
        },

        // --- 4. RODAPÉ FIXO (BottomBar) ---
        bottomBar = {
            // Botões Inferiores (Refatorados com CustomButton)
            Row(modifier = Modifier.fillMaxWidth()) {
                CustomButton(
                    text = "Visualizar Tickets",
                    onClick = { navController.navigate(Routes.GERENTE_TICKETS) },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RectangleShape,
                    containerColor = Color(0xFFE0E0E0),
                    contentColor = Color.Black
                )

                Spacer(modifier = Modifier.width(2.dp))

                CustomButton(
                    text = "Cadastrar Novo Agente",
                    onClick = { navController.navigate(Routes.AGENT_CREATION) },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RectangleShape,
                    containerColor = Color(0xFFE0E0E0),
                    contentColor = Color.Black
                )
            }
        }
    ) { paddingValues ->
        // --- 3. CORPO DA TELA (Lista Rolável) ---
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(agentes) { agente ->
                AgentListItem(agente = agente)
            }
        }
    }
}

// --- COMPONENTE: CARD DO AGENTE ---
@Composable
fun AgentListItem(agente: AgentModel) {

    val statusText = if (agente.isActive) "Ativo" else "Inativo"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🔹 LADO ESQUERDO
            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = agente.username,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = agente.email,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // 🔹 LADO DIREITO
            Column(horizontalAlignment = Alignment.End) {

                // Linha superior
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {

                    CustomButton(
                        text = "Editar",
                        onClick = { /* TODO */ },
                        containerColor = Color(0xFF1976D2),
                        contentColor = Color.White,
                        fontSize = 11.sp,
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        elevation = 2.dp,
                        shape = RoundedCornerShape(4.dp)
                    )

                    CustomButton(
                        text = "Excluir",
                        onClick = { /* TODO */ },
                        containerColor = Color(0xFFD32F2F),
                        contentColor = Color.White,
                        fontSize = 11.sp,
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        elevation = 2.dp,
                        shape = RoundedCornerShape(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 🔹 Role (substitui "setor")
                CustomButton(
                    text = agente.role,
                    onClick = { /* opcional */ },
                    modifier = Modifier.fillMaxWidth(0.5f),
                    containerColor = Color(0xFF388E3C),
                    contentColor = Color.White,
                    fontSize = 11.sp,
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    elevation = 2.dp,
                    shape = RoundedCornerShape(4.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 🔹 Status
                Text(
                    text = statusText,
                    fontSize = 12.sp,
                    color = if (agente.isActive) Color(0xFF2E7D32) else Color.Red
                )
            }
        }
    }
}
