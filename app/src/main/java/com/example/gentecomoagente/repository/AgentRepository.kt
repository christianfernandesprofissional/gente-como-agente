package com.example.gentecomoagente.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.example.gentecomoagente.model.AgentModel

class AgentRepository(
    private val authRepository: AuthRepository = AuthRepository()
) {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("users")

    // 🔥 CREATE (Auth + Firestore sincronizado)
    fun createAgent(
        email: String,
        password: String,
        agent: AgentModel,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        authRepository.register(
            email = email,
            password = password,
            onSuccess = { uid ->

                val agentWithId = agent.copy(id = uid)

                collection.document(uid)
                    .set(agentWithId)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e ->
                        onError(e.message ?: "Erro ao salvar no Firestore")
                    }
            },
            onError = { onError(it) }
        )
    }

    // 🔄 UPDATE (somente username + isActive)
    fun updateAgent(
        uid: String,
        username: String,
        isActive: Boolean,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val updates = mapOf(
            "username" to username,
            "isActive" to isActive
        )

        collection.document(uid)
            .update(updates)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e ->
                onError(e.message ?: "Erro ao atualizar agente")
            }
    }

    // ❌ DELETE (somente Firestore)
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

    // 🔍 FIND ALL (somente agents)
    fun findAllAgents(
        onSuccess: (List<AgentModel>) -> Unit,
        onError: (String) -> Unit
    ) {
        collection
            .whereEqualTo("role", "agent")
            .get()
            .addOnSuccessListener { result ->

                val agents = result.documents.mapNotNull { document ->
                    document.toObject(AgentModel::class.java)
                }

                onSuccess(agents)
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Erro ao buscar agentes")
            }
    }

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
                    onSuccess(agent?.copy(id = document.id)) // 🔥 garante ID
                } else {
                    onSuccess(null)
                }
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Erro ao buscar usuário")
            }
    }

}