package com.example.gentecomoagente.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gentecomoagente.model.ChatMessage

@Composable
fun ChatMessageBubble(message: ChatMessage) {

    val isFromClient = message.senderType == "CLIENT"

    val backgroundColor = if (isFromClient) Color(0xFF00838F) else Color(0xFFE0F7FA)
    val textColor = if (isFromClient) Color.White else Color.Black
    val alignment = if (isFromClient) Arrangement.End else Arrangement.Start

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = alignment
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .wrapContentWidth(
                    if (isFromClient) Alignment.End else Alignment.Start
                )
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(
                text = message.content,
                color = textColor,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun TypingIndicator() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End // Alinhado à direita conforme especificação
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = Color(0xFF00838F).copy(alpha = 0.7f), // Mesma cor do cliente, mas um pouco transparente
                    shape = RoundedCornerShape(50) // Formato de pílula
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "Escrevendo...",
                color = Color.White,
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic // Texto em itálico
            )
        }
    }
}