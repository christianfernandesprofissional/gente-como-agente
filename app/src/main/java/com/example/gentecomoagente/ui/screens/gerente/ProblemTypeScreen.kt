package com.example.gentecomoagente.ui.screens.gerente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gentecomoagente.model.ProblemTypeModel
import com.example.gentecomoagente.ui.components.CustomButton
import com.example.gentecomoagente.ui.components.CustomTextField
import java.util.UUID

@Composable
fun ProblemTypeScreen(navController: NavController) {
    // 1. ESTADOS (Lista dinâmica e campos de texto)
    val problemTypes = remember {
        mutableStateListOf(
            ProblemTypeModel("1", "Suporte Técnico", "Resolução de falhas e auxílio tecnológico"),
            ProblemTypeModel("2", "Vendas", "Conversão de leads e novos pedidos"),
            ProblemTypeModel("3", "Cobranças", "Negociação de débitos e faturas pendentes")
        )
    }

    var novoNome by remember { mutableStateOf("") }
    var novaDescricao by remember { mutableStateOf("") }

    // Container Principal (Fundo Branco)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // --- 2. CABEÇALHO ---
        // Botão Voltar (Ícone + Texto, Retangular)
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(48.dp),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE0E0E0), // Cinza claro
                contentColor = Color.Black
            ),
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Voltar")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Voltar", fontWeight = FontWeight.Medium)
        }

        // Linhas divisórias
        Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
        Divider(color = Color(0xFF81D4FA), thickness = 1.dp)

        // Identificação do Gerente
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Gerente: André",
                fontSize = 16.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // --- 3. LISTA DE TIPOS DE PROBLEMA ---
        // O weight(1f) faz a lista empurrar o formulário lá para o final da tela!
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(problemTypes) { type ->
                ProblemTypeCard(
                    problemType = type,
                    onDelete = {
                        // Lógica real de exclusão da lista
                        problemTypes.remove(type)
                    }
                )
            }
        }

        // --- 4. ÁREA DE CADASTRO (Rodapé Fixo) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                label = "Nome do Tipo de Problema",
                value = novoNome,
                onValueChange = { novoNome = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            CustomTextField(
                label = "Descrição",
                value = novaDescricao,
                onValueChange = { novaDescricao = it },
                singleLine = false,
                minLines = 2
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(
                text = "Cadastrar Tipo de Problema",
                onClick = {
                    if (novoNome.isNotBlank() && novaDescricao.isNotBlank()) {
                        // Adiciona na lista
                        problemTypes.add(
                            ProblemTypeModel(
                                id = UUID.randomUUID().toString(),
                                nome = novoNome,
                                descricao = novaDescricao
                            )
                        )
                        // Limpa os campos
                        novoNome = ""
                        novaDescricao = ""
                    }
                },
                containerColor = Color(0xFFBDBDBD), // Cinza médio
                contentColor = Color.Black,
                modifier = Modifier.fillMaxWidth(0.8f) // Ocupa 80% da largura
            )
        }
    }
}

// --- COMPONENTE: CARD DO TIPO DE PROBLEMA ---
@Composable
fun ProblemTypeCard(problemType: ProblemTypeModel, onDelete: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5)) // Fundo cinza claro
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top // Garante que os botões fiquem no topo direito
        ) {
            // LADO ESQUERDO: Textos
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = problemType.nome,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = problemType.descricao,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // LADO DIREITO: Botões Editar e Excluir
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CustomButton(
                    text = "Editar",
                    onClick = { /* Lógica de edição */ },
                    containerColor = Color(0xFF1976D2), // Azul
                    contentColor = Color.White,
                    fontSize = 12.sp,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                    modifier = Modifier.heightIn(min = 32.dp)
                )

                CustomButton(
                    text = "Excluir",
                    onClick = onDelete, // Chama a função que remove da lista
                    containerColor = Color(0xFFD32F2F), // Vermelho
                    contentColor = Color.White,
                    fontSize = 12.sp,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                    modifier = Modifier.heightIn(min = 32.dp)
                )
            }
        }
    }
}