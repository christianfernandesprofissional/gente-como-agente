package com.example.gentecomoagente.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
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

@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // --- CABEÇALHO ---
        Column(modifier = Modifier.padding(16.dp)) {
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEEEEEE),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Voltar")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Voltar")
            }
        }

        Divider(color = Color(0xFF81D4FA), thickness = 1.dp)

        // 👇 CONTAINER QUE CENTRALIZA
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