package com.example.gentecomoagente.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gentecomoagente.ui.screens.client.CreateTicketScreen
import com.example.gentecomoagente.ui.screens.HomeScreen
import com.example.gentecomoagente.ui.screens.LoginCompanyScreen
import com.example.gentecomoagente.ui.screens.agent.TicketsAgentScreen
import com.example.gentecomoagente.ui.screens.ChatScreen
import com.example.gentecomoagente.ui.screens.client.ClientHomeScreen
import com.example.gentecomoagente.ui.screens.gerente.AgentCreationScreen
import com.example.gentecomoagente.ui.screens.gerente.AgentEditScreen
import com.example.gentecomoagente.ui.screens.gerente.GerenteHomeScreen
import com.example.gentecomoagente.ui.screens.gerente.ProblemTypeScreen

@Composable
fun AppNavigation() {
    // O NavController é o "motorista" que te leva de uma tela pra outra
    val navController = rememberNavController()

    // O NavHost é o mapa. O startDestination é a tela que abre primeiro.
    NavHost(navController = navController, startDestination = Routes.LOGIN_Google) {

        // Rota 1: Tela de Suporte
        composable(Routes.CREATE_TICKET_SCREEN) {
            CreateTicketScreen(navController = navController)
        }

        //Rota 4: Tela de Login
        composable(Routes.LOGIN) {
            LoginCompanyScreen(navController = navController)
        }
        //Rota 5: Tela inicial do agente
        composable(Routes.TICKETS_AGENT) {
            TicketsAgentScreen(navController = navController)
        }

        // Rota 7: Tela de Home do Gerente
        composable(Routes.GERENTE_HOME) {
            GerenteHomeScreen(navController = navController)
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
            HomeScreen(navController = navController)
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