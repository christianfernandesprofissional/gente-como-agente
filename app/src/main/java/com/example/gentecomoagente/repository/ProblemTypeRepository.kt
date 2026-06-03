package com.example.gentecomoagente.repository

import com.example.gentecomoagente.model.ProblemTypeModel
import com.google.firebase.firestore.FirebaseFirestore

class ProblemTypeRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("problemtypes")

    // 🔥 CREATE
    fun create(
        problemType: ProblemTypeModel,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        // Usamos add() para ID automático ou set() se o ID já estiver definido no model
        // Como o usuário pediu ID automático, podemos usar add e depois atualizar o model se necessário
        // Ou simplesmente não enviar o ID no objeto e o Firestore gera um.
        
        val data = hashMapOf(
            "name" to problemType.name,
            "description" to problemType.description
        )

        collection.add(data)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Erro ao salvar tipo de problema")
            }
    }

    // 🔄 UPDATE
    fun update(
        problemType: ProblemTypeModel,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val updates = mapOf(
            "name" to problemType.name,
            "description" to problemType.description
        )

        collection.document(problemType.id)
            .update(updates)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.message ?: "Erro ao atualizar") }
    }

    // ❌ DELETE
    fun delete(
        id: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        collection.document(id)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.message ?: "Erro ao deletar") }
    }

    // 🔍 FIND ALL
    fun findAll(
        onSuccess: (List<ProblemTypeModel>) -> Unit,
        onError: (String) -> Unit
    ) {
        collection.get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { doc ->
                    val model = doc.toObject(ProblemTypeModel::class.java)
                    model?.copy(id = doc.id)
                }
                onSuccess(list)
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Erro ao buscar dados")
            }
    }
}
