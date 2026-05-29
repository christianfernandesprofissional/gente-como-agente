package com.example.gentecomoagente.service

import com.example.gentecomoagente.model.AgentModel
import com.example.gentecomoagente.repository.AgentRepository
import com.google.firebase.firestore.FirebaseFirestore

class AgentService(
    private val repository: AgentRepository = AgentRepository()
) {

    private val db = FirebaseFirestore.getInstance()

    fun createAgent(
        email: String,
        password: String,
        agent: AgentModel,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        // 🔥 valida domínio
        if (!email.endsWith("@gentecomoagente.com")) {
            onError("O email deve terminar com @gentecomoagente.com")
            return
        }

        // 🔥 valida senha
        if (password.length < 8) {
            onError("A senha deve ter no mínimo 8 caracteres")
            return
        }

        // 🔥 verifica se email já existe
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { result ->

                if (!result.isEmpty) {
                    onError("Este email já está cadastrado")
                    return@addOnSuccessListener
                }

                // 🔥 chama repository
                repository.createAgent(
                    email = email,
                    password = password,
                    agent = agent,
                    onSuccess = onSuccess,
                    onError = onError
                )
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Erro ao validar email")
            }
    }

    fun updateAgent(
        uid: String,
        username: String,
        isActive: Boolean,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        if (username.isBlank()) {
            onError("Username inválido")
            return
        }

        repository.updateAgent(
            uid = uid,
            username = username,
            isActive = isActive,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    fun deleteAgent(
        uid: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        repository.deleteAgent(
            uid = uid,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    fun findAllAgents(
        onSuccess: (List<AgentModel>) -> Unit,
        onError: (String) -> Unit
    ) {

        repository.findAllAgents(
            onSuccess = onSuccess,
            onError = onError
        )
    }

    fun getAgentById(
        uid: String,
        onSuccess: (AgentModel?) -> Unit,
        onError: (String) -> Unit
    ) {

        repository.getAgentById(
            uid = uid,
            onSuccess = onSuccess,
            onError = onError
        )
    }
}