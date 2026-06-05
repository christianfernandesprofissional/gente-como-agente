package com.example.gentecomoagente.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

data class TicketModel(
    @get:Exclude @set:Exclude
    var id: String = "",

    val customerName: String = "",
    val customerEmail: String = "",

    val problemType: String = "",

    val status: String = "OPEN",
    val createdAt: Timestamp? = null,

    val assignedAgentId: String? = null,

    val lastMessage: String = "",
    val lastMessageAt: Timestamp? = null,

    val accessCode: String = ""
)