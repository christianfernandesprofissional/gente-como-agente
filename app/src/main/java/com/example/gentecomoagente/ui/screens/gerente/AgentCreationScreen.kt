package com.example.gentecomoagente.ui.screens.gerente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gentecomoagente.ui.components.CustomButton
import com.example.gentecomoagente.ui.components.CustomTextField
import com.example.gentecomoagente.ui.components.CustomTopHeader


@Composable
fun AgentCreationScreen(navController: NavController) {
    // 1. ESTADOS (Variáveis para guardar o que o usuário digitar)
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    // Container Principal (Fundo Branco)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // --- 2. CABEÇALHO (Refatorado com CustomTopHeader!) ---
        CustomTopHeader(
            buttonText = "Voltar",
            buttonIcon = Icons.Default.ArrowBack,
            onClickButton = { navController.popBackStack() }
        )

        // --- 3. FORMULÁRIO CENTRALIZADO ---
        // A Box com weight(1f) ocupa todo o espaço restante da tela.
        // O contentAlignment = Alignment.Center joga o conteúdo pro meio exato.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            // Column interna para organizar os campos um embaixo do outro
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp), // Margens laterais do formulário
                horizontalAlignment = Alignment.CenterHorizontally // Centraliza o botão no final
            ) {

                // Campo 1: Nome
                CustomTextField(
                    label = "Nome do Agente",
                    value = nome,
                    onValueChange = { nome = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo 2: E-mail
                CustomTextField(
                    label = "E-mail",
                    value = email,
                    onValueChange = { email = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo 3: Senha (com máscara)
                CustomTextField(
                    label = "Senha",
                    value = senha,
                    onValueChange = { senha = it },
                    visualTransformation = PasswordVisualTransformation(), // Transforma em bolinhas
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Botão de Cadastro
                CustomButton(
                    text = "Salvar Agente",
                    onClick = {
                        // Lógica de cadastro viria aqui
                        println("Cadastrando: $nome, $email")
                    },
                    modifier = Modifier.fillMaxWidth(0.6f), // Ocupa 60% da largura (menor que os campos)
                    containerColor = Color(0xFFBDBDBD), // Cinza médio
                    contentColor = Color.Black
                )
            }
        }
    }
}
