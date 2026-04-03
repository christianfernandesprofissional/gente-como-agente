package com.example.gentecomoagente.model

data class ChatMessage(
    val text: String,
    val isFromAgent: Boolean // true = Cliente (Direita), false = Agente (Esquerda)
)