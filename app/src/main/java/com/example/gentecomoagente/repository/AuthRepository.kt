package com.example.gentecomoagente.repository

import com.google.firebase.auth.FirebaseAuth

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    // 🔐 LOGIN
    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "Erro ao fazer login")
                }
            }
    }

    // 🆕 CRIAR USUÁRIO (AUTH)
    fun register(
        email: String,
        password: String,
        onSuccess: (String) -> Unit, // 🔥 retorna UID
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid

                    if (uid != null) {
                        onSuccess(uid) // 🔥 ESSENCIAL
                    } else {
                        onError("Erro ao obter UID do usuário")
                    }
                } else {
                    onError(task.exception?.message ?: "Erro ao criar usuário")
                }
            }
    }

    // 🚪 LOGOUT
    fun logout() {
        auth.signOut()
    }

    // 👤 USER ATUAL
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}