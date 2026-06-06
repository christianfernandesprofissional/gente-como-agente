package com.example.gentecomoagente.repository

import android.util.Log
import com.example.gentecomoagente.model.ChatMessage
import com.example.gentecomoagente.model.TicketModel
import com.google.firebase.firestore.FirebaseFirestore
import java.security.MessageDigest

class TicketRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("tickets")


    private fun generateHash(input: String): String {
        return MessageDigest.getInstance("SHA-256")
            .digest(input.toByteArray())
            .joinToString("") { "%02x".format(it) }
            .take(8)
            .uppercase()
    }

    fun createTicket(
        ticket: TicketModel,
        initialMessage: String,
        onSuccess: (String, String) -> Unit, // ticketId, accessCode
        onError: (String) -> Unit
    ) {
        val counterRef = db.collection("metadata").document("ticket_counter")

        db.runTransaction { transaction ->
            Log.d("TicketRepository", "Iniciando transação para criar ticket")
            // 1. Obter o contador atual (ou iniciar em 0 se não existir)
            val snapshot = transaction.get(counterRef)
            val nextId = (snapshot.getLong("lastId") ?: 0L) + 1
            Log.d("TicketRepository", "Próximo ID sequencial: $nextId")

            // 2. Preparar o ID sequencial
            val sequentialId = "TCK-$nextId"
            
            // 3. Gerar código de acesso (Hash baseado no ID, Email e Timestamp)
            val timestamp = ticket.createdAt?.toDate()?.time ?: System.currentTimeMillis()
            val rawString = "$sequentialId-${ticket.customerEmail}-$timestamp"
            val accessCode = generateHash(rawString)
            
            val ticketWithData = ticket.copy(accessCode = accessCode)

            // 4. Criar o documento do ticket com o ID personalizado
            val ticketRef = collection.document(sequentialId)
            transaction.set(ticketRef, ticketWithData)
            Log.d("TicketRepository", "Ticket document set: $sequentialId")

            // 5. Criar a primeira mensagem na subcoleção "messages"
            val messageRef = ticketRef.collection("messages").document()
            val messageData = mapOf(
                "content" to initialMessage,
                "senderId" to ticket.customerEmail,
                "senderType" to "CLIENT",
                "timestamp" to com.google.firebase.Timestamp.now()
            )
            transaction.set(messageRef, messageData)
            Log.d("TicketRepository", "Primeira mensagem setada")

            // 6. Atualizar ou Criar o contador
            transaction.set(counterRef, mapOf("lastId" to nextId))
            Log.d("TicketRepository", "Contador atualizado para $nextId")

            Pair(sequentialId, accessCode)
        }.addOnSuccessListener { result ->
            Log.d("TicketRepository", "Transação concluída com sucesso: ${result.first}")
            onSuccess(result.first, result.second)
        }.addOnFailureListener { e ->
            Log.e("TicketRepository", "Falha na transação: ${e.message}", e)
            onError(e.message ?: "Erro ao criar ticket")
        }
    }

    fun listenToMessages(ticketId: String, onUpdate: (List<com.example.gentecomoagente.model.ChatMessage>) -> Unit) {
        collection.document(ticketId).collection("messages")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) return@addSnapshotListener
                
                val messages = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(com.example.gentecomoagente.model.ChatMessage::class.java)
                }
                onUpdate(messages)
            }
    }

    fun sendMessage(
        ticketId: String,
        message: ChatMessage,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        val batch = db.batch()

        val ticketRef = collection.document(ticketId)
        val messageRef = ticketRef.collection("messages").document()

        // 1. salvar mensagem
        batch.set(messageRef, message)

        // 2. atualizar ticket
        batch.update(ticketRef, mapOf(
            "lastMessage" to message.content,
            "lastMessageAt" to message.timestamp
        ))

        // 3. executar tudo junto
        batch.commit()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e ->
                onError(e.message ?: "Erro ao enviar mensagem")
            }
    }

    fun getTicket(ticketId: String, onSuccess: (TicketModel?) -> Unit, onError: (String) -> Unit) {
        collection.document(ticketId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val ticket = document.toObject(TicketModel::class.java)
                    onSuccess(ticket?.copy(id = document.id))
                } else {
                    onSuccess(null)
                }
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Erro ao buscar ticket")
            }
    }

    fun updateTicketStatus(
        ticketId: String,
        newStatus: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        collection.document(ticketId)
            .update("status", newStatus)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Erro ao atualizar status")
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
                        id = document.id,
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
