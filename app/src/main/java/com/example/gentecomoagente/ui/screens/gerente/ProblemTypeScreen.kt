package com.example.gentecomoagente.ui.screens.gerente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.gentecomoagente.model.ProblemTypeModel
import com.example.gentecomoagente.repository.ProblemTypeRepository
import com.example.gentecomoagente.ui.components.CustomButton
import com.example.gentecomoagente.ui.components.CustomTextField
import com.example.gentecomoagente.ui.components.CustomTopHeader
import java.util.UUID

@Composable
fun ProblemTypeScreen(navController: NavController) {
    val repository = remember { ProblemTypeRepository() }
    
    // 1. ESTADOS (Lista dinâmica e campos de texto)
    val problemTypes = remember { mutableStateListOf<ProblemTypeModel>() }

    var novoNome by remember { mutableStateOf("") }
    var novaDescricao by remember { mutableStateOf("") }

    // Carregar dados iniciais do Firestore
    LaunchedEffect(Unit) {
        repository.findAll(
            onSuccess = { list ->
                problemTypes.clear()
                problemTypes.addAll(list)
            },
            onError = { /* Tratar erro se necessário */ }
        )
    }

    // Container Principal (Fundo Branco)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        // --- 2. CABEÇALHO (Refatorado com CustomTopHeader!) ---
        CustomButton(
            text = "Voltar",
            onClick = { navController.popBackStack() },
            shape = RoundedCornerShape(15),
            containerColor = Color(0xFFE0E0E0),
            contentColor = Color.Black
        )

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
                        repository.delete(
                            id = type.id,
                            onSuccess = { problemTypes.remove(type) },
                            onError = { /* Tratar erro */ }
                        )
                    }
                )
            }
        }

        // --- 4. ÁREA DE CADASTRO (Rodapé Fixo) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
                .navigationBarsPadding(),
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
                        val novoTipo = ProblemTypeModel(
                            name = novoNome,
                            description = novaDescricao
                        )
                        
                        repository.create(
                            problemType = novoTipo,
                            onSuccess = {
                                // Recarrega a lista para pegar o ID gerado ou simplesmente limpa campos
                                repository.findAll(
                                    onSuccess = { list ->
                                        problemTypes.clear()
                                        problemTypes.addAll(list)
                                    },
                                    onError = {}
                                )
                                novoNome = ""
                                novaDescricao = ""
                            },
                            onError = { /* Tratar erro */ }
                        )
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
                    text = problemType.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = problemType.description,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // LADO DIREITO: Botão Excluir
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
