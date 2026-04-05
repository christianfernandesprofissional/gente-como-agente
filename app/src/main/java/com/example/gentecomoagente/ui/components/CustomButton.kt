package com.example.gentecomoagente.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color(0xFFEEEEEE),
    contentColor: Color = Color.Black,
    // --- NOVOS PARÂMETROS ADICIONADOS AQUI ---
    shape: Shape = RoundedCornerShape(8.dp), // Padrão é arredondado, mas aceita quadrado agora
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding, // Aceita mudar o espaçamento interno
    elevation: Dp = 0.dp, // Aceita colocar sombra
    fontSize: TextUnit = 14.sp, // Aceita mudar o tamanho da letra
    fontWeight: FontWeight = FontWeight.SemiBold // Aceita tirar o negrito se precisar
) {
    Button(
        onClick = onClick,
        // Mudamos de height() para heightIn() para permitir que o botão seja menorzinho se a tela pedir
        modifier = modifier.heightIn(min = 28.dp),
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = elevation),
        contentPadding = contentPadding
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = fontWeight
        )
    }
}