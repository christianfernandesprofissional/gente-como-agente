package com.example.gentecomoagente.model

import com.google.firebase.Timestamp

data class TicketModel(
    val customerName: String = "",
    val customerEmail: String = "",

    val problemType: String = "", // 👈 agora é só String

    val status: String = "OPEN",
    val createdAt: Timestamp? = null,

    val assignedAgentId: String? = null,

    val lastMessage: String = "",
    val lastMessageAt: Timestamp? = null,

    val accessCode: String = ""
)