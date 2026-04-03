package com.example.gentecomoagente.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gentecomoagente.ui.screens.HomeScreen
import com.example.gentecomoagente.ui.screens.LoginScreen
import com.example.gentecomoagente.ui.screens.agent.AtendimentoScreen
import com.example.gentecomoagente.ui.screens.agent.TicketsAgentScreen
import com.example.gentecomoagente.ui.screens.client.ChatClientScreen
import com.example.gentecomoagente.ui.screens.client.ExistingTicketScreen

@Composable
fun AppNavigation() {
    // O NavController é o "motorista" que te leva de uma tela pra outra
    val navController = rememberNavController()

    // O NavHost é o mapa. O startDestination é a tela que abre primeiro.
    NavHost(navController = navController, startDestination = "chat_client_screen") {

        // Rota 1: Tela de Suporte
        composable("support_screen") {
            HomeScreen(navController = navController)
        }
        // Rota 2: Tela de busca de ticket existente
        composable("ticket_screen") {
            ExistingTicketScreen(navController = navController)
        }
        // Rota 3: Tela de chat de atendimento do cliente
        composable("chat_client_screen") {
            ChatClientScreen(navController = navController)
        }
        //Rota 4: Tela de Login
        composable("login_screen") {
            LoginScreen(navController = navController)
        }
        //Rota 5: Tela inicial do agente
        composable("tickets_agent_screen") {
            TicketsAgentScreen(navController = navController)
        }

        // Rota 6: Tela de chat de atendimento do cliente
        composable("chat_agent_screen") {
            AtendimentoScreen(navController = navController)
        }

        // Quando você criar outras telas, é só adicionar aqui:
        // composable("employee_screen") { EmployeeScreen(navController) }
    }
}