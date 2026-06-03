package com.example.gentecomoagente.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth

class AuthRepository(
    private val context: Context? = null
) {

    private val auth = FirebaseAuth.getInstance()

    // 🔥 SharedPreferences
    private val prefs =
        context?.getSharedPreferences(
            "auth_prefs",
            Context.MODE_PRIVATE
        )

    // 🔐 LOGIN
    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(
                    e.message ?: "Erro ao fazer login"
                )
            }
    }

    // 🆕 REGISTER
    fun register(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->

                val uid = result.user?.uid

                if (uid != null) {

                    onSuccess(uid)

                } else {

                    onError("Erro ao obter UID do usuário")
                }
            }
            .addOnFailureListener { e ->

                onError(
                    e.message ?: "Erro ao criar usuário"
                )
            }
    }

    // 🚪 LOGOUT
    fun logout() {
        clearAdminCredentials()
        auth.signOut()
    }

    // 👤 UID ATUAL
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    // 👤 EMAIL ATUAL
    fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }

    // 🔥 VERIFICA LOGIN
    fun isUserLogged(): Boolean {
        return auth.currentUser != null
    }

    // =========================================
    // 🔥 ADMIN CREDENTIALS
    // =========================================

    fun saveAdminCredentials(
        email: String,
        password: String
    ) {

        prefs?.edit()
            ?.putString("admin_email", email)
            ?.putString("admin_password", password)
            ?.apply()
    }

    fun getAdminEmail(): String? {
        return prefs?.getString("admin_email", null)
    }

    fun getAdminPassword(): String? {
        return prefs?.getString("admin_password", null)
    }

    fun clearAdminCredentials() {

        prefs?.edit()
            ?.remove("admin_email")
            ?.remove("admin_password")
            ?.apply()
    }
}