package com.example.gentecomoagente.ui.screens.client

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gentecomoagente.model.TicketModel
import com.example.gentecomoagente.repository.AuthRepository
import com.example.gentecomoagente.service.TicketService
import com.example.gentecomoagente.ui.components.CustomButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.gentecomoagente.ui.navigation.Routes
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun ClientHomeScreen(navController: NavController) {

    val context = LocalContext.current

    val ticketService = remember {
        TicketService()
    }

    val authRepository = remember {
        AuthRepository()
    }

    var tickets by remember {
        mutableStateOf<List<TicketModel>>(emptyList())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }


    LaunchedEffect(Unit) {

        val email =
            authRepository.getCurrentUserEmail()

        if (email == null) {

            isLoading = false
            return@LaunchedEffect
        }

        ticketService.findTicketsByCustomerEmail(
            email = email,

            onSuccess = { lista ->

                tickets = lista.sortedByDescending {
                    it.createdAt?.toDate()?.time ?: 0L
                }

                isLoading = false
            },

            onError = {

                isLoading = false
            }
        )
    }

    // Container Principal (Fundo Branco)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()

    ) {
        // --- 2. CABEÇALHO (Refatorado com CustomTopHeader) ---
        // Envolvemos em uma Box com offset negativo para anular o padding padrão do componente
        // e deixar o botão colado na borda esquerda, como no design original.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            CustomButton(
                text = "Sair",
                onClick = {
                    authRepository.logoutGoogle(context) {
                        navController.navigate(Routes.LOGIN_Google) {
                            popUpTo(0)
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            )

            CustomButton(
                text = "Novo atendimento",
                onClick = { navController.navigate(Routes.CREATE_TICKET_SCREEN) },
                modifier = Modifier.weight(1f)
            )
        }

        // --- 3. IDENTIFICAÇÃO DO USUÁRIO ---
        if (isLoading) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator()
            }

        } else if (tickets.isEmpty()) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Text("Nenhum ticket encontrado")
            }

        } else {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(tickets) { ticket ->

                    GerenteTicketCard(ticket, navController)
                }
            }
        }
    }
}

// --- COMPONENTE: CARD DO TICKET ---
@Composable
fun GerenteTicketCard(ticket: TicketModel, navController: NavController) {

    val dateFormatted = ticket.createdAt?.toDate()?.let { date ->
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
    }

    val statusText = when (ticket.status) {
        "OPEN" -> "Em andamento"
        "IN_PROGRESS" -> "Sendo atendido"
        "CLOSED" -> "Finalizado"
        else -> ticket.status
    }

    val buttonText = if (ticket.status == "CLOSED") "Visualizar" else "Conversar"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // 🔹 Problema (agora é problemType)
            Text(
                text = "Problema: ${ticket.problemType}",
                fontSize = 15.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "Data de criação: ${dateFormatted}",
                fontSize = 15.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {

                // 🔹 Status
                Text(
                    text = "Atendimento: $statusText",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // 🔹 Botão dinâmico
                CustomButton(
                    text = buttonText,
                    onClick = {
                        navController.navigate("${Routes.CHAT_GERAL}/${ticket.id}/CLIENT")
                    },
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier.height(36.dp)
                )
            }
        }
    }
}