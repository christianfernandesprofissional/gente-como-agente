package com.example.gentecomoagente.ui.screens.agent

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gentecomoagente.model.TicketModel
import com.example.gentecomoagente.repository.AuthRepository
import com.example.gentecomoagente.ui.components.CustomButton
import com.example.gentecomoagente.ui.navigation.Routes
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.gentecomoagente.repository.AgentRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketsAgentScreen(navController: NavController) {

    val authRepository = remember { AuthRepository() }
    val agentRepository = remember { AgentRepository() }
    var agentRole by remember { mutableStateOf("") }

    var tickets by remember { mutableStateOf<List<TicketModel>>(emptyList()) }
    var agentName by remember { mutableStateOf("") }

    var ticketListener by remember {
        mutableStateOf<ListenerRegistration?>(null)
    }
    var expandedFiltro by remember { mutableStateOf(false) }

    val filtros = listOf(
        "Data de Criação",
        "Última Mensagem",
        "Abertos",
        "Fechados"
    )

    var filtroSelecionado by remember {
        mutableStateOf("Data de Criação")
    }

    fun carregarTickets(filtro: String) {



        var query: Query = FirebaseFirestore.getInstance()
            .collection("tickets")

        query = when (filtro) {

            "Data de Criação" -> {
                query.orderBy(
                    "createdAt",
                    Query.Direction.DESCENDING
                )
            }

            "Última Mensagem" -> {
                query.orderBy(
                    "lastMessageAt",
                    Query.Direction.DESCENDING
                )
            }

            "Abertos" -> {
                query.whereEqualTo(
                    "status",
                    "OPEN"
                )
            }


            "Fechados" -> {
                query.whereEqualTo(
                    "status",
                    "CLOSED"
                )
            }

            else -> {
                query.orderBy(
                    "createdAt",
                    Query.Direction.DESCENDING
                )
            }
        }
        ticketListener?.remove()
        ticketListener = query.addSnapshotListener { snapshot, error ->

            if (error != null) {
                return@addSnapshotListener
            }

            tickets = snapshot?.documents?.map { document ->

                TicketModel(
                    id = document.id,
                    customerName = document.getString("customerName") ?: "",
                    customerEmail = document.getString("customerEmail") ?: "",
                    problemType = document.getString("problemType") ?: "",
                    status = document.getString("status") ?: "OPEN",
                    createdAt = document.getTimestamp("createdAt"),
                    assignedAgentId = document.getString("assignedAgentId"),
                    lastMessage = document.getString("lastMessage") ?: "",
                    lastMessageAt = document.getTimestamp("lastMessageAt"),
                    accessCode = document.getString("accessCode") ?: ""
                )

            } ?: emptyList()
        }
    }

    LaunchedEffect(Unit) {

        val currentUser = FirebaseAuth.getInstance().currentUser

        currentUser?.let { user ->

            FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.uid)
                .get()
                .addOnSuccessListener { document ->

                    agentName = document.getString("username") ?: "Agente"
                    agentRole = document.getString("role") ?: "AGENT" // 🔥 AQUI
                }
        }

        carregarTickets("Data de Criação")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        CustomButton(
            text = "Sair",
            onClick = {
                authRepository.logout()

                navController.navigate(Routes.LOGIN_Google) {
                    popUpTo(0)
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.45f)
                .height(48.dp),
            shape = RectangleShape,
            containerColor = Color(0xFFE0E0E0),
            contentColor = Color.Black,
            fontWeight = FontWeight.Medium
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Tickets de Atendimento",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Agente: $agentName",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Divider(
                color = Color(0xFF00BCD4),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = expandedFiltro,
                onExpandedChange = {
                    expandedFiltro = !expandedFiltro
                }
            ) {

                OutlinedTextField(
                    value = filtroSelecionado,
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text("Filtro")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expandedFiltro
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedFiltro,
                    onDismissRequest = {
                        expandedFiltro = false
                    }
                ) {

                    filtros.forEach { filtro ->

                        DropdownMenuItem(
                            text = {
                                Text(filtro)
                            },
                            onClick = {

                                filtroSelecionado = filtro
                                expandedFiltro = false

                                carregarTickets(filtro)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(16.dp))

            if (tickets.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Sem tickets no momento",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }

            } else {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(tickets) { ticket ->
                        TicketListItem(
                            ticket = ticket,
                            onActionClick = {
                                Log.d("CHAT", "ticketId recebido: ${ticket.id}")
                                if (agentRole.isNotBlank()) {
                                    navController.navigate("${Routes.CHAT_GERAL}/${ticket.id}/${agentRole.uppercase()}")
                                }
                            }
                        )
                    }
                }
            }
        }
    }


}

@Composable
fun TicketListItem(
    ticket: TicketModel,
    onActionClick: () -> Unit
) {

    val statusText = when (ticket.status) {
        "OPEN" -> "Aberto"
        "IN_PROGRESS" -> "Em andamento"
        "CLOSED" -> "Fechado"
        else -> ticket.status
    }

    val buttonText =
        if (ticket.status == "CLOSED") "Visualizar"
        else "Conversar"

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        // 🔹 Título
        Text(
            text = "${ticket.customerName} - ${ticket.problemType}",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5))
                .padding(12.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                // 🔹 Tipo do problema
                Text(
                    text = "Tipo: ${ticket.problemType}",
                    fontSize = 14.sp,
                    color = Color.Black
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {

                    Text(
                        text = "Status: $statusText",
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    CustomButton(
                        text = buttonText,
                        onClick = onActionClick,
                        modifier = Modifier.height(36.dp),
                        shape = RoundedCornerShape(4.dp),
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.White,
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }


}



