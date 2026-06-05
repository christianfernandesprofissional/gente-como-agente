package com.example.gentecomoagente.repository

import com.example.gentecomoagente.model.TicketModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class TicketRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("tickets")

    fun createTicket(
        ticket: TicketModel,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        // Gerar um código de acesso simples (Ex: 6 dígitos)
        val accessCode = (100000..999999).random().toString()
        val ticketWithCode = ticket.copy(accessCode = accessCode)

        collection.add(ticketWithCode)
            .addOnSuccessListener { documentReference ->
                onSuccess(accessCode)
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Erro ao criar ticket")
            }
    }
}
