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


@Composable
fun GerenteHomeScreen(navController: NavController) {
    // 1. DADOS FALSOS (Simulando o banco de dados)
    val agentes = remember {
        listOf(
            AgentModel("1", "Gustavo", "gustavo.suporte@empresa.com", "Suporte Técnico"),
            AgentModel("2", "Maria", "maria.vendas@empresa.com", "Vendas"),
            AgentModel("3", "João", "joao.cobranca@empresa.com", "Cobranças"),
            AgentModel("4", "Ana", "ana.credito@empresa.com", "Análise de Crédito"),
            AgentModel("5", "Carlos", "carlos.suporte@empresa.com", "Suporte Técnico")
        )
    }

    // O Scaffold é a estrutura que segura o Topo e o Rodapé fixos!
    Scaffold(
        containerColor = Color.White, // Fundo totalmente branco

        // --- 2. CABEÇALHO FIXO (TopBar) ---
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // 2.1 Botões Superiores (50/50)
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.weight(1f).height(48.dp), // weight(1f) divide o espaço
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0), contentColor = Color.Black)
                    ) { Text("Sair") }

                    Spacer(modifier = Modifier.width(2.dp)) // Fenda branca entre eles

                    Button(
                        onClick = { /* Ação */ },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0), contentColor = Color.Black)
                    ) { Text("Tipos de Problema") }
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
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { /* Ação */ },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0), contentColor = Color.Black)
                ) { Text("Visualizar Tickets", fontWeight = FontWeight.Bold) }

                Spacer(modifier = Modifier.width(2.dp)) // Fenda branca

                Button(
                    onClick = { /* Ação */ },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0), contentColor = Color.Black)
                ) { Text("Cadastrar Novo Agente", fontWeight = FontWeight.Bold) }
            }
        }
    ) { paddingValues ->
        // --- 3. CORPO DA TELA (Lista Rolável) ---
        // O paddingValues garante que a lista não fique escondida atrás do TopBar ou BottomBar
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Aplica o respiro do Scaffold
                .padding(horizontal = 16.dp), // Margem lateral da lista
            contentPadding = PaddingValues(vertical = 16.dp), // Espaço no topo e fim da lista
            verticalArrangement = Arrangement.spacedBy(12.dp) // Espaço entre os cards
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
            .background(Color(0xFFF5F5F5)) // Fundo cinza claro
            .padding(12.dp) // Padding interno compacto
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween, // Separa Esquerda e Direita
            verticalAlignment = Alignment.CenterVertically
        ) {
            // LADO ESQUERDO: Informações
            Column(modifier = Modifier.weight(1f)) { // weight(1f) empurra os botões pra direita
                Text(text = agente.nome, fontSize = 16.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = agente.email, fontSize = 14.sp, color = Color.Black)
            }

            Spacer(modifier = Modifier.width(8.dp))

            // LADO DIREITO: Botões de Ação
            Column(horizontalAlignment = Alignment.End) {
                // Linha superior: Editar e Excluir
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    SmallActionButton(text = "Editar", color = Color(0xFF1976D2)) // Azul
                    SmallActionButton(text = "Excluir", color = Color(0xFFD32F2F)) // Vermelho
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Linha inferior: Setor
                SmallActionButton(
                    text = agente.setor,
                    color = Color(0xFF388E3C), // Verde
                    modifier = Modifier.fillMaxWidth(0.4f) // Faz o botão verde ocupar um tamanho legal
                )
            }
        }
    }
}

// --- COMPONENTE AUXILIAR: BOTÃO PEQUENO COLORIDO ---
@Composable
fun SmallActionButton(text: String, color: Color, modifier: Modifier = Modifier) {
    Button(
        onClick = { /* Ação */ },
        modifier = modifier.height(28.dp), // Altura bem pequena
        shape = RoundedCornerShape(4.dp), // Levemente arredondado
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.White
        ),
        // Reduz o padding interno padrão do Android para caber o texto no botão pequeno
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp) // Leve sombra/relevo
    ) {
        Text(text = text, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}