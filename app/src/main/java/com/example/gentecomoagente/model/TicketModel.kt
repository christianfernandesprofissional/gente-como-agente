package com.example.gentecomoagente.model

data class TicketModel(
    val id: String,
    val nomeCliente: String,
    val setor: String,
    val problema: String,
    val statusAtendimento: String,
    val textoBotaoAcao: String // "Conversar" ou "Visualizar"
)