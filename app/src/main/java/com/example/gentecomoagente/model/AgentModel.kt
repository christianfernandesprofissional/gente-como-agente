package com.example.gentecomoagente.model

data class AgentModel(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val role: String = "",
    val isActive: Boolean = true
)