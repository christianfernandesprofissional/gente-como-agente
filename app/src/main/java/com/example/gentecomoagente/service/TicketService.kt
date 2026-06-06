package com.example.gentecomoagente.service

import com.example.gentecomoagente.model.TicketModel
import com.example.gentecomoagente.repository.AgentRepository
import com.example.gentecomoagente.repository.TicketRepository

class TicketService(
    private val repository: TicketRepository = TicketRepository()
){
    fun findTicketsByCustomerEmail(
        email: String,
        onSuccess: (List<TicketModel>) -> Unit,
        onError: (String) -> Unit
    ) {

        repository.findTicketsByCustomerEmail(
            email = email,
            onSuccess = onSuccess,
            onError = onError
        )
    }
}
