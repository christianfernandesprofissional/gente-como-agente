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

    fun findTicketsByCustomerEmail(
        email: String,
        onSuccess: (List<TicketModel>) -> Unit,
        onError: (String) -> Unit
    ) {

        collection
            .whereEqualTo("customerEmail", email)
            .get()

            .addOnSuccessListener { result ->

                val tickets = result.documents.mapNotNull { document ->

                    TicketModel(
                        customerName = document.getString("customerName") ?: "",
                        customerEmail = document.getString("customerEmail") ?: "",
                        problemType = document.getString("problemType") ?: "",
                        status = document.getString("status") ?: "OPEN",
                        accessCode = document.getString("accessCode") ?: "",
                        assignedAgentId = document.getString("assignedAgentId"),
                        lastMessage = document.getString("lastMessage") ?: "",
                        createdAt = document.getTimestamp("createdAt"),
                        lastMessageAt = document.getTimestamp("lastMessageAt")
                    )
                }

                onSuccess(tickets)
            }

            .addOnFailureListener { e ->

                onError(
                    e.message ?: "Erro ao buscar tickets"
                )
            }
    }
}
