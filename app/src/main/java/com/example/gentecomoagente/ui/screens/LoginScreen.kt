package com.example.gentecomoagente.ui.screens

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
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // --- CABEÇALHO (Refatorado usando o componente novo!) ---
        CustomTopHeader(
            buttonText = "Voltar",
            buttonIcon = Icons.Default.ArrowBack,
            onClickButton = { navController.popBackStack() }
        )

        // --- CONTAINER QUE CENTRALIZA O FORMULÁRIO ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                CustomTextField(
                    label = "Usuário",
                    value = username,
                    onValueChange = { username = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                CustomTextField(
                    label = "Senha",
                    value = password,
                    onValueChange = { password = it },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Spacer(modifier = Modifier.height(40.dp))

                CustomButton(
                    text = "Entrar",
                    onClick = { /* Ação */ },
                    modifier = Modifier.fillMaxWidth(0.6f),
                    containerColor = Color(0xFFBDBDBD)
                )
            }
        }
    }
}