package com.example.gentecomoagente.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.example.gentecomoagente.model.AgentModel

class AgentRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("agents")

    // 🔹 CREATE
    fun createAgent(
        uid: String,
        agent: AgentModel,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        collection.document(uid)
            .set(agent)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e ->
                onError(e.message ?: "Erro ao criar agente")
            }
    }

    // 🔹 READ (buscar por ID)
    fun getAgentById(
        uid: String,
        onSuccess: (AgentModel?) -> Unit,
        onError: (String) -> Unit
    ) {
        collection.document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val agent = document.toObject(AgentModel::class.java)
                    onSuccess(agent)
                } else {
                    onSuccess(null)
                }
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Erro ao buscar agente")
            }
    }

    // 🔹 READ (listar todos)
    fun getAllAgents(
        onSuccess: (List<Pair<String, AgentModel>>) -> Unit,
        onError: (String) -> Unit
    ) {
        collection.get()
            .addOnSuccessListener { result ->
                val agents = result.documents.mapNotNull { doc ->
                    val agent = doc.toObject(AgentModel::class.java)
                    agent?.let { doc.id to it } // mantém UID junto
                }
                onSuccess(agents)
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Erro ao listar agentes")
            }
    }

    // 🔹 UPDATE
    fun updateAgent(
        uid: String,
        updates: Map<String, Any>,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        collection.document(uid)
            .update(updates)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e ->
                onError(e.message ?: "Erro ao atualizar agente")
            }
    }

    // 🔹 DELETE
    fun deleteAgent(
        uid: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        collection.document(uid)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e ->
                onError(e.message ?: "Erro ao deletar agente")
            }
    }
}