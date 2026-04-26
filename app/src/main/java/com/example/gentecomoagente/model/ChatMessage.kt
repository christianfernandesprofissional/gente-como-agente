package com.example.gentecomoagente.model

import com.google.firebase.Timestamp

data class ChatMessage(
    val senderId: String = "",
    val senderType: String = "", // "AGENT" ou "CLIENT"
    val content: String = "",
    val timestamp: Timestamp? = null
)