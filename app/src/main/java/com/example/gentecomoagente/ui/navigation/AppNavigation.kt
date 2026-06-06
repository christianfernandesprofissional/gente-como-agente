package com.example.gentecomoagente.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gentecomoagente.ui.screens.HomeScreen
import com.example.gentecomoagente.ui.screens.LoginGoogleScreen
import com.example.gentecomoagente.ui.screens.LoginScreen
import com.example.gentecomoagente.ui.screens.agent.AtendimentoScreen
import com.example.gentecomoagente.ui.screens.agent.TicketsAgentScreen
import com.example.gentecomoagente.ui.screens.client.ChatClientScreen
import com.example.gentecomoagente.ui.screens.client.ChatScreen
import com.example.gentecomoagente.ui.screens.client.ClientHomeScreen
import com.example.gentecomoagente.ui.screens.client.ExistingTicketScreen
import com.example.gentecomoagente.ui.screens.gerente.AgentCreationScreen
import com.example.gentecomoagente.ui.screens.gerente.AgentEditScreen
import com.example.gentecomoagente.ui.screens.gerente.GerenteHomeScreen
import com.example.gentecomoagente.ui.screens.gerente.GerenteTicketScreen
import com.example.gentecomoagente.ui.screens.gerente.ProblemTypeScreen

@Composable
fun AppNavigation() {
    // O NavController é o "motorista" que te leva de uma tela pra outra
    val navController = rememberNavController()

    // O NavHost é o mapa. O startDestination é a tela que abre primeiro.
    NavHost(navController = navController, startDestination = Routes.LOGIN_Google) {

        // Rota 1: Tela de Suporte
        composable(Routes.TELA_INICIAL) {
            HomeScreen(navController = navController)
        }
        // Rota 2: Tela de busca de ticket existente
        composable(Routes.TICKET_EXISTENTE) {
            ExistingTicketScreen(navController = navController)
        }
        // Rota 3: Tela de chat de atendimento do cliente
        composable("${Routes.CHAT_CLIENT}/{ticketId}") { backStackEntry ->
            val ticketId = backStackEntry.arguments?.getString("ticketId") ?: ""
            ChatClientScreen(navController = navController, ticketId = ticketId)
        }
        //Rota 4: Tela de Login
        composable(Routes.LOGIN) {
            LoginScreen(navController = navController)
        }
        //Rota 5: Tela inicial do agente
        composable(Routes.TICKETS_AGENT) {
            TicketsAgentScreen(navController = navController)
        }

        // Rota 6: Tela de chat de atendimento do cliente
        composable(Routes.CHAT_AGENT) {
            AtendimentoScreen(navController = navController)
        }
        // Rota 7: Tela de Home do Gerente
        composable(Routes.GERENTE_HOME) {
            GerenteHomeScreen(navController = navController)
        }

        // Rota 8: Tela de Tickets do Gerente
        composable(Routes.GERENTE_TICKETS) {
            GerenteTicketScreen(navController = navController)
        }

        // Rota 9: Tela de Tipos de problema
        composable(Routes.PROBLEM_TYPE) {
            ProblemTypeScreen(navController = navController)
        }

        // Rota 10: Tela de cadastro de agente
        composable(Routes.AGENT_CREATION) {
            AgentCreationScreen(navController = navController)
        }

        // Rota 11: Tela de edição de agente
        composable(
            route = "${Routes.AGENT_EDIT}/{agentId}"
        ) { backStackEntry ->

            val agentId =
                backStackEntry.arguments
                    ?.getString("agentId")
                    ?: ""

            AgentEditScreen(
                navController = navController,
                agentId = agentId
            )
        }

        // Rota 12: ClienteHomeScreen
        composable(Routes.CLIENT_HOME) {
            ClientHomeScreen(navController = navController)
        }

        // Rota 13: Login-google
        composable(Routes.LOGIN_Google) {
            LoginGoogleScreen(navController = navController)
        }

        // Rota 14: Tela de chat de atendimento que recebe tanto cliente quanto agente
        composable(
            route = "${Routes.CHAT_GERAL}/{ticketId}/{userType}",
            arguments = listOf(
                navArgument("ticketId") { type = NavType.StringType },
                navArgument("userType") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val ticketId = backStackEntry.arguments?.getString("ticketId") ?: ""
            val userType = backStackEntry.arguments?.getString("userType") ?: ""

            ChatScreen(
                navController = navController,
                ticketId = ticketId,
                userType = userType
            )
        }
    }
}