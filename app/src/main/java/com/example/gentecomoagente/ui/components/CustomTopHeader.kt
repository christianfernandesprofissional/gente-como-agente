package com.example.gentecomoagente.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CustomTopHeader(
    buttonText: String,
    onClickButton: () -> Unit,
    buttonIcon: ImageVector? = null,
    showDividers: Boolean = true, // Permite esconder as linhas se a tela não precisar
    buttonWidthFraction: Float = 0.45f // Ocupa 45% da tela por padrão
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Envolvemos o botão em um padding lateral (opcional dependendo do design, mas alinha bem)
        Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp)) {
            CustomButton(
                text = buttonText,
                onClick = onClickButton,
                icon = buttonIcon,
                modifier = Modifier
                    .fillMaxWidth(buttonWidthFraction)
                    .height(48.dp),
                shape = RectangleShape, // Formato retangular padrão do seu cabeçalho
                containerColor = Color(0xFFE0E0E0),
                contentColor = Color.Black,
                contentPadding = PaddingValues(horizontal = 12.dp)
            )
        }

        if (showDividers) {
            Divider(color = Color(0xFFEEEEEE), thickness = 1.dp) // Linha Neutra
            Divider(color = Color(0xFF81D4FA), thickness = 1.dp) // Linha Azul
        }
    }
}