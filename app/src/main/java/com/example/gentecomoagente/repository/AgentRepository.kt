package com.example.gentecomoagente.repository

import com.example.gentecomoagente.model.AgentModel
import com.google.firebase.firestore.FirebaseFirestore

class AgentRepository(
    private val authRepository: AuthRepository = AuthRepository()
) {

    private val db = FirebaseFirestore.getInstance()

    private val collection = db.collection("users")

    // 🔥 CREATE
    fun createAgent(
        adminEmail: String,
        adminPassword: String,
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

                    .addOnSuccessListener {

                        // 🔥 RELOGA ADMIN
                        authRepository.login(
                            email = adminEmail,
                            password = adminPassword,

                            onSuccess = {
                                onSuccess()
                            },

                            onError = {
                                onError(
                                    "Usuário criado, mas erro ao relogar admin"
                                )
                            }
                        )
                    }

                    .addOnFailureListener { e ->

                        onError(
                            e.message ?: "Erro ao salvar usuário"
                        )
                    }
            },

            onError = {
                onError(it)
            }
        )
    }

    // 🔄 UPDATE
    fun updateAgent(
        uid: String,
        username: String,
        role: String,
        isActive: Boolean,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        val updates = mapOf(
            "username" to username,
            "role" to role,
            "isActive" to isActive
        )

        collection.document(uid)
            .update(updates)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(
                    e.message ?: "Erro ao atualizar agente"
                )
            }
    }

    // ❌ DELETE
    fun deleteAgent(
        uid: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        collection.document(uid)
            .delete()

            .addOnSuccessListener {
                onSuccess()
            }

            .addOnFailureListener { e ->
                onError(
                    e.message ?: "Erro ao deletar agente"
                )
            }
    }

    // 🔍 FIND ALL
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
                        ?.copy(id = document.id)
                }

                onSuccess(agents)
            }

            .addOnFailureListener { e ->

                onError(
                    e.message ?: "Erro ao buscar agentes"
                )
            }
    }

    // 🔍 GET BY ID
    fun getAgentById(
        uid: String,
        onSuccess: (AgentModel?) -> Unit,
        onError: (String) -> Unit
    ) {

        collection.document(uid)
            .get()

            .addOnSuccessListener { document ->

                if (document.exists()) {

                    val agent = document
                        .toObject(AgentModel::class.java)

                    onSuccess(
                        agent?.copy(id = document.id)
                    )

                } else {

                    onSuccess(null)
                }
            }

            .addOnFailureListener { e ->

                onError(
                    e.message ?: "Erro ao buscar usuário"
                )
            }
    }
}