package com.example.gentecomoagente.ui.screens

import android.text.Layout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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


@Composable
    fun HomeScreen(navController: NavController) {
        // ESTADOS (A memória da tela)
        var nome by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var descricao by remember { mutableStateOf("") }

        val opcoesProblema = listOf("Análise de Crédito", "Problema Técnico", "Dúvida Financeira")
        var problemaSelecionado by remember { mutableStateOf(opcoesProblema[0]) }

        // 1. Fundo escuro ao redor
        Box(
            modifier = Modifier
                .fillMaxSize()
                //.background(Color(0xFF121212)) // Fundo escuro
                .padding(1.dp), // Margens laterais pequenas
            contentAlignment = Alignment.Center
        ) {
            // 2. O Card Branco Central
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
                    // --- CABEÇALHO ---
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                        CustomButton(
                            text = "Acessar como Funcionário",
                            onClick = { /* Futura navegação */ }
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
                        minLines = 4 // Textarea maior
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // --- RODAPÉ (Botões Lado a Lado) ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp) // Espaço entre eles
                    ) {
                        CustomButton(
                            text = "Ticket Existente",
                            onClick = { /* Ação */ },
                            modifier = Modifier.weight(1f) // Faz ocupar metade do espaço
                        )

                        CustomButton(
                            text = "Iniciar Atendimento",
                            onClick = { /* Ação */ },
                            modifier = Modifier.weight(1f) // Faz ocupar a outra metade
                        )
                    }
                }
            }
        }
    }

