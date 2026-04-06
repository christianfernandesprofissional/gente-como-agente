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


@Composable
fun GerenteHomeScreen(navController: NavController) {
    // 1. DADOS FALSOS
    val agentes = remember {
        listOf(
            AgentModel("1", "Gustavo", "gustavo.suporte@empresa.com", "Suporte Técnico"),
            AgentModel("2", "Maria", "maria.vendas@empresa.com", "Vendas"),
            AgentModel("3", "João", "joao.cobranca@empresa.com", "Cobranças"),
            AgentModel("4", "Ana", "ana.credito@empresa.com", "Análise de Crédito"),
            AgentModel("5", "Carlos", "carlos.suporte@empresa.com", "Suporte Técnico")
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
                        onClick = { /* Ação */ },
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
                    onClick = { /* Ação */ },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RectangleShape,
                    containerColor = Color(0xFFE0E0E0),
                    contentColor = Color.Black
                )

                Spacer(modifier = Modifier.width(2.dp))

                CustomButton(
                    text = "Cadastrar Novo Agente",
                    onClick = { /* Ação */ },
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
            // LADO ESQUERDO: Informações
            Column(modifier = Modifier.weight(1f)) {
                Text(text = agente.nome, fontSize = 16.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = agente.email, fontSize = 14.sp, color = Color.Black)
            }

            Spacer(modifier = Modifier.width(8.dp))

            // LADO DIREITO: Botões de Ação (Refatorados com CustomButton)
            Column(horizontalAlignment = Alignment.End) {
                // Linha superior: Editar e Excluir
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    CustomButton(
                        text = "Editar",
                        onClick = { /* Ação */ },
                        containerColor = Color(0xFF1976D2), // Azul
                        contentColor = Color.White,
                        fontSize = 11.sp,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                        elevation = 2.dp,
                        shape = RoundedCornerShape(4.dp)
                    )

                    CustomButton(
                        text = "Excluir",
                        onClick = { /* Ação */ },
                        containerColor = Color(0xFFD32F2F), // Vermelho
                        contentColor = Color.White,
                        fontSize = 11.sp,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                        elevation = 2.dp,
                        shape = RoundedCornerShape(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Linha inferior: Setor
                CustomButton(
                    text = agente.setor,
                    onClick = { /* Ação */ },
                    modifier = Modifier.fillMaxWidth(0.4f),
                    containerColor = Color(0xFF388E3C), // Verde
                    contentColor = Color.White,
                    fontSize = 11.sp,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                    elevation = 2.dp,
                    shape = RoundedCornerShape(4.dp)
                )
            }
        }
    }
}

